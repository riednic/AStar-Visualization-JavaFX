package de.riedlnico.pathfinding;

import de.riedlnico.graph.GraphNode;

public interface Evaluator<T extends GraphNode> {
    double computeCost(T from, T to);
}
