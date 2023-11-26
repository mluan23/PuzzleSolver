package puzzles.tilt.gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private static Background EMPTY = new Background(new BackgroundFill(Color.SLATEGRAY,null,null));
    private Button[][] buttons;
    private TiltModel model;
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
        model.setCurrentConfig(loadFile(filename));
        dimensions = model.getCurrentConfig().getDimensions();
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
        center.setVgap(4);
        center.setHgap(4);
        Button upArrow = new Button("^");
        upArrow.setOnAction(event -> {
            model.makeMove("north");
            setBoard();
        });
        upArrow.setAlignment(Pos.CENTER);
        upArrow.setPrefWidth(SIZE*.8);
        Button downArrow = new Button("V");
        downArrow.setOnAction(event -> {
            model.makeMove("south");
            setBoard();
        });
        downArrow.setAlignment(Pos.CENTER);
        downArrow.setPrefWidth(SIZE*.8);
        Button rightArrow = new Button(">");
        rightArrow.setOnAction(event -> {
            model.makeMove("east");
            setBoard();
        });
        rightArrow.setAlignment(Pos.CENTER);
        rightArrow.setPrefHeight(SIZE*.78);
        Button leftArrow = new Button("<");
        leftArrow.setOnAction(event -> {
            model.makeMove("west");
            setBoard();
        });
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
        reset.setOnAction(event -> {
            model.reset();
            setBoard();
        });
        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            model.getHint();
            this.setBoard();
        });
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
        Scene scene = new Scene(outer);
        stage.setScene(scene);
        setBoard();
        stage.setWidth(SIZE);
        stage.setHeight(SIZE);
        stage.setResizable(false);
        stage.setTitle("Tilt GUI");
        stage.show();
    }
    private void setBoard(){
        BackgroundSize size = new BackgroundSize((double) SIZE /dimensions, (double) SIZE /dimensions,
                true, true, true, false);
        for(int rows = 0; rows<dimensions; rows++){
            for (int cols = 0; cols<dimensions; cols++){
                if(model.getCurrentConfig().getBoard()[rows][cols] == 'G'){
                    BackgroundImage green = new BackgroundImage(greenDisk,BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
                    buttons[rows][cols].setBackground(new Background(green));
                }
                else if(model.getCurrentConfig().getBoard()[rows][cols] == 'B'){
                    BackgroundImage blue = new BackgroundImage(blueDisk,BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
                    buttons[rows][cols].setBackground(new Background(blue));
                }
                else if (model.getCurrentConfig().getBoard()[rows][cols] == '*'){
                    BackgroundImage block = new BackgroundImage(blocker,BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
                    buttons[rows][cols].setBackground(new Background(block));
                }
                else if (model.getCurrentConfig().getBoard()[rows][cols] == 'O'){
                    BackgroundImage hole = new BackgroundImage(this.hole,BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
                    buttons[rows][cols].setBackground(new Background(hole));
                }
                else{
                    buttons[rows][cols].setBackground(EMPTY);
                }

            }
        }
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
        if(message.startsWith(TiltModel.LOADED)){
            this.text.setText(message);
        }
        else if (message.startsWith(TiltModel.HINT_PREFIX)){
            text.setText("Next step!");
        }
        else if (message.startsWith(TiltModel.NO_SOLUTION)){
            text.setText("No solution!");
        }
        else if (message.equals(TiltModel.ILLEGAL)){
            text.setText(message);
        }
        else if (message.equals(TiltModel.RESET)){
            text.setText(message);
        }
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
