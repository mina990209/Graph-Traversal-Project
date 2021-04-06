package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author Mina Kim
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {
        return outDegree(v);
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> predecessor = new ArrayList<>();
        for (int i = 0; i < edgeSize(); i++) {
            int[] curr = edgelistN().get(i);
            int first = curr[0];
            int second = curr[1];
            if (first == v) {
                predecessor.add(second);
            } else if (second == v) {
                predecessor.add(first);
            }
        }
        return Iteration.iteration(predecessor.iterator());
    }

    @Override
    public Iteration<Integer> successors(int v) {
        return predecessors(v);
    }


}
