package org.grapher.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.grapher.logic.Graph;

public class GenerationSettings extends Pane {
    private static final double WIDTH = 500;
    private static final double HEIGHT = 580;
    private static final double PADDING = 10;
    private static final String ERROR_TITLE = "Nieprawidłowe dane";
    private static final String GENERATE_WAGES_MODE = "Wszystkie krawędzie";
    private static final String GENERATE_CONSISTENT_MODE = "Spójny";
    private static final String GENERATE_RANDOM_MODE = "Pełna losowość";
    private final VBox container = new VBox();
    private final Graph graph;
    private final GraphView graphView;
    private TextField filenameTextField;
    private ToggleGroup generationModeToggleGroup;
    private TextField rowsTextField;
    private TextField columnsTextField;
    private TextField minWageTextField;
    private TextField maxWageTextField;

    public GenerationSettings(Graph graph, GraphView graphView) {
        this.graph = graph;
        this.graphView = graphView;
    }

    public void initialize() {
        createContainer();
        createElements();
    }

    private void resetGenerationSettings() {
        container.getChildren().clear();
        initialize();
    }

    private String getFilename() {
        return filenameTextField.getText();
    }

    private Integer getRowsCount() {
        try {
            return Integer.parseInt(rowsTextField.getText());
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    private Integer getColumnsCount() {
        try {
            return Integer.parseInt(columnsTextField.getText());
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    private Double getMinWage() {
        try {
            return Double.parseDouble(minWageTextField.getText());
        } catch (NumberFormatException e){
            return null;
        }
    }

    private Double getMaxWage() {
        try {
            return Double.parseDouble(maxWageTextField.getText());
        } catch (NumberFormatException e){
            return null;
        }
    }

    private void createElements() {
        VBox body = createBody();
        getChildren().add(body);

        createFilenameTextField(body);
        createGenerationModeToggleGroup(body);
        createVariablesPanel(body);
    }

    private void createContainer() {
        container.setPrefSize(WIDTH, HEIGHT);
        container.setPadding(new Insets(PADDING));
    }

    private VBox createBody() {
        VBox body = new VBox();
        body.setSpacing(20);
        body.setPrefWidth(520);
        body.setPadding(new Insets(20));
        body.setAlignment(Pos.CENTER);

        createTitleLabel(body);

        return body;
    }

    private void createTitleLabel(VBox body) {
        Label title = new Label();
        title.setText("GENEROWANIE");
        title.setFont(new Font(20));
        body.getChildren().add(title);
    }

    private void createFilenameTextField(VBox body) {
        VBox vBox = new VBox();
        vBox.setSpacing(7);
        vBox.setAlignment(Pos.CENTER_LEFT);
        body.getChildren().add(vBox);

        createFilenameLabel(vBox);

        filenameTextField = new TextField();
        vBox.getChildren().add(filenameTextField);
    }

    private void createFilenameLabel(VBox field) {
        Label label = new Label("Nazwa pliku wyjściowego");
        field.getChildren().add(label);
    }

    private void createGenerationModeToggleGroup(VBox body) {
        VBox modesContainer = new VBox();
        modesContainer.setSpacing(5);
        modesContainer.setAlignment(Pos.CENTER_LEFT);
        body.getChildren().add(modesContainer);

        createModesLabel(modesContainer);

        generationModeToggleGroup = new ToggleGroup();
        createRadioButtons(modesContainer);
    }

    private void createModesLabel(VBox modesContainer) {
        Label label = new Label("Tryb");
        label.setFont(new Font(12));
        modesContainer.getChildren().add(label);
    }

    private void createRadioButtons(VBox modesContainer) {
        RadioButton generateWagesRadioButton = new RadioButton(GENERATE_WAGES_MODE);
        generateWagesRadioButton.setSelected(true);
        generateWagesRadioButton.setToggleGroup(generationModeToggleGroup);

        RadioButton generateConsistentRadioButton = new RadioButton(GENERATE_CONSISTENT_MODE);
        generateConsistentRadioButton.setToggleGroup(generationModeToggleGroup);

        RadioButton generateRandomRadioButton = new RadioButton(GENERATE_RANDOM_MODE);
        generateRandomRadioButton.setToggleGroup(generationModeToggleGroup);

        modesContainer.getChildren().add(generateWagesRadioButton);
        modesContainer.getChildren().add(generateConsistentRadioButton);
        modesContainer.getChildren().add(generateRandomRadioButton);
    }

    private void createVariablesPanel(VBox body) {
        GridPane gridPane = createSettingsGridPane();
        body.getChildren().add(gridPane);

        createSettingsLabels(gridPane);
        createSettingsFields(gridPane);

        Button generationButton = createGenerationButton();
        body.getChildren().add(generationButton);
    }

    private GridPane createSettingsGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(30);
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    private void createSettingsLabels(GridPane gridPane) {
        Label rowsCountLabel = new Label("Liczba wierszy");
        gridPane.add(rowsCountLabel, 0, 0);

        Label columnsCountLabel = new Label("Liczba kolumn");
        gridPane.add(columnsCountLabel, 1, 0);

        Label minWageLabel = new Label("Minimalna waga");
        gridPane.add(minWageLabel, 0, 2);

        Label maxWageLabel = new Label("Maksymalna waga");
        gridPane.add(maxWageLabel, 1, 2);
    }

    private void createSettingsFields(GridPane gridPane) {
        rowsTextField = new TextField();
        gridPane.add(rowsTextField, 0, 1);

        columnsTextField = new TextField();
        gridPane.add(columnsTextField, 1, 1);

        minWageTextField = new TextField();
        gridPane.add(minWageTextField, 0, 3);

        maxWageTextField = new TextField();
        gridPane.add(maxWageTextField, 1, 3);
    }

    private Button createGenerationButton() {
        Button generationButton = new Button("Generuj");
        generationButton.setOnAction(generateGraphEventHandler());

        return generationButton;
    }

    private EventHandler<ActionEvent> generateGraphEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isValid()) {
                    generateGraph();

                    if (!getFilename().isEmpty()) {
                        saveGraphToFile();
                    }
                }
            }
        };
    }

