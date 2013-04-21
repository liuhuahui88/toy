package com.neo.pcfg;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.TextNode;

import com.neo.util.FileUtility;

public class Transformer {

    public static void transform(JsonNode node, HashSet<String> set) {
        if (!node.isArray()) {
            return;
        }
        ArrayNode array = (ArrayNode) node;

        if (node.size() == 2) {
            if (!set.contains(array.get(1).asText())) {
                array.set(1, new TextNode("_RARE_"));
            }
        } else if (node.size() == 3) {
            transform(array.get(1), set);
            transform(array.get(2), set);
        }
    }

    public static void main(String[] args) throws Exception {
        HashSet<String> set = new HashSet<String>();
        String words = FileUtility.read(new File("data/common_words"));
        set.addAll(Arrays.asList(words.split("\n")));

        ObjectMapper MAPPER = new ObjectMapper();
        String string = FileUtility.read(new File("data/parse_train_vert.dat"));
        StringBuilder builder = new StringBuilder();
        for (String line : string.split("\n")) {
            JsonNode node = MAPPER.readTree(line);
            transform(node, set);
            builder.append(node).append("\n");
        }
        FileUtility.write(new File("data/parse_train_vert.dat.new"),
                builder.toString());
    }
}
