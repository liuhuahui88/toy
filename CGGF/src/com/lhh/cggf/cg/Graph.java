package com.lhh.cggf.cg;

import java.util.ArrayList;

import com.lhh.cggf.algorithm.Permutation;

public class Graph {
	private ArrayList<Node> nodes;
	private ArrayList<Arc> arcs;

	public Graph() {
		nodes = new ArrayList<Node>();
		arcs = new ArrayList<Arc>();
	}

	public Arc[] getArcStartWith(Node node) {
		ArrayList<Arc> arcArray = new ArrayList<Arc>();
		for (int i = 0; i < arcs.size(); i++)
			if (arcs.get(i).getFrom().equals(node))
				arcArray.add(arcs.get(i));
		if (arcArray.size() == 0)
			return null;
		else {
			Arc[] result = new Arc[arcArray.size()];
			arcArray.toArray(result);
			return result;
		}
	}

	public Arc[] getArcEndWith(Node node) {
		ArrayList<Arc> arcArray = new ArrayList<Arc>();
		for (int i = 0; i < arcs.size(); i++)
			if (arcs.get(i).getTo().equals(node))
				arcArray.add(arcs.get(i));
		if (arcArray.size() == 0)
			return null;
		else {
			Arc[] result = new Arc[arcArray.size()];
			arcArray.toArray(result);
			return result;
		}
	}

	public Arc[] getAllArc() {
		Arc[] result = new Arc[arcs.size()];
		arcs.toArray(result);
		return result;
	}

	public Node[] getAllNode() {
		Node[] result = new Node[nodes.size()];
		nodes.toArray(result);
		return result;
	}

	public void addNode(Node n) {
		if (!nodes.contains(n))
			nodes.add(n);
	}

	public void addArc(Arc a) {
		if (!arcs.contains(a)) {
			addNode(a.getFrom());
			int indexF = nodes.indexOf(a.getFrom());
			addNode(a.getTo());
			int indexT = nodes.indexOf(a.getTo());
			arcs.add(new Arc(a.getName(), nodes.get(indexF), nodes.get(indexT)));
		}
	}

	public double coverFactor(Graph g, int[] map, boolean[] arcMark) {
		for (int i = 0; i < nodes.size(); i++) {
			Node n1 = nodes.get(i);
			Node n2 = g.nodes.get(map[i]);
			if (!n1.getName().equals(n2.getName()))
				return 0;
		}

		int factor = 0;
		for (int i = 0; i < arcs.size(); i++) {
			Arc a1 = arcs.get(i);

			Node f1 = a1.getFrom();
			int indexF = 0;
			while (!nodes.get(indexF).equals(f1))
				indexF++;
			Node f2 = g.nodes.get(map[indexF]);

			Node t1 = a1.getTo();
			int indexT = 0;
			while (!nodes.get(indexT).equals(t1))
				indexT++;
			Node t2 = g.nodes.get(map[indexT]);

			String name = a1.getName();

			if (g.arcs.contains(new Arc(name, f2, t2))) {
				factor += 1;
				arcMark[i] = true;
			}
		}
		return factor / arcs.size();
	}

	public double coverFactor(Graph g) {
		if (nodes.size() > g.nodes.size())
			return 0;

		int[][] maps = Permutation.generate(nodes.size(), g.nodes.size());

		boolean[] arcMark = new boolean[arcs.size()];
		double max = 0;
		for (int i = 0; i < maps.length; i++) {
			double tmax = coverFactor(g, maps[i], arcMark);
			max = max > tmax ? max : tmax;
		}
		int count = 0;
		for (int i = 0; i < arcMark.length; i++)
			if (arcMark[i])
				count++;
		return 0.5 * max + 0.5 * (double) count / arcMark.length;
	}

	public boolean isCoveredBy(Graph g, int[] map) {
		for (int i = 0; i < nodes.size(); i++) {
			Node n1 = nodes.get(i);
			Node n2 = g.nodes.get(map[i]);
			if (!n1.getName().equals(n2.getName()))
				return false;
		}
		for (int i = 0; i < arcs.size(); i++) {
			Arc a1 = arcs.get(i);

			Node f1 = a1.getFrom();
			int indexF = 0;
			while (!nodes.get(indexF).equals(f1))
				indexF++;
			Node f2 = g.nodes.get(map[indexF]);

			Node t1 = a1.getTo();
			int indexT = 0;
			while (!nodes.get(indexT).equals(t1))
				indexT++;
			Node t2 = g.nodes.get(map[indexT]);

			String name = a1.getName();

			if (!g.arcs.contains(new Arc(name, f2, t2)))
				return false;
		}
		return true;
	}

	public boolean isCoveredBy(Graph g) {
		if (nodes.size() > g.nodes.size())
			return false;

		int[][] maps = Permutation.generate(nodes.size(), g.nodes.size());

		for (int i = 0; i < maps.length; i++)
			if (isCoveredBy(g, maps[i]))
				return true;
		return false;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Node Information:\n");
		for(int i=0 ; i<nodes.size() ; i++) {
			Node node = nodes.get(i);
			sb.append(node.getId() + " : ");
			sb.append(node.getName() + "\n");
		}

		sb.append("Arc Informatioin:\n");
		for(int i=0 ; i<arcs.size() ; i++) {
			Arc arc = arcs.get(i);
			sb.append(arc.getFrom().getId() + " : ");
			sb.append(arc.getTo().getId() + " : ");
			sb.append(arc.getName() + "\n");
		}
		
		return sb.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph g1 = new Graph();
		g1.addArc(new Arc("un", new Node(1, "a"), new Node(4, "b")));
		g1.addArc(new Arc("un", new Node(4, "b"), new Node(1, "a")));
		g1.addArc(new Arc("un", new Node(4, "b"), new Node(3, "c")));
		g1.addArc(new Arc("un", new Node(3, "c"), new Node(4, "b")));
		System.out.println(g1.getAllNode().length);
		System.out.println(g1.getAllArc().length);
		Graph g2 = new Graph();
		g2.addArc(new Arc("un", new Node(3, "a"), new Node(4, "b")));
		g2.addArc(new Arc("un", new Node(5, "b"), new Node(6, "c")));
		System.out.println(g1.isCoveredBy(g2));
	}

}
