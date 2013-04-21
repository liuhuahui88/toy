package com.neo.pcfg;

import java.util.ArrayList;
import java.util.HashMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.TextNode;

public class Parser {

    public HashMap<String, HashMap<String, Double>> uniRuleMap;
    public HashMap<ArrayList<String>, HashMap<String, Double>> binRuleMap;

    private static class Data {

        public String token;
        public double logProb;
        public Data leftData;
        public Data rightData;
        public String word;
    }

    public Parser(HashMap<String, HashMap<String, Double>> uniRuleMap,
            HashMap<ArrayList<String>, HashMap<String, Double>> binRuleMap) {
        this.uniRuleMap = uniRuleMap;
        this.binRuleMap = binRuleMap;
    }

    public JsonNode parse(String string, String target) {
        Data data = cyk(string, target);
        JsonNode node = build(data);
        return node;
    }

    @SuppressWarnings("unchecked")
	private Data cyk(String string, String target) {
        // i: length - 1
        // j: offset
        // k: relative split position
        String elements[] = string.split("\\s+");

        HashMap<String, Data> matrix[][] = new HashMap[elements.length][];

        matrix[0] = new HashMap[elements.length];
        for (int j = 0; j < elements.length; j++) {
            matrix[0][j] = new HashMap<String, Data>();
            HashMap<String, Double> probMap = uniRuleMap.get(elements[j]);
            if (probMap == null) {
                probMap = uniRuleMap.get("_RARE_");
            }
            for (String token : probMap.keySet()) {
                double logProb = Math.log(probMap.get(token));
                Data data = new Data();
                data.word = elements[j];
                data.token = token;
                data.logProb = logProb;
                matrix[0][j].put(token, data);
            }
        }

        for (int i = 1; i < elements.length; i++) {
            matrix[i] = new HashMap[elements.length - i];
            for (int j = 0; j < elements.length - i; j++) {
                matrix[i][j] = new HashMap<String, Data>();
                for (int k = 0; k < i; k++) {
                    for (ArrayList<String> array : binRuleMap.keySet()) {
                        HashMap<String, Double> probMap = binRuleMap.get(array);
                        Data leftData = matrix[k][j].get(
                                array.get(0));
                        Data rightData = matrix[i - k - 1][j + k + 1].get(
                                array.get(1));
                        if (leftData == null || rightData == null) {
                            continue;
                        }
                        for (String token : probMap.keySet()) {
                            double logProb = Math.log(probMap.get(token))
                                    + leftData.logProb + rightData.logProb;
                            if (!matrix[i][j].containsKey(token)
                                    || matrix[i][j].get(token).logProb
                                    < logProb) {
                                Data data = new Data();
                                data.token = token;
                                data.logProb = logProb;
                                data.leftData = leftData;
                                data.rightData = rightData;
                                matrix[i][j].put(token, data);
                            }
                        }
                    }
                }
            }
        }
        return matrix[elements.length - 1][0].get(target);
    }

    private JsonNode build(Data data) {
        if (data.leftData == null && data.rightData == null) {
            ArrayNode node = new ArrayNode(JsonNodeFactory.instance);
            node.insert(0, new TextNode(data.token));
            node.insert(1, new TextNode(data.word));
            return node;
        } else {
            JsonNode leftNode = build(data.leftData);
            JsonNode rightNode = build(data.rightData);
            ArrayNode node = new ArrayNode(JsonNodeFactory.instance);
            node.insert(0, new TextNode(data.token));
            node.insert(1, leftNode);
            node.insert(2, rightNode);
            return node;
        }
    }

    public static Parser create(String string) {
        HashMap<String, Double> ntMap = new HashMap<String, Double>();
        HashMap<String, HashMap<String, Double>> uniRuleMap =
                new HashMap<String, HashMap<String, Double>>();
        HashMap<ArrayList<String>, HashMap<String, Double>> binRuleMap =
                new HashMap<ArrayList<String>, HashMap<String, Double>>();
        for (String line : string.split("\n")) {
            String elements[] = line.split("\\s+");
            if (elements[1].equals("NONTERMINAL")) {
                ntMap.put(elements[2], Double.parseDouble(elements[0]));
            } else if (elements[1].equals("UNARYRULE")) {
                if (!uniRuleMap.containsKey(elements[3])) {
                    uniRuleMap.put(elements[3], new HashMap<String, Double>());
                }
                uniRuleMap.get(elements[3]).put(elements[2],
                        Double.parseDouble(elements[0]));
            } else if (elements[1].equals("BINARYRULE")) {
                ArrayList<String> array = new ArrayList<String>();
                array.add(elements[3]);
                array.add(elements[4]);
                if (!binRuleMap.containsKey(array)) {
                    binRuleMap.put(array, new HashMap<String, Double>());
                }
                binRuleMap.get(array).put(elements[2],
                        Double.parseDouble(elements[0]));
            }
        }
        for (String word : uniRuleMap.keySet()) {
            HashMap<String, Double> probMap = uniRuleMap.get(word);
            for (String token : probMap.keySet()) {
                double prob = probMap.get(token) / ntMap.get(token);
                probMap.put(token, prob);
            }
        }
        for (ArrayList<String> array : binRuleMap.keySet()) {
            HashMap<String, Double> probMap = binRuleMap.get(array);
            for (String token : probMap.keySet()) {
                double prob = probMap.get(token) / ntMap.get(token);
                probMap.put(token, prob);
            }
        }
        return new Parser(uniRuleMap, binRuleMap);
    }
}
