
![GitHub release (latest by date)](https://img.shields.io/github/v/release/fuggerjaki61/search2sql) [![Build Status](https://travis-ci.org/fuggerjaki61/search2sql.svg?branch=master)](https://travis-ci.org/fuggerjaki61/search2sql) 

# Search2SQL

Search2SQL is a Java library that can be used to convert search expressions to a valid parts of a SQL query. Search2SQL aims to be highly configurable for every usage scenario but also to be easy to understand and use. 

### Goals
 - Simple API Design
 - Easy to configure and use
 - Lightweight 

### Documentation

 > If you don't want to *waste* ;) time reading the *Full Documentation*, please take 5 minutes and read the *Quick Documentation*.

[Wiki](https://github.com/fuggerjaki61/Search2SQL/wiki) | [Full Documentation](https://github.com/fuggerjaki61/Search2SQL/wiki/Full-Documentation) | [Quick Documentation](https://github.com/fuggerjaki61/Search2SQL/wiki/Quick-Documentation)

### Usage
Maven:
```xml
<dependency>
  <groupId>com.search2sql</groupId>
  <artifactId>search2sql</artifactId>
  <version>1.0-echo-rc1</version>
</dependency>
```

### Examples

A simple usage would be to configure a table

```java
TableConfig config = new TableConfig("tableName", // your sql table name
	new Column("id", "int"), // column named 'id' with an integer value
	new Column("name", "text"), // column named 'text' with any text value
	new Column("date", "date")); // column named 'date' with a date value
```

and then translate a search expression

```java
// this can be used to translate it to sql
String sql = new FileTranslator().translate(expression, // input from the user
	config, // previously configured
	new BasicInterpreter() // instance of an intepreter
```

this will generate something that follows this pattern `tableName.columnName = ? or tableName.columnName like(?)`. The `?` will be replaced by a JDBC `PreparedStatement` with the real values to prevent SQL Injection (utility is going to be added).

### License

Search2SQL is released under the [MIT License](LICENSE).
