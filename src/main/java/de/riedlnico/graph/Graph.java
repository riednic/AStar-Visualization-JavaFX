package de.riedlnico.graph;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<T extends GraphNode> {

    private final Set<T> nodes;
    private final Map<String, Set<String>> connections;

    public Graph(Set<T> nodes, Map<String, Set<String>> connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

    public T getNode(String id) throws IllegalArgumentException{
        return nodes.stream()
                .filter(node -> node.getNodeId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No node found with id"));
    }

    public Set<T> getConnections(T node) {
        return connections.get(node.getNodeId()).stream()
                .map(this::getNode)
                .collect(Collectors.toSet());
    }

}
