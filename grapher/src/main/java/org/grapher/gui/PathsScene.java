package org.grapher.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.grapher.logic.Graph;
import org.grapher.logic.Path;
import org.grapher.main.Main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.grapher.main.Main.WINDOW_HEIGHT;
import static org.grapher.main.Main.WINDOW_WIDTH;

public class PathsScene extends Scene {
    public static final String ERROR_TITLE = "Błędna konfiguracja ścieżek";
    public static final String PATH_INVALID_ERROR = "Pola wierzchołków startowych i końcowych nie mogą być puste oraz wartość start i koniec w jednym wierszu nie może być taka sama.";
    private static final String SCENE_TITLE = "szukanie najkrótszych ścieżek";
    private static final String[] COLUMN_NAMES = {"Pokazuj ścieżkę", "Oznaczenie", "Pokazuj wagi", "Start", "Koniec", "Usuń"};
    private static final String NODE_INDEX_OUT_OF_BOUNDS_ERROR = "Numery wierzchołków muszą zawierać się w przedziale < 0 ; ";
    private static final String WRONG_NODE_INDEX_FORMAT_ERROR = "Numery wierzchołków muszą być liczbami całkowitymi.";
    private final Graph graph;
    private BorderPane container;
    private GridPane grid;

    public PathsScene(Parent root, Graph graph) {
        super(root);

        this.graph = graph;
    }

    public void initialize() {
        createContainer();
        createTitle();
        createPathsList();
        createButtons();
    }

    private void createContainer() {
        Group root = (Group) getRoot();
        container = new BorderPane();
        container.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        container.setBackground(createContainerBackground());
        container.setPadding(new Insets(20, 20, 100, 20));
        root.getChildren().add(container);
    }

    private Background createContainerBackground() {
        return new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
    }

