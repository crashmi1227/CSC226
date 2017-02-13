/* MWST.java
   CSC 226 - Fall 2016
   Assignment 3 - Minimum Weight Spanning Tree Template
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java MWST
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java MWST file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 08/02/2014
*/

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;

class Edge implements Comparable<Edge> {
  public int weight;
  public Node v; // The Source
  public Node w; // The Destination.

  public Edge(Node src, Node dest, int x) {
    weight = x;
    v = src;
    w = dest;
  }

  public boolean has(Node x) {
    if (this.v == x || this.w == x)  return true;
    else return false;  
  }

  public int compareTo(Edge e) {
    return Integer.compare(this.weight, e.weight);
  }
}

class Node {
  public int val;
  public ArrayList<Edge> edges;
  public UnionFind uf;

  public Node(int x) {
    val = x;
    edges = new ArrayList<Edge>();
  }
}

class UnionFind{
  public UnionFind parent;
  public Node key;
  public int rank;

  public UnionFind(Node x) {
    parent = this;
    key = x;
    rank = 0;
  }

  public static UnionFind make_set(Node x) {
    return new UnionFind(x);
  }

  public UnionFind find() {
    UnionFind current = this;
    if (current.parent == current) return current;
    else return current.parent.find(current); 
  }
  
  public UnionFind find(UnionFind last) {
    UnionFind current = this;
    last.parent = current.parent;
    if (current.parent == current) return current;
    else return current.parent.find(current);
  }

  public UnionFind union(UnionFind other) { 
    UnionFind bigger, smaller, x, y;

    x = this.find(); 
    y = other.find(); 
    
    if (x.rank > y.rank) {
      bigger = x;
      smaller = y;
    } else {
      smaller = y;
      bigger = x;
    }
    // Attach the smaller to the larger.
    smaller.parent = bigger;
    if (smaller.rank > bigger.rank) bigger.rank = smaller.rank + 1;
    else if (smaller.rank == bigger.rank) bigger.rank += 1;
    return bigger;
  }
}

//Do not change the name of the MWST class
public class MWST{

  /* mwst(G)
     Given an adjacency matrix for graph G, return the total weight
     of all edges in a minimum weight spanning tree.
     If G[i][j] == 0, there is no edge between vertex i and vertex j
     If G[i][j] > 0, there is an edge between vertices i and j, and the
     value of G[i][j] gives the weight of the edge.
     No entries of G will be negative.
     */
  static int MWST(int[][] G){
    int numVerts = G.length;

    /* Find a minimum weight spanning tree by any method */
    /* (You may add extra functions if necessary) */
    /* ... Your code here ... */

    
    Node[] T = new Node[numVerts];
    UnionFind[] U = new UnionFind[numVerts];
    ArrayList<Edge> E = new ArrayList<Edge>();
    E.ensureCapacity(numVerts);
    int in_tree = 1;

    ArrayList<Edge> M = new ArrayList<Edge>(); 
    M.ensureCapacity(numVerts);
    
    for (int i=0; i < numVerts; i++) {
      T[i] = new Node(i);
      U[i] = UnionFind.make_set(T[i]);
      T[i].uf = U[i];
    }
    
    for (int i=0; i < numVerts-1; i++) {
      for (int j=i+1; j < numVerts; j++) {
        if (G[i][j] > 0) {
          Edge e = new Edge(T[i], T[j], G[i][j]);
          E.add(e);
        }
      }  
    }
    
    Collections.sort(E);
    for (Edge e: E) {
      if (!e.v.uf.find().equals(e.w.uf.find())) {
        M.add(e);
        e.v.uf.union(e.w.uf);
        e.v.uf.union(e.v.uf);
      }
    }

    int totalWeight = 0;

    for (Edge e : M) totalWeight += e.weight;
    
    return totalWeight;
  }	
		
	/* main()
	   Contains code to test the MWST function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			int totalWeight = MWST(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}
