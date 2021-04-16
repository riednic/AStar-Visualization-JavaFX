package de.riedlnico.pathfinding;

import de.riedlnico.graph.CellState;
import de.riedlnico.graph.GridCell;

public class GridEvaluator implements Evaluator<GridCell> {

    /**
     * This method calculates the "Manhattan Distance" between 2 Cells on a 2D-Grid.
     * @param from start cell to start calculation
     * @param to destination cell
     * @return the cost from the start to the destination cell.
     * @author Nico Riedl.
     */
    @Override
    public double computeCost(GridCell from, GridCell to) {
        if(to.getState() != CellState.OBSTACLE) {
            return Math.abs(from.getPosition().getX() - to.getPosition().getX()) +
                    Math.abs(from.getPosition().getY() - to.getPosition().getY());
        }
        return Double.POSITIVE_INFINITY;
    }
}
