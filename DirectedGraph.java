package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Mina Kim
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        int count = 0;
        for (int i = 0; i < edgeSize(); i++) {
            int[] curr = edgelistN().get(i);
            int second = curr[1];
            if (second == v) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> predecessors = new ArrayList<>();
        for (int i = 0; i < edgelistN().size(); i++) {
            int[] curr = edgelistN().get(i);
            int first = curr[0];
            int second = curr[1];
            if (second == v) {
                predecessors.add(first);
            }
        }
        return Iteration.iteration(predecessors.iterator());
    }
}
