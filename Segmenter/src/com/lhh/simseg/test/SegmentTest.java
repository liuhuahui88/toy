package com.lhh.simseg.test;

import java.io.File;

import com.lhh.simseg.dic.Dictionary;
import com.lhh.simseg.dic.impl.TextDictionary;
import com.lhh.simseg.seg.Segmenter;
import com.lhh.simseg.seg.impl.BackwardMaxSegmenter;
import com.lhh.simseg.seg.impl.BackwardMinSegmenter;
import com.lhh.simseg.seg.impl.ForwardMaxSegmenter;
import com.lhh.simseg.seg.impl.ForwardMinSegmenter;

public class SegmentTest {
	private Segmenter seg;
	
	private void print(String[] result){
		if(result == null || result.length == 0)
			return ;
		
		System.out.println(result.length + " tokens!");
		
		int i;
		for(i=0 ; i<result.length-1 ; i++)
			System.out.print(result[i] + "|");
		System.out.println(result[i]);
	}
	
	public SegmentTest(Segmenter seg) {
		this.seg = seg;
	}
	
	public void run(String str) {
		String[] result = seg.handle(str);
		print(result);
	}

	public static void main(String[] args) {
		String str = "你好啊，哈哈哈！";
		
		File file = new File("dic.txt");
		Dictionary dic = TextDictionary.newInstance(file);

		Segmenter forMinSeg = new ForwardMinSegmenter(dic);
		SegmentTest forMinSegTest = new SegmentTest(forMinSeg);
		System.out.println("Forward Min Segementer");
		forMinSegTest.run(str);
		System.out.println();

		Segmenter forMaxSeg = new ForwardMaxSegmenter(dic);
		SegmentTest forMaxSegTest = new SegmentTest(forMaxSeg);
		System.out.println("Forward Max Segementer");
		forMaxSegTest.run(str);
		System.out.println();

		Segmenter backMinSeg = new BackwardMinSegmenter(dic);
		SegmentTest backMinSegSegTest = new SegmentTest(backMinSeg);
		System.out.println("Backward Min Segementer");
		backMinSegSegTest.run(str);
		System.out.println();

		Segmenter backMaxSeg = new BackwardMaxSegmenter(dic);
		SegmentTest backMaxSegSegTest = new SegmentTest(backMaxSeg);
		System.out.println("Backward Max Segementer");
		backMaxSegSegTest.run(str);
		System.out.println();
	}

}