    private void generateGraph() {
        RadioButton selectedToggle = (RadioButton) generationModeToggleGroup.getSelectedToggle();
        String mode = selectedToggle.getText();

        switch (mode) {
            case GENERATE_WAGES_MODE:
                setGraphSize();
                graph.generateWages(getMinWage(), getMaxWage());
                break;
            case GENERATE_CONSISTENT_MODE:
                setGraphSize();
                graph.generateConsistent(getMinWage(), getMaxWage());
                break;
            case GENERATE_RANDOM_MODE:
                setGraphSize();
                graph.generateRandom(getMinWage(), getMaxWage());
                break;
            default:
                ErrorAlert.showError("Nie wybrano trybu", "Proszę wybrać tryb.");
                resetGenerationSettings();
                return;
        }

        graphView.drawGraph();
    }

    private void setGraphSize() {
        graph.setRows(getRowsCount());
        graph.setColumns(getColumnsCount());
    }

    private void saveGraphToFile() {
        graph.writeToFile(getFilename());
        InformationAlert.showNotification("Graf zapisany do pliku", "Graf został zapisany do pliku " + getFilename());
    }

    private boolean isValid() {
        if (getRowsCount() == null) {
            showValidationError("Podana liczba wierszy nie jest typu Integer");
            return false;
        }

        if (getColumnsCount() == null) {
            showValidationError("Podana liczba kolumn nie jest typu Integer");
            return false;
        }

        if (getMinWage() == null) {
            showValidationError("Podana minimalna waga nie jest typu Double");
            return false;
        }

        if (getMaxWage() == null) {
            showValidationError("Podana maksymalna waga nie jest typu Double");
            return false;
        }

        if (getRowsCount() < 2) {
            showValidationError("Liczba wierszy nie może być mniejsza od 2.");
            return false;
        }

        if (getColumnsCount() < 2) {
            showValidationError("Liczba kolumn nie może być mniejsza od 2.");
            return false;
        }

        if (getMinWage() < 0) {
            showValidationError("Minimalna waga nie może być mniejsza od 0");
            return false;
        }

        if (getMaxWage() < 0) {
            showValidationError("Maksymalna waga nie może być mniejsza od 0");
            return false;
        }

        if (getMinWage() == Double.POSITIVE_INFINITY) {
            showValidationError("Zbyt duża waga minimalna.");
            return false;
        }

        if (getMinWage() == (-1.0)*Double.POSITIVE_INFINITY) {
            showValidationError("Zbyt mała waga minimalna.");
            return false;
        }

        if (getMaxWage() == Double.POSITIVE_INFINITY) {
            showValidationError("Za duża maksymalna waga.");
            return false;
        }

        if (getMaxWage() == (-1.0)*Double.POSITIVE_INFINITY) {
            showValidationError("Zbyt mała waga maksymalna.");
            return false;
        }

        if (getMinWage() > getMaxWage()) {
            showValidationError("Waga maksymalna nie może być mniejsza niż minimalna.");
            return false;
        }


        return true;
    }

    private void showValidationError(String message) {
        ErrorAlert.showError(ERROR_TITLE, message);
    }
}
