package de.riedlnico.pathfinding;

import de.riedlnico.graph.GraphNode;

public class RouteNode<T extends GraphNode> implements Comparable<RouteNode> {

    private final T current;
    private T previous;
    private double routeScore;
    private double estimatedScore;

    public RouteNode(T current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public RouteNode(T current, T previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    /**
     * Compares 2 RouteNodes.
     * Important to sort a List of RouteNodes
     * @param o the other RouteNode
     * @return -1 if the current estimatedScore is lower than the other.
     *          0 if both RouteNodes have the same cost.
     *          1 if the current estimatedScore is higher than the other.
     * @author Nico Riedl.
     */
    @Override
    public int compareTo(RouteNode o) {
        return Double.compare(this.estimatedScore, o.estimatedScore);
    }

    public T getCurrent() {
        return current;
    }

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public void setEstimatedScore(double estimatedScore) {
        this.estimatedScore = estimatedScore;
    }
}
