package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Mina Kim
 */
abstract class GraphObj extends Graph {


    /** A new, empty Graph. */
    GraphObj() {
        edgelist = new ArrayList<>();
        verticelist = new ArrayList<>();
        removedvertex = new ArrayList<>();
    }

    @Override
    public int vertexSize() {
        return verticelist.size();
    }

    @Override
    public int maxVertex() {
        int maxvertex = 0;
        for (int i = 0; i < verticelist.size(); i++) {
            int num = verticelist.get(i);
            if (num > maxvertex) {
                maxvertex = num;
            }
        }
        return maxvertex;
    }

    @Override
    public int edgeSize() {
        return edgelist.size();
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        if (isDirected()) {
            if (!verticelist.contains(v)) {
                return 0;
            }
            int count = 0;
            for (int i = 0; i < edgelist.size(); i++) {
                if (edgelist.get(i)[0] == v) {
                    count++;
                }
            }
            return count;
        } else {
            if (!verticelist.contains(v)) {
                return 0;
            }
            int count = 0;
            for (int i = 0; i < edgelist.size(); i++) {
                if (edgelist.get(i)[0] == v || edgelist.get(i)[1] == v) {
                    count++;
                }
            }
            return count;
        }
    }


    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        for (int i = 0; i < verticelist.size(); i++) {
            int curr = verticelist.get(i);
            if (u == curr) {
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean contains(int u, int v) {
        if (isDirected()) {
            if (contains(u) & contains(v)) {
                for (int i = 0; i < edgelist.size(); i++) {
                    int[] currpair = edgelist.get(i);
                    int first = currpair[0];
                    int second = currpair[1];
                    if (first == u & second == v) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            if (contains(u) & contains(v)) {
                for (int i = 0; i < edgelist.size(); i++) {
                    int[] currpair = edgelist.get(i);
                    int first = currpair[0];
                    int second = currpair[1];
                    if ((first == u & second == v) || (first == v
                            & second == u)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }




    @Override
    public int add() {
        if (verticelist.isEmpty()) {
            verticelist.add(1);
            return 1;
        } else if (removedvertex.size() == 0) {
            verticelist.add(maxVertex() + 1);
            return maxVertex();
        } else {
            int min = removedvertex.get(0);
            int minindex = 0;
            for (int i = 0; i < removedvertex.size(); i++) {
                int curr = removedvertex.get(i);
                if (curr < min) {
                    min = curr;
                    minindex = i;
                }
            }
            verticelist.add(min);
            removedvertex.remove(minindex);
            return min;
        }

    }

    @Override
    public int add(int u, int v) {
        if (isDirected()) {
            if (!contains(u, v)) {
                int[] newedge = {u, v};
                edgelist.add(newedge);
            }
            return edgeId(u, v);

        } else {
            if (!contains(u, v) || !contains(v, u)) {
                int[] newedge = {u, v};
                edgelist.add(newedge);
            }
            return edgeId(u, v);
        }
    }

    @Override
    public void remove(int v) {
        if (verticelist.contains(v)) {
            for (int i = 0; i < verticelist.size(); i++) {
                int curr = verticelist.get(i);
                if (curr == v) {
                    removedvertex.add(verticelist.get(i));
                    verticelist.remove(i);
                }
            }
            for (int j = 0; j < edgelist.size(); j++) {
                int[] currpair = edgelist.get(j);
                int first = currpair[0];
                int second = currpair[1];
                if (first == v || second == v) {
                    edgelist.remove(j);
                    j--;
                }

            }
        }
    }

    @Override
    public void remove(int u, int v) {
            for (int i = 0; i < edgelist.size(); i++) {
                int[] currpair = edgelist.get(i);
                int first = currpair[0];
                int second = currpair[1];
                if (first == u & second == v) {
                    edgelist.remove(i);
                }
            }
        }


    @Override
    public Iteration<Integer> vertices() {
        return Iteration.iteration(verticelist.iterator());
    }



    @Override
    public Iteration<Integer> successors(int v) {
        if (!contains(v)) {
            return Iteration.iteration(new ArrayList<Integer>());
        }
        ArrayList<Integer> successor = new ArrayList<>();
        for (int i = 0; i < edgelist.size(); i++) {
            int[] curr = edgelist.get(i);
            int first = curr[0];
            int second = curr[1];
            if (first == v) {
                successor.add(second);
            }
        }
        return Iteration.iteration(successor.iterator());
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        return Iteration.iteration(edgelist.iterator());
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("vertex not from Graph");
        }
    }

    @Override
    protected int edgeId(int u, int v) {
        if (isDirected()) {
            u = u - 1;
            v = v - 1;
            return (u + v) * (u + v + 1) / 2 + v;
        } else {
            int max = Math.max(u, v);
            int min = Math.min(u, v);
            return (max + min) * (max + min + 1) / 2 + min;
        }
    }
    /** return edgelist. */
    ArrayList<int[]> edgelistN() {
        return edgelist;
    }

    /** return verticelist. */
    ArrayList<Integer> verticelistN() {
        return verticelist;
    }

    /** list of Edges. */
    private ArrayList<int[]> edgelist;

    /** list of Vertices. */
    private ArrayList<Integer> verticelist;

    /** index after removing vertex. */
    private ArrayList<Integer> removedvertex;
}
