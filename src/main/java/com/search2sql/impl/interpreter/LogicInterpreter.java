package com.search2sql.impl.interpreter;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.impl.interpreter.util.ParserLoader;
import com.search2sql.impl.parser.QuotedParser;
import com.search2sql.impl.parser.RangeParser;
import com.search2sql.impl.parser.TaggedParser;
import com.search2sql.interpreter.Interpreter;
import com.search2sql.parser.Parser;
import com.search2sql.query.Query;
import com.search2sql.query.SubQuery;
import com.search2sql.table.Column;
import com.search2sql.table.Table;
import com.search2sql.table.TableConfig;

import java.util.*;

public class LogicInterpreter extends Interpreter {

    private final String keywordAnd;
    private final String keywordOr;
    private final String keywordNot;

    private final SubQuery queryAnd;
    private final SubQuery queryOr;
    private final SubQuery queryNot;

    public LogicInterpreter() {
        this("and", "or", "not");
    }

    public LogicInterpreter(String keywordAnd, String keywordOr, String keywordNot) {
        this.keywordAnd = keywordAnd;
        this.keywordOr = keywordOr;
        this.keywordNot = keywordNot;

        this.queryAnd = new SubQuery(null, null, "logic.connector.and", null);
        this.queryOr = new SubQuery(null, null, "logic.connector.or", null);
        this.queryNot = new SubQuery(null, null, "logic.operator.not", null);
    }

    @Override
    public Query interpret(String search, TableConfig tableConfig) throws InvalidSearchException {
        Map<Parser, String[]> parsers = new HashMap<>();

        for (Table table : tableConfig.getTables()) {
            for (Column column : table.getColumns()) {
                parsers.put(ParserLoader.loadParser(column.getParserId()), new String[]{table.getPrefix(), column.getName()});
            }
        }

        LinkedList<SubQuery> subQueries = new LinkedList<>();

        List<String> split = split(search, parsers.keySet());

        mainLoop:
        for (ListIterator<String> iterator = split.listIterator(); iterator.hasNext(); ) {
            int index = iterator.nextIndex();
            String current = iterator.next();

            boolean isFirst = index <= 0;
            boolean isLast = index >= split.size() - 1;

            SubQuery previous = subQueries.size() <= 0 ? new SubQuery() : subQueries.getLast();

            if (isFirst) {
                if (keywordNot.equalsIgnoreCase(current)) {
                    subQueries.add(queryNot);

                    continue;
                }
            } else {
                if (queryAnd.getType().equalsIgnoreCase(previous.getType())
                        || queryOr.getType().equalsIgnoreCase(previous.getType())) {
                    if (keywordNot.equalsIgnoreCase(current)) {
                        if (!isLast) {
                            subQueries.add(queryNot);

                            continue;
                        }
                    }
                } else {
                   if (!queryNot.getType().equalsIgnoreCase(previous.getType())) {
                       if (isLast) {
                           subQueries.add(queryOr);
                       } else {
                            if (keywordAnd.equalsIgnoreCase(current)) {
                                subQueries.add(queryAnd);

                                continue;
                            } else {
                                if (keywordOr.equalsIgnoreCase(current)) {
                                    subQueries.add(queryOr);

                                    continue;
                                } else {
                                    if (keywordNot.equalsIgnoreCase(current)) {
                                        subQueries.add(queryOr);
                                        subQueries.add(queryNot);

                                        continue;
                                    } else {
                                        subQueries.add(queryOr);
                                    }
                                }
                            }
                       }
                   }
                }
            }

            for (Map.Entry<Parser, String[]> parserEntry : parsers.entrySet()) {
                if (parserEntry.getKey().isParserFor(current)) {
                    SubQuery subQuery = parserEntry.getKey().parse(current);

                    subQuery.setColumnName((parserEntry.getValue()[0] != null ? parserEntry.getValue()[0] + "." : "") + parserEntry.getValue()[1]);

                    subQueries.add(subQuery);

                    continue mainLoop;
                }
            }

            throw new InvalidSearchException(1);
        }

        return new Query(search, tableConfig, subQueries);
    }

