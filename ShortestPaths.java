package graph;

/* See restrictions in Graph.java. */

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;
import java.util.PriorityQueue;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Mina Kim
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        tree = new AstarQ<Integer>(new Comparing());
//        tree = new TreeSet<Integer>(compare);
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {

        tree.add(_source);
        for (int i :  _G.vertices()) {
            setWeight(i, Double.POSITIVE_INFINITY);
            setPredecessor(i, 0);
            tree.add(i);

        }
        setWeight(_source, 0);
        while (!tree.isEmpty()) {
            int poll = tree.poll();
            if (poll == _dest) {
                return;
            }
            for (int temp : _G.successors(poll)) {
                double value1 = getWeight(poll) + getWeight(poll, temp);
                double value2 = getWeight(temp);
                if (value1 < value2) {
                    tree.remove(temp);
                    setWeight(temp, value1);
                    tree.add(temp);
                    setPredecessor(temp, poll);
                }
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        LinkedList<Integer> pathto = new LinkedList<>();
        while ((getPredecessor(v) != 0)) {
            pathto.addFirst(v);
            v = getPredecessor(v);
        }
        pathto.addFirst(_source);
        return pathto;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }



    /** Making heuristics for AStar. */
    private class AStarPath extends Traversal {


        /**
         * A Traversal of G, using FRINGE as the fringe.
         *
         * @param G
         * @param fringe
         */
        protected AStarPath(Graph G, Queue<Integer> fringe) {
            super(G, fringe);
        }

        /**
         * ASTAR sometimes does not return true.
         */
        @Override
        protected boolean visit(int v) {
            if (v != ShortestPaths.this.getDest()) {
                Iteration<Integer> shortest = _G.successors(v);
                for (Integer succ : shortest) {
                    if (getWeight(succ) > (getWeight(v, succ) + getWeight(v))) {
                        setWeight(succ, (getWeight(v, succ) + getWeight(v)));
                        setPredecessor(succ, v);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
    /** class for AstarQ queue. */
    private class AstarQ<Integer> extends PriorityQueue<Integer> {

        /** comparator for AstarQ class. */
        private Comparator<Integer> comp;

        /** Treeset in AstarQ class. */
        private TreeSet<Integer> treeSet;

        /** constructor. @param comp1 */
        public AstarQ(Comparator<Integer> comp1) {
            this.comp = comp1;
            treeSet = new TreeSet<>(this.comp);
        }


        @Override
        public Integer poll() {
            return treeSet.pollFirst();
        }

        @Override
        public boolean remove(Object x) {
            treeSet.remove(x);
            return true;
        }

        @Override
        public boolean contains(Object x) {
            return treeSet.contains(x);
        }

        @Override
        public boolean isEmpty() {
            if (treeSet.size() > 0) {
                return false;
            }
            return true;
        }

        @Override
        public boolean add(Integer x) {
            treeSet.add(x);
            return true;
        }

        @Override
        public int size() {
            return treeSet.size();
        }


    }
//    private final Comparator<Integer> compare = new Comparator<Integer>() {
//        @Override
//        public int compare(Integer o1, Integer o2) {
//            double edge1 = getWeight(o1) + estimatedDistance(o1);
//            double edge2 = getWeight(o2) + estimatedDistance(o2);
//            if (edge1 > edge2) {
//                return 1;
//            }
//            if (edge1 == edge2) {
//                return o1 - o2;
//            }
//            return -1;
//        }
//
//    };
    /** Comparator and heuristic for my TreeSet. */
    private class Comparing implements Comparator<Integer> {

        @Override
        public int compare(Integer i1, Integer i2) {
            double edge1 = getWeight(i1) + estimatedDistance(i1);
            double edge2 = getWeight(i2) + estimatedDistance(i2);
            if (edge1 > edge2) {
                return 1;
            }
            if (edge1 == edge2) {
                if (i1 < i2) {
                    return -1;
                } else if (i1 > i2) {
                    return 1;
                } else {
                    return i1 - i2;
                }
            }
            return -1;
        }
    }


    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** The fringe. */
//    private TreeSet<Integer> tree;
    private AstarQ<Integer> tree;


}
