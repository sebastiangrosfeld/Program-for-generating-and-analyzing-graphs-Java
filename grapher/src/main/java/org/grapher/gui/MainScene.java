package org.grapher.gui;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.grapher.logic.Graph;

public class MainScene extends Scene {
    private final Graph graph;
    private GraphView graphView;
    private GenerationSettings generationSettings;
    private AnalizationSettings analizationSettings;

    public MainScene(Parent root, Graph graph) {
        super(root);

        this.graph = graph;
    }

    public void initialize() {
        prepareGraphView();
        prepareGenerationSettings();
        prepareAnalizationSettings();
        fuzeComponents((Group) getRoot());
        setFill(Color.LIGHTGRAY);
    }

    private void prepareGraphView() {
        graphView = new GraphView(graph);
        graphView.initialize();
    }

    private void prepareGenerationSettings() {
        generationSettings = new GenerationSettings(graph, graphView);
        generationSettings.initialize();
    }

    private void prepareAnalizationSettings() {
        analizationSettings = new AnalizationSettings(graph, graphView);
        analizationSettings.initialize();
    }

    private void fuzeComponents(Group root) {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        root.getChildren().add(hBox);
        hBox.getChildren().add(graphView);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        hBox.getChildren().add(vBox);
        vBox.getChildren().add(generationSettings);
        vBox.getChildren().add(analizationSettings);
    }
}
