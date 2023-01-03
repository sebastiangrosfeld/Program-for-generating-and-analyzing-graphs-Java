package org.grapher.main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import org.grapher.gui.MainScene;
import org.grapher.gui.PathsScene;
import org.grapher.logic.Graph;

public class Main extends Application {
    public static final double WINDOW_WIDTH = 1280;
    public static final double WINDOW_HEIGHT = 768;
    private static final Graph graph = new Graph();
    private static final String WINDOW_TITLE = "Grapher";
    private static Stage stage;
    private static MainScene mainScene;
    private static PathsScene pathsScene;

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToMainScene() {
        stage.setScene(mainScene);
    }

    public static void switchToPathsScene() {
        pathsScene.initialize();
        stage.setScene(pathsScene);
    }

    @Override
    public void start(Stage stage) {
        Main.stage = stage;

        prepareMainScene();
        preparePathsScene();
        prepareStage();
    }

    private void prepareMainScene() {
        Group root = new Group();
        mainScene = new MainScene(root, graph);
        mainScene.initialize();
    }

    private void preparePathsScene() {
        Group root = new Group();
        pathsScene = new PathsScene(root, graph);
        pathsScene.initialize();
    }

    private void prepareStage() {
        stage.setResizable(false);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(mainScene);
        stage.show();
    }
}
