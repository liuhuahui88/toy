package com.neo.pcfg;

import java.io.File;

import com.neo.util.FileUtility;

public class Application {

    public static void main(String args[]) throws Exception {
        String modelString = FileUtility.read(
        		new File("data/cfg_vert.counts.new"));
        Parser parser = Parser.create(modelString);

        String testString = FileUtility.read(
                new File("data/parse_test.dat"));
        StringBuilder builder = new StringBuilder();
        for (String line : testString.split("\n")) {
            builder.append(parser.parse(line, "SBARQ").toString()).append("\n");
        }
        FileUtility.write(new File("data/output"), builder.toString());
    }
}
