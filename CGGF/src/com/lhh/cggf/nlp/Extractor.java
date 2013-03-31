package com.lhh.cggf.nlp;

import java.util.ArrayList;
import java.util.Arrays;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.ne.impl.Date;
import com.lhh.cggf.ne.impl.Location;
import com.lhh.cggf.ne.impl.Name;
import com.lhh.cggf.ne.impl.Gender;
import com.lhh.cggf.ne.impl.Status;
import com.lhh.cggf.ne.impl.Token;


public class Extractor {

	public static NamedEntity[] extract(String str) {
		NamedEntity[][] ess = new NamedEntity[5][];
		ess[0] = Date.extract(str);
		ess[1] = Name.extract(str);
		ess[2] = Gender.extract(str);
		ess[3] = Location.extract(str);
		ess[4] = Status.extract(str);

		int count = 0;
		for (int i = 0; i < ess.length; i++)
			count += ess[i].length;

		NamedEntity[] es = new NamedEntity[count];
		int index = 0;
		for (int i = 0; i < ess.length; i++)
			for (int j = 0; j < ess[i].length; j++)
				es[index++] = ess[i][j];

		Arrays.sort(es);

		NamedEntity[] ts = Token.extract(str);

		ArrayList<NamedEntity> eal = new ArrayList<NamedEntity>();

		for (int i = 0; i < es.length; i++)
			eal.add(es[i]);

		for (int i = 0; i < ts.length; i++) {
			boolean mark = true;
			for (int j = 0; j < es.length; j++) {
				if (ts[i].getBegin() >= es[j].getBegin()
						&& ts[i].getEnd() <= es[j].getEnd()) {
					mark = false;
					break;
				}
			}
			if (mark)
				eal.add(ts[i]);
		}

		NamedEntity[] result = new NamedEntity[eal.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = eal.get(i);

		Arrays.sort(result);

		return result;
	}

	public static void main(String[] args) {
		Splitter.init();
		
		String str = "姓名:毛泽东,性别:男,籍贯:吉林长春,出生日期:1962年6月16日,民族:朝鲜族,职位:工人.";

		NamedEntity[] nes = extract(str);
		
		for (int i = 0; i < nes.length; i++)
			System.out.println(nes[i]);
	}
}
