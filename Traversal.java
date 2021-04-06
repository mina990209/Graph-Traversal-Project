package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular collection of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.
 *
 *  Generally, the client will extend Traversal.  By overriding the visit
 *  method, the client can determine what happens when a node is visited.
 *  By supplying an appropriate type of Queue object to the constructor,
 *  the client can control the behavior of the fringe. By overriding the
 *  shouldPostVisit and postVisit methods, the client can arrange for
 *  post-visits of a node (as in depth-first search).  By overriding
 *  the reverseSuccessors and processSuccessor methods, the client can control
 *  the addition of neighbor vertices to the fringe when a vertex is visited.
 *
 *  Traversals may be interrupted or restarted, remembering the previously
 *  marked vertices.
 *  @author Mina Kim
 */
public abstract class Traversal {

    /**
     * A Traversal of G, using FRINGE as the fringe.
     */
    protected Traversal(Graph G, Queue<Integer> fringe) {
        _G = G;
        _fringe = fringe;
        marked = new boolean[G.maxVertex()];
        mark = new ArrayList<>();
        postvisit = new ArrayList<>();

    }

    /**
     * Unmark all vertices in the graph.
     */
    public void clear() {
        for (int i = 0; i < _G.vertexSize(); i++) {
            marked[i] = false;
        }
    }

    /**
     * Initialize the fringe to V0 and perform a traversal.
     */
    public void traverse(Collection<Integer> V0) {
        _fringe.addAll(V0);
        while (!_fringe.isEmpty()) {
            int removed = _fringe.remove();
            if (marked(removed)) {
                if (shouldPostVisit(removed) && !postvisit.contains(removed)) {
                    postVisit(removed);
                    postvisit.add(removed);
                }
            } else {
                mark(removed);
                visit(removed);
                _fringe.add(removed);
                for (int i : _G.successors(removed)) {
                    if (processSuccessor(removed, i)) {
                        _fringe.add(i);
                    }
                }
            }
        }
    }


    /** Initialize the fringe to { V0 } and perform a traversal. */
    public void traverse(int v0) {
        traverse(Arrays.<Integer>asList(v0));
    }

    /** Returns true iff V has been marked. */
    protected boolean marked(int v) {
        if (marked[v - 1]) {
            return true;
        }
        return false;
    }

    /** Mark vertex V. */
    protected void mark(int v) {
        marked[v - 1] = true;
    }

    /** Perform a visit on vertex V.  Returns false iff the traversal is to
     *  terminate immediately. */
    protected boolean visit(int v) {
        return true;
    }

    /** Return true if we should postVisit V after traversing its
     *  successors.  (Post-visiting generally is useful only for depth-first
     *  traversals, although we define it for all traversals.) */
    protected boolean shouldPostVisit(int v) {
        return false;
    }

    /** Revisit vertex V after traversing its successors.  Returns false iff
     *  the traversal is to terminate immediately. */
    protected boolean postVisit(int v) {
        return true;
    }

    /** Return true if we should schedule successors of V in reverse order. */
    protected boolean reverseSuccessors(int v) {
        return false;
    }

    /** Process the successors of vertex U.  Assumes U has been visited.  This
     *  default implementation simply processes each successor using
     *  processSuccessor. */
    protected void processSuccessors(int u) {
        for (int v : _G.successors(u)) {
            if (processSuccessor(u, v)) {
                _fringe.add(v);
            }
        }
    }

    /** Process successor V to U.  Returns true iff V is then to
     *  be added to the fringe.  By default, returns true iff V is unmarked. */
    protected boolean processSuccessor(int u, int v) {
        return !marked(v);
    }

    /** The graph being traversed. */
    private final Graph _G;
    /** The fringe. */
    protected final Queue<Integer> _fringe;

    /** boolean of marked vertices. */
    protected boolean[] marked;

    /** arraylist of marked vertices. */
    protected ArrayList<Integer> mark;

    /** Arraylist of post visited. */
    protected ArrayList<Integer> postvisit;

}
