package com.lhh.cggf.nlp;

import java.util.Arrays;
import java.util.Collection;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;

// Parse Phrase & Dependency Grammar
public class Parser {
	private static LexicalizedParser lp = null;
	private static TreebankLanguagePack tlp = null;
	private static GrammaticalStructureFactory gsf = null;

	// Initialize Before Parsing
	public static void init() {
		lp = new LexicalizedParser("stanford/xinhuaPCFG.ser.gz");
		lp.setOptionFlags(new String[] { "-maxLength", "80" });

		tlp = new ChineseTreebankLanguagePack();
		gsf = tlp.grammaticalStructureFactory();
	}
	
	// Destroy After Parsing
	public static void destroy() {
		lp = null;
		
		tlp = null;
		gsf = null;
	}

	// Parse Phrase Grammar
	public static Tree parsePhrase(String[] sentence) {
		Tree ret = lp.apply(Arrays.asList(sentence));
		
		return ret;
	}
	
	// Parse Dependency Grammar
	public static Collection<TypedDependency> parseDependency(String[] sentence) {
		Tree tree = parsePhrase(sentence);
		
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> ret = gs.typedDependenciesCollapsed();
		
		return ret;
	}

	public static void main(String[] args) {
		String sentence = "山东烟台苹果的颜色是红色。";

		// Split Sentence
		Splitter.init();
		String[] words = Splitter.splitWithoutPOS(sentence);
		Splitter.destroy();

		// Initialize Parser
		Parser.init();
		
		// Phrase Grammar
		Tree phrase = Parser.parsePhrase(words);
		System.out.println("Phrase Grammar:");
		phrase.pennPrint();
		System.out.println();
		
		// Dependency Grammar
		Collection<TypedDependency> dependency = Parser.parseDependency(words);
		System.out.println("Dependency Grammar:");
		System.out.println(dependency);
		System.out.println();
		
		// Destroy Parser
		Parser.destroy();
	}
}
