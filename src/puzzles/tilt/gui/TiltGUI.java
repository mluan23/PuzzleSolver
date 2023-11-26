package puzzles.tilt.gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    private final int SIZE = 500;
    public GridPane makeGridPane() {
        GridPane gridPane = new GridPane();
        buttons = new Button[dimensions][dimensions];
        for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < dimensions; col++) {
                int r = row;
                int c = col;
                Button button = new Button();
                button.setPrefWidth((double) SIZE /dimensions);
                button.setPrefHeight((double) SIZE /dimensions);
                buttons[r][c] = button;
                gridPane.add(button, col, row);
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
        this.model = new TiltModel();
        current = loadFile(filename);
        dimensions = current.getDimensions();
        board = new char[dimensions][dimensions];
        model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane outer = new BorderPane();
        BorderPane inner = new BorderPane();
        HBox upBox = new HBox();
        HBox downBox = new HBox();
        HBox rightBox = new HBox();
        HBox leftBox = new HBox();
        upBox.setAlignment(Pos.CENTER);
        downBox.setAlignment(Pos.CENTER);
        rightBox.setAlignment(Pos.CENTER);
        leftBox.setAlignment(Pos.CENTER);
        GridPane center = makeGridPane();
        center.setAlignment(Pos.CENTER);
        Button upArrow = new Button("^");
        upArrow.setAlignment(Pos.CENTER);
        upArrow.setPrefWidth(SIZE*.8);
        Button downArrow = new Button("V");
        downArrow.setAlignment(Pos.CENTER);
        downArrow.setPrefWidth(SIZE*.8);
        Button rightArrow = new Button(">");
        rightArrow.setAlignment(Pos.CENTER);
        rightArrow.setPrefHeight(SIZE*.78);
        Button leftArrow = new Button("<");
        leftArrow.setAlignment(Pos.CENTER);
        leftArrow.setPrefHeight(SIZE*.78);
        HBox top = new HBox();
        text = new Text("Test");
        top.getChildren().add(text);
        top.setAlignment(Pos.CENTER);
        HBox right = new HBox();
        VBox threeButtons = new VBox();
        threeButtons.setAlignment(Pos.CENTER);
        threeButtons.setSpacing(20);
        Button load = new Button("Load");
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");
        threeButtons.getChildren().addAll(load, reset, hint);
        right.getChildren().addAll(rightArrow, threeButtons);
        upBox.getChildren().add(upArrow);
        downBox.getChildren().add(downArrow);
        rightBox.getChildren().add(rightArrow);
        leftBox.getChildren().add(leftArrow);
        inner.setTop(upBox);
        inner.setCenter(center);
        inner.setRight(rightBox);
        inner.setLeft(leftBox);
        inner.setBottom(downBox);
        outer.setTop(top);
        outer.setCenter(inner);
        outer.setRight(threeButtons);
//        Button button = new Button();
//        button.setGraphic(new ImageView(greenDisk));
        Scene scene = new Scene(outer);
        stage.setScene(scene);
        //setBoard();
        stage.setWidth(SIZE);
        stage.setHeight(SIZE);
        stage.setResizable(false);
        stage.setTitle("Tilt GUI");
        stage.show();
    }
    private void setBoard(){
        for(int rows = 0; rows<dimensions; rows++){
            for (int cols = 0; cols<dimensions; cols++){
                if(current.getBoard()[rows][cols] == 'G'){
                    buttons[rows][cols].setGraphic(new ImageView(greenDisk));
                }
                else if(current.getBoard()[rows][cols] == 'B'){
                    buttons[rows][cols].setGraphic(new ImageView(blueDisk));
                }
                else if (current.getBoard()[rows][cols] == '*'){
                    buttons[rows][cols].setGraphic(new ImageView(blueDisk));
                }

            }
        }
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
