package puzzles.tilt.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import puzzles.tilt.solver.Tilt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    private Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    private Image blocker = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    private Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));
    private Button[][] buttons;
    private char[][] board;
    private TiltModel model;
    private TiltConfig current;
    private Text text;
    private int dimensions;
    public GridPane makeGridPane() {
        GridPane gridPane = new GridPane();

        buttons = new Button[dimensions][dimensions];
        for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < dimensions; col++) {
                int r = row;
                int c = col;
                Button button = new Button();
                button.setPrefWidth(100);
                button.setPrefHeight(100);
                buttons[r][c] = button;
            }
        }
        return gridPane;
    }
    public TiltConfig loadFile(String file) throws IOException {
        model.setCurrentConfig(model.loadBoardFile(file));

        return model.getCurrentConfig();
    }

    public void init() throws IOException{
        String filename = getParameters().getRaw().get(0);
        current = loadFile(filename);
        dimensions = current.getDimensions();
        this.model = new TiltModel();
        model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();
        GridPane center = new GridPane();
        VBox top = new VBox();
        Button up = new Button();
        Button down = new Button();
        Button rightButton = new Button();
        Button left = new Button();
        text = new Text("Test");
        HBox right = new HBox();
        VBox threeButtons = new VBox();
        Button load = new Button("Load");
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");
        threeButtons.getChildren().addAll(load, reset, hint);
        top.getChildren().addAll(text, up);
        right.getChildren().addAll(rightButton, threeButtons);
        borderPane.getChildren().addAll(top,center,down,right,left);
//        Button button = new Button();
//        button.setGraphic(new ImageView(greenDisk));
        Scene scene = new Scene(borderPane);
        stage.setTitle("Tilt GUI");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
