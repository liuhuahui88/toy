package com.neo.pcfg;

import java.io.File;
import java.util.HashMap;

import com.neo.util.FileUtility;

public class MapCreator {

    public static void main(String args[]) throws Exception {
        String string = FileUtility.read(new File("data/cfg.counts"));
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String line : string.split("\n")) {
            String elements[] = line.split(" ");
            if (elements[1].equals("UNARYRULE")) {
                Integer count = map.get(elements[3]);
                if (count == null) {
                    map.put(elements[3], Integer.parseInt(elements[0]));
                } else {
                    map.put(elements[3], count + Integer.parseInt(elements[0]));
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            if (map.get(key) >= 5) {
                builder.append(key).append("\n");
            }
        }
        FileUtility.write(new File("data/common_words"), builder.toString());
    }
}
