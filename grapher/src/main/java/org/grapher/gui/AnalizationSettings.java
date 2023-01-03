package org.grapher.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.grapher.logic.Graph;
import org.grapher.logic.Node;
import org.grapher.main.Main;

import java.io.File;
import java.util.Objects;

public class AnalizationSettings extends Pane {
    private static final double WIDTH = 500;
    private static final double HEIGHT = 120;
    private static final double PADDING = 10;
    private final Pane container = new Pane();
    private final Graph graph;
    private final GraphView graphView;
    private TextField filenameTextField;

    public AnalizationSettings(Graph graph, GraphView graphView) {
        this.graph = graph;
        this.graphView = graphView;
    }

    public void initialize() {
        createContainer();
        createElements();
    }

    private String getFilename() {
        return filenameTextField.getText();
    }

    private void createContainer() {
        container.setPrefSize(WIDTH, HEIGHT);
        container.setPadding(new Insets(PADDING));
    }

    private void createElements() {
        VBox body = addBody();
        getChildren().add(body);
        filenameTextField = addFilenameTextFieldAndButtons(body);
    }

    private VBox addBody() {
        VBox body = new VBox();
        body.setSpacing(20);
        body.setMinWidth(520);
        body.setPadding(new Insets(20));
        body.setAlignment(Pos.CENTER);

        createTitleLabel(body);

        return body;
    }

    private void createTitleLabel(VBox body) {
        Label title = new Label();
        title.setText("ANALIZA");
        title.setFont(new Font(20));
        body.getChildren().add(title);
    }

    private TextField addFilenameTextFieldAndButtons(VBox body) {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        body.getChildren().add(vBox);

        Label filenameLabel = new Label("Nazwa pliku wejściowego");
        filenameLabel.setFont(new Font(16));
        vBox.getChildren().add(filenameLabel);

        TextField filename = new TextField();
        vBox.getChildren().add(filename);

        HBox buttons = new HBox();
        body.getChildren().add(buttons);
        addButtons(buttons);

        return filename;
    }

    private void addButtons(HBox buttons) {
        buttons.setSpacing(100);
        buttons.setAlignment(Pos.CENTER);
        Button paths = new Button("Ścieżki");
        paths.setOnAction(showPathsEventHandler());
        buttons.getChildren().add(paths);
        Button analyze = new Button("Analizuj");
        analyze.setOnAction(analyzeFileEventHandler());
        buttons.getChildren().add(analyze);
    }

    private EventHandler<ActionEvent> showPathsEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isGraphGenerated()) {
                    Main.switchToPathsScene();
                } else {
                    ErrorAlert.showError("Brak wygenerowanego grafu", "Graf nie został wygenerowany.");
                }
            }
        };
    }

    private EventHandler<ActionEvent> analyzeFileEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String filename = getFilename().trim();

                if (filename.isEmpty()) {
                    if (isGraphGenerated()) {
                        analyzeGraph();
                    }
                } else {
                    File file = new File(getFilename());

                    if (isAnalyzable(file)) {
                        graph.readFromFile(getFilename());
                        graphView.drawGraph();
                        analyzeGraph();
                    }
                }
            }
        };
    }

    private void analyzeGraph() {
        analyzeConsistency();
        findPaths();
    }

    private void analyzeConsistency() {
        if (isGraphGenerated() && graph.checkConsistency()) {
            InformationAlert.showNotification("Graf spójny", "Wygenerowany graf jest spójny.");
        } else {
            InformationAlert.showNotification("Graf niespójny", "Wygenerowany graf nie jest spójny lub nie został wygenerowany żaden graf.");
        }
    }

    private void findPaths() {
        graph.findPaths();
        graphView.drawPaths();
    }

    private boolean isAnalyzable(File file) {
        if (Objects.equals(getFilename(), "")) {
            ErrorAlert.showError("Puste pole", "Proszę wpisać nazwę pliku.");
            return false;
        } else if (!file.exists()) {
            ErrorAlert.showError("Plik nie istnieje", "Podany plik nie istnieje.");
            return false;
        }

        return true;
    }

    private boolean isGraphGenerated() {
        Node[] adjacencyList = graph.getAdjacencyList();
        return adjacencyList != null && adjacencyList.length > 0;
    }
}
