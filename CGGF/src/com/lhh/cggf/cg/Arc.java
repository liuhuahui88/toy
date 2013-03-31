package com.lhh.cggf.cg;

public class Arc {
	private String name;
	private Node from;
	private Node to;

	public Arc(String n, Node f, Node t) {
		name = n;
		from = f;
		to = t;
	}

	public String getName() {
		return name;
	}

	public Node getFrom() {
		return from;
	}

	public Node getTo() {
		return to;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o.getClass() != getClass())
			return false;
		Arc a = (Arc) o;
		if (a.from.equals(from) && a.to.equals(to) && a.name.equals(name))
			return true;
		else
			return false;
	}

	public static void main(String[] args) {
	}

}
