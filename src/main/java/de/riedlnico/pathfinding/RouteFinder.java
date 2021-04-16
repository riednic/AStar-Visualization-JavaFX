package de.riedlnico.pathfinding;

import de.riedlnico.graph.Graph;
import de.riedlnico.graph.GraphNode;

import java.util.*;

public class RouteFinder<T extends GraphNode> {

    private final Graph<T> graph;
    private final Evaluator<T> nextNodeEvaluator;
    private final Evaluator<T> targetEvaluator;

    public RouteFinder(Graph<T> graph, Evaluator<T> nextNodeEvaluator, Evaluator<T> targetEvaluator) {
        this.graph = graph;
        this.nextNodeEvaluator = nextNodeEvaluator;
        this.targetEvaluator = targetEvaluator;
    }

    /**
     * This method finds the shortest route between to Cells.
     * @param from start Node.
     * @param to destination Node.
     * @return a List that contains the shortest Path from the
     *         start to the destination Cell.
     * @throws IllegalStateException if no Route was found.
     * @author Nico Riedl.
     */
    public List<T> findRoute (T from, T to) throws IllegalStateException {
        /**
         * 1. Initialize the openSet as a PriorityQueue
         *    The OpenSet contains all RouteNodes that have not been checked.
         *    In the first Position of the openSet is always the RouteNode with the
         *    least expensive estimatedScore(Based on our compareTo Method in RouteNode).
         * 2. Initialize a Map that contains all Nodes and their given RouteNode.
         */
        Queue<RouteNode<T>> openSet = new PriorityQueue<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        /**
         * 3. Create a new RouteNode based on the "from" Node and initialize it
         *    with the current Node(from) and the current cost(which is 0)
         *    and the estimated cost from the start to the destination.
         * 4. Add the Node to the openSet
         * 5. Add the Node "from" and the corresponding RouteNode "start" to the Map.
         */
        RouteNode<T> start = new RouteNode<>(from, null, 0d, targetEvaluator.computeCost(from, to));
        openSet.add(start);
        allNodes.put(from, start);

        /**
         * We only go into this Part if we still have Nodes to be checked left
         */
        while(!openSet.isEmpty()) {
            /**
             * Get the RouteNode with the lowest cost.
             * If the RouteNode is our destination we are done.
             */
            RouteNode<T> next = openSet.poll();
            if(next.getCurrent().equals(to)) {
                /**
                 * Now we need to find the Path since we are at the destination.
                 * So we add the current Cell to our List and then
                 * go to the Previous RouteNode.
                 * We do this until the Previous RouteNode is null.
                 */
                List<T> route = new ArrayList<>();
                RouteNode<T> current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPrevious());
                } while(current != null);
                return route;
            }
            /**
             * We have to get all Cells that are connected to our
             * current Cell
             */
            Set<T> con = graph.getConnections(next.getCurrent());
            for(T cell: con) {

                /**
                 * We calculate the Score for every connection our Cell has.
                 * We create a new RouteNode and add it to the Map.
                 */
                double newScore = next.getRouteScore() + nextNodeEvaluator.computeCost(next.getCurrent(), cell);
                RouteNode<T> nextNode = allNodes.getOrDefault(cell, new RouteNode<>(cell));
                allNodes.put(cell, nextNode);

                /**
                 * Now we want to find the best Score and add it to the path.
                 * If our newScore is better than the previous ones,
                 * we add the current cell as the previous Cell to our new RouteNode.
                 * We also set the new Score and the new estimated Score from the new
                 * RouteNode to our target.
                 */
                if(newScore < nextNode.getRouteScore()) {
                    nextNode.setPrevious(next.getCurrent());
                    nextNode.setRouteScore(newScore);
                    nextNode.setEstimatedScore(newScore + targetEvaluator.computeCost(cell, to));
                    openSet.add(nextNode);
                }
            }
        }
        /**
         * We throw a Exception if we do not find a Route
         */
        throw new IllegalStateException("No route found");
    }
}
