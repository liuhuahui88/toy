package com.lhh.cggf.cg;

public class Node {
	private int id;
	private String name;

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o.getClass() != getClass())
			return false;
		Node node = (Node) o;
		if (name.equals(node.name) && id == node.id)
			return true;
		else
			return false;
	}

	public Node(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