    private List<String> split(String search, Set<Parser> parsers) throws InvalidSearchException {
        List<String> result = new LinkedList<>();

        Set<Character> quotations = new HashSet<>();
        Set<String> ranges = new HashSet<>();

        for (Parser parser : parsers) {
            if (parser instanceof QuotedParser) {
                QuotedParser quotedParser = (QuotedParser) parser;

                if (quotedParser.getQuotation() != Character.MIN_VALUE) {
                    quotations.add(quotedParser.getQuotation());
                }
            } else if (parser instanceof RangeParser) {
                RangeParser rangeParser = (RangeParser) parser;

                if (rangeParser.getDelimiter() != null) {
                    ranges.add(rangeParser.getDelimiter());
                }

                if (rangeParser.getQuotation() != Character.MIN_VALUE) {
                    quotations.add(rangeParser.getQuotation());
                }
            } else if (parser instanceof TaggedParser) {
                TaggedParser taggedParser = (TaggedParser) parser;

                if (taggedParser.getQuotation() != Character.MIN_VALUE) {
                    quotations.add(taggedParser.getQuotation());
                }
            }
        }

        List<String> split = splitQuery(search, quotations);

        mainLoop:
        for (ListIterator<String> iterator = split.listIterator(); iterator.hasNext(); ) {
            int index = iterator.nextIndex();
            String current = iterator.next();
            boolean isQuote = quotations.contains(current.charAt(0)) && quotations.contains(current.charAt(current.length() - 1));

            if (!isQuote) {
                if (current.contains(":")) {
                    String tag = null;
                    String value = null;

                    if (current.matches("^:.*")) {
                        if (iterator.hasPrevious()) {
                            tag = split.get(index - 1);
                        }

                        result.remove(result.size() - 1);
                    } else {
                        tag = current.split(":")[0];
                    }

                    if (current.matches(".*:$")) {
                        if (iterator.hasNext()) {
                            value = split.get(index + 1);
                        }

                        iterator.next();
                    } else {
                        value = current.split(":")[1];
                    }

                    result.add(tag + ":" + value);

                    continue;
                } else {
                    for (String range : ranges) {
                        if (current.matches(range)) {
                            String prefix = null;
                            String delimiter = null;
                            String suffix = null;

                            if (current.matches("^" + range + ".*")) {
                                if (iterator.hasPrevious()) {
                                    prefix = split.get(index - 1);
                                }

                                delimiter = current;

                                result.remove(result.size() - 1);
                            } else {
                                prefix = current.split(range)[0];
                            }

                            if (current.matches(".*" + range + "$")) {
                                if (iterator.hasNext()) {
                                    suffix = split.get(index + 1);
                                }

                                iterator.next();
                            } else {
                                suffix = current.split(range)[1];
                            }

                            result.add(prefix + delimiter + suffix);

                            continue mainLoop;
                        }
                    }
                }
            }

            result.add(current);
        }

        return result;
    }

    private List<String> splitQuery(String search, Set<Character> quotations) throws InvalidSearchException {
        List<String> quotes = new LinkedList<>();

        char quote = Character.MIN_VALUE;
        String buffer = "";

        for (int i = 0; i < search.length(); i++) {
            char c = search.charAt(i);

            if (quotations.contains(c) && (i > 0 ? search.charAt(i - 1) : Character.MIN_VALUE) != '\\') {
                if (quote == Character.MIN_VALUE) {
                    if (!buffer.matches("\\s*")) {
                        quotes.add(buffer.trim());
                    }

                    buffer = "";

                    buffer += c;

                    quote = c;
                } else if (quote == c) {
                    buffer += c;

                    if (!buffer.matches("\\s*")) {
                        quotes.add(buffer.trim());
                    }

                    buffer = "";

                    quote = Character.MIN_VALUE;
                } else {
                    buffer += c;
                }
            } else {
                buffer += c;
            }
        }

        if (quote != Character.MIN_VALUE) {
            throw new InvalidSearchException(2);
        }

        if (!buffer.isEmpty()) {
            quotes.add(buffer.trim());
        }

        List<String> result = new LinkedList<>();

        for (String s : quotes) {
            if (quotations.contains(s.charAt(0)) && quotations.contains(s.charAt(s.length() - 1))) {
                result.add(s);
            } else {
                result.addAll(Arrays.asList(s.split("\\s+")));
            }
        }

        return result;
    }

    public String getKeywordAnd() {
        return keywordAnd;
    }

    public String getKeywordOr() {
        return keywordOr;
    }

    public String getKeywordNot() {
        return keywordNot;
    }
}
