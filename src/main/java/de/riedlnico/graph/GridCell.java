package de.riedlnico.graph;

import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GridCell extends Pane implements GraphNode {

    /**
     * @attribute position represents the current Position on the 2D-Grid.
     * @param color represents the current Color of the Cell.
     * @param state represents the current State of the Cell.
     * Notice: Since the Cell represents its state through the Cell-Color, the
     *         Cell-State and the Cell-Color have to match each other.
     *
     * STATE:               COLOR:
     * START                RED
     * END                  GREEN
     * EMPTY                WHITE
     * OBSTACLE             BLACK
     * ROUTE                ORANGE
     */
    private final Point2D position;
    private Color color;
    private CellState state;

    public GridCell(final String id, final double xPosition, final double yPosition) {
        this(id, xPosition, yPosition, CellState.EMPTY);
    }

    private GridCell(final String id, final double xPosition, final double yPosition, final CellState state) {
        this.setId(id);
        this.position = new Point2D(xPosition, yPosition);
        this.state = state;
        this.changeState(this.state);
        Border b = new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        this.setBorder(b);
    }

    /**
     * This method changes the state of the current GridCell.
     * The state of a Cell is represented through the state attribute and corresponding color.
     * @param state is the new state the Cell should change to.
     * @author Nico Riedl.
     */
    public void changeState(CellState state) {
        switch (state) {
            case START: this.state = state;
                        this.color = Color.RED;
                        break;

            case END:   this.state = state;
                        this.color = Color.GREEN;
                        break;

            case EMPTY: this.state = state;
                        this.color = Color.WHITE;
                        break;

            case OBSTACLE: this.state = state;
                        this.color = Color.BLACK;
                        break;

            case ROUTE: this.state = state;
                        this.color = Color.ORANGE;
                        break;
        }
        BackgroundFill bf = new BackgroundFill(this.color, null, null);
        Background bg = new Background(bf);
        this.setBackground(bg);
    }

    @Override
    public String getNodeId() {
        return this.getId();
    }

    public CellState getState() {
        return this.state;
    }

    public Point2D getPosition() {
        return position;
    }
}