    private void createTitle() {
        Label label = new Label(SCENE_TITLE.toUpperCase());
        label.setPrefWidth(WINDOW_WIDTH);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font(12));
        container.setTop(label);
    }

    private void createButtons() {
        HBox buttonsContainer = createButtonsContainer();
        createAddPathButton(buttonsContainer);
        createSaveButton(buttonsContainer);
    }

    private HBox createButtonsContainer() {
        HBox buttonsContainer = new HBox();
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setSpacing(20);
        container.setBottom(buttonsContainer);

        return buttonsContainer;
    }

    private void createAddPathButton(HBox buttonsContainer) {
        Button button = new Button("Dodaj ścieżkę");
        button.setOnMouseClicked(addPathEventHandler());
        buttonsContainer.getChildren().add(button);
    }

    private EventHandler<MouseEvent> addPathEventHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Path path = new Path(calculateNewId());
                graph.getPaths().add(path);
                createPathsList();
            }

            private int calculateNewId() {
                ArrayList<Path> paths = graph.getPaths();
                int pathsSize = paths.size();

                if (pathsSize == 0) {
                    return 1;
                }

                return paths.get(pathsSize - 1).getId() + 1;
            }
        };
    }

    private void createSaveButton(HBox buttonsContainer) {
        Button button = new Button("Zapisz");
        button.setOnMouseClicked(saveEventHandler());
        buttonsContainer.getChildren().add(button);
    }

    private EventHandler<MouseEvent> saveEventHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (pathsAreValid()) {
                    Main.switchToMainScene();
                }
            }
        };
    }

    private boolean pathsAreValid() {
        for (Path path : graph.getPaths()) {
            if (!path.isValid()) {
                ErrorAlert.showError(ERROR_TITLE, PATH_INVALID_ERROR);
                return false;
            }
        }

        return true;
    }

    private void createPathsList() {
        createPathsListGrid();
        ScrollPane scrollPane = createScrollPane();
        setGridWidthToFitScrollPane(scrollPane);
        createPathsListRows();
    }

    private void createPathsListGrid() {
        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setAlignment(Pos.CENTER);

        createColumns();
    }

    private void createColumns() {
        for (int columnIndex = 0; columnIndex < COLUMN_NAMES.length; columnIndex++) {
            createColumn(COLUMN_NAMES[columnIndex], COLUMN_NAMES.length, columnIndex);
        }
    }

    private void createColumn(String name, int columnsCount, int columnIndex) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.0 / columnsCount);
        columnConstraints.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().add(columnConstraints);
        grid.add(createColumnLabel(name), columnIndex, 0);
    }

    private Label createColumnLabel(String name) {
        Label label = new Label(name);
        label.setFont(new Font(18));

        return label;
    }

    private ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(20, 0, 20, 0));
        scrollPane.setContent(grid);
        container.setCenter(scrollPane);

        return scrollPane;
    }

    private void setGridWidthToFitScrollPane(ScrollPane scrollPane) {
        grid.prefWidthProperty().bind(scrollPane.widthProperty());
    }

    private void createPathsListRows() {
        ArrayList<Path> paths = graph.getPaths();

        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.get(i);
            int rowIndex = i + 1;

            if (path != null) {
                createPathsListRow(path, rowIndex);
            }
        }
    }

    private void createPathsListRow(Path path, int rowIndex) {
        CheckBox displayPathCheckBox = createDisplayPathCheckBox(path);
        grid.add(displayPathCheckBox, 0, rowIndex);

        Label label = createPathLabel(path);
        grid.add(label, 1, rowIndex);

        CheckBox showWagesCheckBox = createShowWagesCheckBox(path);
        grid.add(showWagesCheckBox, 2, rowIndex);

        TextField sourceTextField = createSourceTextField(path);
        grid.add(sourceTextField, 3, rowIndex);

        TextField destinationTextField = createDestinationTextField(path);
        grid.add(destinationTextField, 4, rowIndex);

        Button removePathButton = createRemovePathButton(path);
        grid.add(removePathButton, 5, rowIndex);
    }

    private CheckBox createDisplayPathCheckBox(final Path path) {
        CheckBox displayPathCheckBox = new CheckBox();
        displayPathCheckBox.setSelected(path.isDisplay());
        displayPathCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                path.setDisplay(newValue);
            }
        });

        return displayPathCheckBox;
    }

    private Label createPathLabel(Path path) {
        Label label = new Label("S" + path.getId());
        label.setFont(new Font(16));

        return label;
    }

    private CheckBox createShowWagesCheckBox(final Path path) {
        CheckBox showWagesCheckBox = new CheckBox();
        showWagesCheckBox.setSelected(path.isShowWages());
        showWagesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                path.setShowWages(newValue);
            }
        });

        return showWagesCheckBox;
    }

    private TextField createSourceTextField(Path path) {
        TextField sourceTextField;

        try {
            sourceTextField = createTextField(path, path.getSource(), Path.class.getMethod("setSource", Integer.class));
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }

        return sourceTextField;
    }

    private TextField createDestinationTextField(Path path) {
        TextField destinationTextField;

        try {
            destinationTextField = createTextField(path, path.getDestination(), Path.class.getMethod("setDestination", Integer.class));
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }

        return destinationTextField;
    }

    private TextField createTextField(final Path path, Integer nodeIndex, final Method setNodeIndex) {
        final TextField textField = new TextField(generateTextFromNodeIndex(nodeIndex));
        textField.setFont(new Font(16));
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                try {
                    int parsedValue = Integer.parseInt(newValue);

                    if (valueIsValid(parsedValue)) {
                        setNodeIndex.invoke(path, parsedValue);
                    } else {
                        handleError(oldValue, NODE_INDEX_OUT_OF_BOUNDS_ERROR + (graph.getNodesCount() - 1) + " >.");
                    }
                } catch (NumberFormatException numberFormatException) {
                    if (newValue.isEmpty()) {
                        removeNodeIndex();
                        return;
                    }

                    handleError(oldValue, WRONG_NODE_INDEX_FORMAT_ERROR);
                } catch (InvocationTargetException | IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            }

            private boolean valueIsValid(int value) {
                return value >= 0 && value < graph.getNodesCount();
            }

            private void removeNodeIndex() {
                try {
                    setNodeIndex.invoke(path, (Object) null);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    throw new RuntimeException(exception);
                }
            }

            private void handleError(String oldValue, String message) {
                ErrorAlert.showError(ERROR_TITLE, message);
                textField.setText(oldValue);
            }
        });

        return textField;
    }

    private String generateTextFromNodeIndex(Integer nodeIndex) {
        return nodeIndex != null ? String.valueOf(nodeIndex) : "";
    }

    private Button createRemovePathButton(Path path) {
        Button removePathButton = new Button("Usuń");
        removePathButton.setOnMouseClicked(removePathEventHandler(path));

        return removePathButton;
    }

    private EventHandler<MouseEvent> removePathEventHandler(final Path path) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                removePath(path);
                createPathsList();
            }

            private void removePath(Path path) {
                graph.getPaths().remove(path);
            }
        };
    }
}
