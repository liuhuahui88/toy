package com.lhh.cggf.algorithm;

public class GraphDistance {
	public static final int MAX_DISTANCE = 1000000;

	public static int[][] graphToArc(int graph[][]) {
		// Count Number Of Arcs
		int count = 0;
		for (int i = 0; i < graph.length; i++)
			for (int j = 0; j < graph.length; j++)
				if (graph[i][j] < MAX_DISTANCE)
					count++;

		// Transform Graph To Arc
		int index = 0;
		int[][] arc = new int[count][3];
		for (int i = 0; i < graph.length; i++)
			for (int j = 0; j < graph.length; j++)
				if (graph[i][j] < MAX_DISTANCE) {
					arc[index][0] = i;
					arc[index][1] = j;
					arc[index][2] = graph[i][j];
					index++;
				}

		return arc;
	}

	public static int[][] arcToGraph(int arc[][]) {
		int maxIndex = -1;
		for (int i = 0; i < arc.length; i++) {
			maxIndex = (maxIndex < arc[i][0]) ? arc[i][0] : maxIndex;
			maxIndex = (maxIndex < arc[i][1]) ? arc[i][1] : maxIndex;
		}

		int[][] graph = new int[maxIndex + 1][maxIndex + 1];
		for (int i = 0; i < graph.length; i++)
			for (int j = 0; j < graph.length; j++)
				graph[i][j] = MAX_DISTANCE;

		for (int i = 0; i < arc.length; i++) {
			int x = arc[i][0];
			int y = arc[i][1];
			graph[x][y] = arc[i][2];
			graph[y][x] = arc[i][2];
		}

		return graph;
	}

	public static int[][] calc(int graph[][]) {
		int[][] dist = new int[graph.length][graph.length];

		// Copy Distance
		for (int i = 0; i < graph.length; i++)
			for (int j = 0; j < graph.length; j++)
				dist[i][j] = graph[i][j];

		// Calculate Distance
		for (int k = 0; k < dist.length; k++)
			for (int i = 0; i < dist.length; i++)
				for (int j = 0; j < dist.length; j++)
					if ((dist[i][j] == 0 || dist[i][k] + dist[k][j] < dist[i][j])
							&& dist[i][k] < MAX_DISTANCE && dist[k][j] < MAX_DISTANCE)
						dist[i][j] = dist[i][k] + dist[k][j];

		return dist;
	}
}
