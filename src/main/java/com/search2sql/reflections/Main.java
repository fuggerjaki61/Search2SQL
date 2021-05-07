package com.search2sql.reflections;

import com.search2sql.parser.SearchParser;
import org.reflections8.Reflections;
import org.reflections8.scanners.FieldAnnotationsScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.scanners.TypeAnnotationsScanner;
import org.reflections8.util.ConfigurationBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException {
        long millis = System.currentTimeMillis();

        ConfigurationBuilder builder = new ConfigurationBuilder()
                .setScanners(
                        new TypeAnnotationsScanner(),
                        new SubTypesScanner(),
                        new FieldAnnotationsScanner())
                .useParallelExecutor();

        Reflections reflections = new Reflections(builder);

        List<Class<?>> classes = new ArrayList<>(reflections.getTypesAnnotatedWith(SearchParser.class));

        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();

        path += "reflections";

        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fout = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(classes);

        oos.flush();

        oos.close();

        System.out.println(System.currentTimeMillis() - millis); // takes <100ms
    }
}
