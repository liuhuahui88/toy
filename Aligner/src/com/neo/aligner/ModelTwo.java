package com.neo.aligner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.neo.aligner.Corpus.Pair;
import com.neo.util.FileUtility;

public class ModelTwo {

    public HashMap<String, HashMap<String, Double>> replaceProbMap;
    public HashMap<ArrayList<Integer>, double[][]> reorderProbMap;
    
    public ModelTwo(HashMap<String, HashMap<String, Double>> replaceProbMap,
    		HashMap<ArrayList<Integer>, double[][]> reorderProbMap) {
    	this.replaceProbMap = replaceProbMap;
    	this.reorderProbMap = reorderProbMap;
    }
    
    public StringBuilder align(int id, ArrayList<String> english,
            ArrayList<String> spanish) {
        StringBuilder builder = new StringBuilder();
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        lengths.add(english.size());
        lengths.add(spanish.size());
        double orderProbArray[][] = reorderProbMap.get(lengths);
        for (int i = 0; i < spanish.size(); i++) {
            String spanishWord = spanish.get(i);
            double maxProb = 0.0;
            int maxIndex = -1;
            for (int j = 0; j < english.size(); j++) {
                String englishWord = english.get(j);
                HashMap<String, Double> wordProbMap =
                		replaceProbMap.get(englishWord);
                if (wordProbMap.containsKey(spanishWord)) {
                    double prob = wordProbMap.get(spanishWord)
                    		* orderProbArray[i][j];
                    if (prob > maxProb) {
                        maxProb = prob;
                        maxIndex = j;
                    }
                }
            }
            builder.append(id + 1).append(" ").append(maxIndex).append(" ")
                    .append(i + 1).append("\n");
        }
        return builder;
    }
    
    public static ModelTwo create(Corpus corpus, ModelOne modelOne, int round) {
        ModelTwo model = initialize(corpus, modelOne);
        for (int i = 0; i < round; i++) {
            train(model, corpus);
            System.out.println("Round " + (i + 1) + " Done");
        }
        return model;
    }
    
    private static ModelTwo initialize(Corpus corpus, ModelOne modelOne) {
    	HashMap<ArrayList<Integer>, double[][]>	reorderProbMap =
    			new HashMap<ArrayList<Integer>, double[][]>();
    	for (Pair pair : corpus.pairs) {
            ArrayList<Integer> lengths = new ArrayList<Integer>();
            lengths.add(pair.english.size());
            lengths.add(pair.spanish.size());
            if (!reorderProbMap.containsKey(lengths)) {
                double orderProbArray[][] =
                		new double[pair.spanish.size()][pair.english.size()];
            	reorderProbMap.put(lengths, orderProbArray);
            	double prob = 1.0 / pair.english.size();
                for (int i = 0; i < pair.spanish.size(); i++) {
                	for (int j = 0; j < pair.english.size(); j++) {
                		orderProbArray[i][j] = prob;
                	}
                }
            }
    	}
        return new ModelTwo(modelOne.replaceProbMap, reorderProbMap);
    }
    
    private static void train(ModelTwo model, Corpus corpus) {
    	HashMap<String, HashMap<String, Double>> newReplaceProbMap =
                new HashMap<String, HashMap<String, Double>>();
    	HashMap<ArrayList<Integer>, double[][]> newReorderProbMap =
    			new HashMap<ArrayList<Integer>, double[][]>();
        for (Pair pair : corpus.pairs) {
            ArrayList<String> spanish = pair.spanish;
            ArrayList<String> english = pair.english;
            ArrayList<Integer> lengths = new ArrayList<Integer>();
            lengths.add(english.size());
            lengths.add(spanish.size());
            for (int i = 0; i < spanish.size(); i++) {
                String spanishWord = spanish.get(i);
                double normalizer = 0.0;
                HashMap<Integer, Double> deltaMap =
                        new HashMap<Integer, Double>();
                for (int j = 0; j < english.size(); j++) {
                    String englishWord = english.get(j);
                    double tProb = model.replaceProbMap.get(englishWord)
                            .get(spanishWord);
                    double pProb = model.reorderProbMap.get(lengths)[i][j];
                    double prob = tProb * pProb; 
                    normalizer += prob;
                    deltaMap.put(j, prob);
                }
                for (Integer index : deltaMap.keySet()) {
                    double prob = deltaMap.get(index) / normalizer;
                	String englishWord = english.get(index);
                    if (!newReplaceProbMap.containsKey(englishWord)) {
                        newReplaceProbMap.put(englishWord,
                                new HashMap<String, Double>());
                    }
                    HashMap<String, Double> newWordProbMap =
                            newReplaceProbMap.get(englishWord);
                    if (!newWordProbMap.containsKey(spanishWord)) {
                        newWordProbMap.put(spanishWord, prob);
                    } else {
                        newWordProbMap.put(spanishWord,
                                prob + newWordProbMap.get(spanishWord));
                    }
                    if (!newReorderProbMap.containsKey(lengths)) {
                    	newReorderProbMap.put(lengths,
                    			new double [spanish.size()][english.size()]);
                    }
                    double newOrderProbArray[][] =
                    		newReorderProbMap.get(lengths);
                    newOrderProbArray[i][index] += prob;
                }
            }
        }
        for (String englishWord : newReplaceProbMap.keySet()) {
            HashMap<String, Double> newWordProbMap =
            		newReplaceProbMap.get(englishWord);
            double sum = 0.0;
            for (String spanishWord : newWordProbMap.keySet()) {
            	sum += newWordProbMap.get(spanishWord);
            }
            for (String spanishWord : newWordProbMap.keySet()) {
                double prob = newWordProbMap.get(spanishWord);
                newWordProbMap.put(spanishWord, prob / sum);
            }
        }
        for (ArrayList<Integer> lengths : newReorderProbMap.keySet()) {
        	double newOrderProbArray[][] = newReorderProbMap.get(lengths);
        	for (int i = 0; i < newOrderProbArray.length; i++) {
            	double sum = 0.0;
        		for (int j = 0; j < newOrderProbArray[i].length; j++) {
        			sum += newOrderProbArray[i][j];
        		}
        		for (int j = 0; j < newOrderProbArray[i].length; j++) {
        			newOrderProbArray[i][j] /= sum;
        		}
        	}
        }
        model.replaceProbMap = newReplaceProbMap;
        model.reorderProbMap = newReorderProbMap;
    }
    
    public static void main(String args[]) {
        File englishFile = new File("data/corpus.en");
        File spanishFile = new File("data/corpus.es");
        String englishString = FileUtility.read(englishFile);
        String spanishString = FileUtility.read(spanishFile);
        Corpus corpus = Corpus.create(englishString, spanishString);
        System.out.println(corpus.englishWordSet.size());
        System.out.println(corpus.spanishWordSet.size());
        File modelOneFile = new File("data/m1");
        String modelOneString = FileUtility.read(modelOneFile);
        ModelOne modelOne = ModelOne.load(modelOneString);
        ModelTwo model = ModelTwo.create(corpus, modelOne, 5);
        
	    File testEnglishFile = new File("data/test.en");
	    File testSpanishFile = new File("data/test.es");
	    String testEnglishString = FileUtility.read(testEnglishFile);
	    String testSpanishString = FileUtility.read(testSpanishFile);
	    Corpus testCorpus = Corpus.create(testEnglishString, testSpanishString);
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < testCorpus.pairs.size(); i++) {
	        Pair pair = testCorpus.pairs.get(i);
	        builder.append(model.align(i, pair.english, pair.spanish));
	    }
	    FileUtility.write(new File("data/output"), builder.toString());
    }
}
