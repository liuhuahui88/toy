package com.lhh.cggf.ne;

public abstract class NamedEntity implements Comparable<NamedEntity> {
	private int begin;
	private int end;

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	public int compareTo(NamedEntity other) {
		int b1 = getBegin();
		int b2 = other.getBegin();
		int e1 = getEnd();
		int e2 = other.getEnd();
		if (b1 < b2)
			return -1;
		else if (b1 > b2)
			return 1;
		else {
			if (e1 < e2)
				return -1;
			else if (e1 > e2)
				return 1;
			else
				return 0;
		}
	}

	public NamedEntity(int b, int e) {
		begin = b;
		end = e;
	}
	
	public String toString(){
		return "["+getType()+", "+getBegin()+", "+getEnd()+"] "+getContent();
	}

	public abstract String getType();

	public abstract String getContent();
}
