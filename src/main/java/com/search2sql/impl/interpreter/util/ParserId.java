package com.search2sql.impl.interpreter.util;

import com.search2sql.exception.IllegalUseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserId {

    private static final Pattern splitterPattern = Pattern.compile("\\s*(?<id>[^#\\s]*)\\s*(?:#(?<params>.*)|)");

    private final String id;

    private final List<String> rawParameters;
    private final List<Class<?>> parametersTypes;
    private final List<Object> parameters;

    public ParserId(String id, List<String> rawParameters, List<Class<?>> parametersTypes, List<Object> parameters) {
        this.id = id;
        this.rawParameters = rawParameters == null ? null : Collections.unmodifiableList(rawParameters);
        this.parametersTypes = parametersTypes == null ? null : Collections.unmodifiableList(parametersTypes);
        this.parameters = parameters == null ? null : Collections.unmodifiableList(parameters);
    }

    public static ParserId valueOf(String parserId) throws IllegalUseException {
        if (parserId.matches(splitterPattern.pattern())) {
            String id = "";
            String parameters = "";

            Matcher matcher = splitterPattern.matcher(parserId);

            if (matcher.find()) {
                id = matcher.group("id");
                parameters = matcher.group("params");
            }

            if (parameters != null && !parameters.isEmpty()) {
                List<String> rawParameters = evaluateRawParameters(parameters);
                List<Object> parsedParameters = evaluateParameters(rawParameters);
                List<Class<?>> parameterValues = evaluateParameterTypes(parsedParameters);

                return new ParserId(id, rawParameters, parameterValues, parsedParameters);
            } else {
                return new ParserId(id, null, null, null);
            }
        }

        throw new IllegalUseException(String.format("The parser id '%s' is invalid.", parserId));
    }

    private static List<Class<?>> evaluateParameterTypes(List<Object> parameters) {
        List<Class<?>> result = new ArrayList<>();

        for (Object o : parameters) {
            if (o instanceof Integer) {
                result.add(Integer.TYPE);
            } else if (o instanceof Long) {
                result.add(Long.TYPE);
            } else if (o instanceof Float) {
                result.add(Float.TYPE);
            } else if (o instanceof Double) {
                result.add(Double.TYPE);
            } else {
                result.add(o.getClass());
            }
        }

        return result;
    }

    private static List<Object> evaluateParameters(List<String> rawParameters) {
        List<Object> result = new ArrayList<>();

        for (String rawParam : rawParameters) {
            if (rawParam.matches("^\\s*(?:\\+|-|)\\d+\\s*$")) {
                result.add(Integer.parseInt(rawParam.trim()));
            } else if (rawParam.matches("^\\s*(?:\\+|-|)\\d+\\s*(?:l|)\\s*$")) {
                String temp = rawParam;

                temp = temp.replace("l", "");

                result.add(Long.parseLong(temp.trim()));
            } else if (rawParam.matches("^\\s*(?:\\+|-|)\\d+(?:\\.\\d+|)\\s*f\\s*$")) {
                String temp = rawParam;

                temp = temp.replace("f", "");

                result.add(Float.parseFloat(temp.trim()));
            } else if (rawParam.matches("^\\s*(?:\\+|-|)\\d+(?:\\.\\d+|)\\s*(?:d|)\\s*$")) {
                result.add(Double.parseDouble(rawParam.trim()));
            } else if (rawParam.matches("^\\s*true\\s*$")) {
                result.add(true);
            } else if (rawParam.matches("^\\s*false\\s*$")) {
                result.add(false);
            } else {
                String temp = rawParam;

                temp = temp.replaceAll("^\\\\", "");
                temp = temp.replaceAll("\\\\$", "");

                result.add(temp);
            }
        }

        return result;
    }

    private static List<String> evaluateRawParameters(String parameters) {
        List<String> result = new ArrayList<>();

        StringBuilder param = new StringBuilder();

        for (int i = 0; i < parameters.length(); i++) {
            char currentChar = parameters.charAt(i);

            if (currentChar == ',' && (i - 1 < 0 || parameters.charAt(i - 1) != '\\')) {
                result.add(param.toString().trim());
                param = new StringBuilder();
            } else {
                param.append(currentChar);
            }
        }

        result.add(param.toString().trim());

        return result;
    }

    public String getId() {
        return id;
    }

    public List<String> getRawParameters() {
        return rawParameters;
    }

    public List<Class<?>> getParametersTypes() {
        return parametersTypes;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "ParserId{" +
                "id='" + id + '\'' +
                ", rawParameters=" + rawParameters +
                ", parametersValues=" + parametersTypes +
                ", parameters=" + parameters +
                '}';
    }
}
