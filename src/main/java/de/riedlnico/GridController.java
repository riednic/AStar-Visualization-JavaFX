package de.riedlnico;

import de.riedlnico.graph.CellState;
import de.riedlnico.graph.Graph;
import de.riedlnico.graph.GridCell;
import de.riedlnico.pathfinding.GridEvaluator;
import de.riedlnico.pathfinding.RouteFinder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.*;

public class GridController {

    @FXML
    private GridPane gridPane;
    private Stage stage;
    private Set<GridCell> nodes;
    private Map<String, Set<String>> connections;
    private Graph<GridCell> graph;
    private List<GridCell> route;
    private GridCell start;
    private GridCell end;
    private CellState currentState;
    private double xOffset;
    private double yOffset;
    private static final int COLS = 40;
    private static final int ROWS = 40;
    private static final int SIZE = ROWS*COLS;

    protected void setStageAndInitializeGrid(Stage stage) {
        this.stage = stage;
        this.currentState = CellState.START;
        this.route = null;
        this.initializeNodes();
        this.initializeConnections();
        this.graph = new Graph<>(this.nodes, this.connections);
    }

    /**
     * Initialize all the GridCells and add them to the nodes Set
     * and also add them to the GridPane that represents our Grid.
     * @author Nico Riedl.
     */
    private void initializeNodes() {
        this.nodes = new HashSet<>();
        this.connections = new HashMap<>();
        int k = 0;
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GridCell c = new GridCell(Integer.toString(k), j, i);
                nodes.add(c);
                gridPane.add(c, j, i);
                k++;
            }
        }
    }

    /**
     * Initialize all connections between Nodes and store them
     * in the connections Map.
     * @author Nico Riedl.
     */
    private void initializeConnections() {
        int k = 0;
        for(int m = 0; m < ROWS; m++) {
            for (int n = 0; n < COLS; n++) {
                Set<String> con = new HashSet<>();
                if(n + 1 < COLS) {
                    con.add(Integer.toString(k + 1));
                }
                if(n - 1 >= 0) {
                    con.add(Integer.toString(k - 1));
                }
                if(m + 1 < ROWS) {
                    con.add(Integer.toString(k + ROWS));
                }
                if(m - 1 >= 0) {
                    con.add(Integer.toString(k - ROWS));
                }
                connections.put(Integer.toString(k), con);
                k++;
            }
        }
    }

    /**
     * This method checks if a CellState is already set on the Grid.
     * Note: We only want one Start and one Destination Cell to be set.
     * @param state is the state to check for if it is already set.
     * @return true if the state is set
     *         false if the state is not set(Obstacles can be set multiple times).
     * @author Nico Riedl.
     */
    private boolean checkDuplicateCellState(CellState state) {
        GridCell c;
         for(int i = 0; i < SIZE; i++) {
             c = this.graph.getNode(Integer.toString(i));
             if(c.getState() == state && state != CellState.OBSTACLE) {
                 return true;
             }
         }
         return false;
    }

    /**
     * This method resets all Obstacles on the Grid.
     * Note: Start and Destination Cell's are not affected.
     * @author Nico Riedl.
     */
    private void resetObstacles() {
        GridCell c;
        this.route = null;
        for(int i = 0; i < SIZE; i++) {
            c = this.graph.getNode(Integer.toString(i));
            if(c.getState() != CellState.START && c.getState() != CellState.END) {
                c.changeState(CellState.EMPTY);
            }
        }
    }

    /**
     * This method handles the AStar pathfinder event.
     * Note: We Need to check if Start and End Cell are set, otherwise
     * we cant search for a Route!
     * @author Nico Riedl.
     */
    @FXML
    private void pathfinderEvent() {
        if(this.start != null && this.end != null && this.route == null) {
            RouteFinder<GridCell> finder = new RouteFinder<>(this.graph, new GridEvaluator(), new GridEvaluator());
            try {
                this.route = finder.findRoute(start, end);
                for (GridCell c : this.route) {
                    if(c.getState() != CellState.START && c.getState() != CellState.END) {
                        c.changeState(CellState.ROUTE);
                    }
                }
            } catch (IllegalStateException e) {
                System.out.println(e);
            }
        } else {
            this.resetGrid();
        }
    }

    /**
     * This method handles MouseEvents on a specific GridCell.
     * @param event the event that happened.
     * Note: We only want to have one Cell in the Start state
     *       and one Cell in the Destination state so
     *       we have to check for this.
     * @author Nico Riedl.
     */
    @FXML
    private void gridCellEvent(MouseEvent event) {
        Node source = (Node) event.getTarget();
        String id = source.getId();
        try {
            GridCell c = this.graph.getNode(id);
            if (event.getButton() == MouseButton.PRIMARY && !checkDuplicateCellState(this.currentState)) {
                if(this.currentState == CellState.START) {
                    this.start = c;
                } else if(this.currentState == CellState.END) {
                    this.end = c;
                }
                if(c.getState() == CellState.EMPTY) {
                    c.changeState(this.currentState);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                c.changeState(CellState.EMPTY);
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    /**
     * This method generates some obstacles on the grid.
     * Note: Start and Destination Cell are not affected!
     * @author Nico Riedl.
     */
    @FXML
    private void generateObstacles() {
        this.resetObstacles();
        Random r = new Random();
        GridCell c;
        int number;
        for(int i = 0; i < SIZE; i++) {
            number = r.nextInt(20);
           c = this.graph.getNode(Integer.toString(i));
           if(c.getState() != CellState.START && c.getState() != CellState.END && number < 5) {
               c.changeState(CellState.OBSTACLE);
           }
        }
    }

    /**
     * This method resets the whole Grid
     * Note: Every Cell state is affected!
     * @author Nico Riedl.
     */
    @FXML
    private void resetGrid() {
        GridCell c;
        this.route = null;
        this.start = null;
        this.end = null;
        for(int i = 0; i < SIZE; i++) {
            c = this.graph.getNode(Integer.toString(i));
            c.changeState(CellState.EMPTY);
        }
    }

    @FXML
    private void setStartColor() {
        this.currentState = CellState.START;
    }

    @FXML
    private void setEndColor() {
        this.currentState = CellState.END;
    }

    @FXML
    private void setObstacleColor() {
        this.currentState = CellState.OBSTACLE;
    }

    @FXML
    private void handleCloseEvent() {
        this.stage.close();
    }

    @FXML
    private void moveWindow(MouseEvent event) {
        Node source = (Node) event.getTarget();
        if(!(source instanceof Cell)) {
            stage.setX(event.getScreenX() - this.xOffset);
            stage.setY(event.getScreenY() - this.yOffset);
        }
    }

    @FXML
    private void getOffset(MouseEvent event) {
        this.xOffset = event.getSceneX();
        this.yOffset = event.getSceneY();
    }

}
