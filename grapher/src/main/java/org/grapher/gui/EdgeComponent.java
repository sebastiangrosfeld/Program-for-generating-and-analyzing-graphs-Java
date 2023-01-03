package org.grapher.gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class EdgeComponent extends Group {
    private static final double ARROWHEAD_ANGLE = 45;
    private static final double ARROWHEAD_OFFSET = 15;
    private static final double THICKNESS = 5;
    private final double wage;
    private final Label label = new Label();
    private final Rectangle edge = new Rectangle();
    private Color color = Color.WHITE;
    private Rectangle leftArrowheadPart;
    private Rectangle rightArrowheadPart;

    public EdgeComponent(double wage) {
        this.wage = wage;
    }

    public Label getLabel() {
        return label;
    }

    public void setColor(Color color) {
        this.color = color;
        edge.setFill(color);
        leftArrowheadPart.setFill(color);
        rightArrowheadPart.setFill(color);
        label.setTextFill(color);
    }

    public void create() {
        createArrow();
        createLabel();
        applyCenteringOffset();
        setMouseTransparent(true);
    }

    public void showWage() {
        label.setOpacity(1);
    }

    private void createArrow() {
        createEdge();
        createArrowhead();
    }

    private void createEdge() {
        edge.setWidth(GraphView.GAP_BETWEEN_NODES);
        edge.setHeight(THICKNESS);
        edge.setFill(color);
        getChildren().add(edge);
    }

    private void createArrowhead() {
        createLeftArrowheadPart();
        createRightArrowheadPart();
    }

    private void createLeftArrowheadPart() {
        leftArrowheadPart = createArrowheadPart(270 - ARROWHEAD_ANGLE, -THICKNESS);
    }

    private void createRightArrowheadPart() {
        rightArrowheadPart = createArrowheadPart(90 + ARROWHEAD_ANGLE, THICKNESS);
    }

    private Rectangle createArrowheadPart(double rotation, double yOffset) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(NodeComponent.NODE_RADIUS);
        rectangle.setHeight(THICKNESS);
        rectangle.setFill(color);
        rectangle.setRotate(rotation);
        rectangle.setX(GraphView.GAP_BETWEEN_NODES - (NodeComponent.NODE_RADIUS + ARROWHEAD_OFFSET));
        rectangle.setY(yOffset);
        rectangle.setArcWidth(THICKNESS);
        rectangle.setArcHeight(THICKNESS);
        getChildren().add(rectangle);
        return rectangle;
    }

    private void applyCenteringOffset() {
        setLayoutY(-THICKNESS / 2);
    }

    private void createLabel() {
        label.setText(getFormattedWage());
        label.setTextFill(color);
        label.setPrefWidth(GraphView.GAP_BETWEEN_NODES);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(10));
        label.setTranslateY(-NodeComponent.NODE_RADIUS);
        label.setOpacity(0);
        getChildren().add(label);
    }

    private String getFormattedWage() {
        if (wage < 0.001) {
            return roundWage("0.##E0");
        }

        if (wage >= 1000) {
            return roundWage("0.##E0");
        }

        return roundWage("#.##");
    }

    private String roundWage(String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(wage);
    }
}
