package puzzles.tilt.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    /** the image for a green disk */
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    /** the image for a blue disk */
    private Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    /** the image for a blocker */
    private Image blocker = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    /** the image for the hole */
    private Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));
    /** represents an empty square on the board */
    private static Background EMPTY = new Background(new BackgroundFill(Color.SLATEGRAY,null,null));
    /** the background color for the central GridPane */
    private static Background BLACK = new Background(new BackgroundFill(Color.BLACK, null, null));
    /** represents each square as a Button */
    private Button[][] buttons;
    /** the model */
    private TiltModel model;
    /** the text for displaying a message */
    private Text text = new Text();
    /** the dimensions of the game board */
    private int dimensions;
    /** the dimensions of the stage */
    private final double SIZE = 650.0;
    /** the GridPane that represents the game board */
    private GridPane center;
    private String shortFilename;

    /**
     * Creates a new model and adds the GUI as an observer. Attempts to load the file given in the
     * command line.
     */

    public void init() {
        String filename = getParameters().getRaw().get(0);
        File file = new File(filename);
        this.model = new TiltModel();
        model.addObserver(this);
        loadFile(file);
    }

    /**
     * Creates an arrow button for tilting.
     * @param direction the direction to be tilted in
     * @param symbol the symbol that is on the face of the button
     * @param value the amount to multiply the button height or width by to set its size
     * @return the arrow button
     */
    private Button arrows(String direction, String symbol, double value){
        Button arrow = new Button(symbol);
        arrow.setOnAction(event -> {
            if(!model.gameOver()) {
                model.makeMove(direction);
                setBoard();
            }
        });
        if(direction.equals("north") || direction.equals("south")){
            arrow.setPrefWidth(SIZE*value);
        }
        else{
            arrow.setPrefHeight(SIZE*value);
        }
        arrow.setAlignment(Pos.CENTER);
        return arrow;
    }

    /**
     * Creates an HBox that is centered.
     * @return the HBox
     */
    private HBox box(){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Creates the inner BorderPane with the game board and arrow buttons.
     * @return the inner BorderPane
     */
    private BorderPane inner(){
        BorderPane inner = new BorderPane();
        Button upArrow = arrows("north","^",.8);
        Button downArrow = arrows("south","V",.8);
        Button rightArrow = arrows("east",">",.78);
        Button leftArrow = arrows("west","<",.78);
        HBox upBox = box();
        HBox downBox = box();
        HBox rightBox = box();
        HBox leftBox = box();
        makeBoard();
        upBox.getChildren().add(upArrow);
        downBox.getChildren().add(downArrow);
        rightBox.getChildren().add(rightArrow);
        leftBox.getChildren().add(leftArrow);
        inner.setTop(upBox);
        inner.setCenter(center);
        inner.setRight(rightBox);
        inner.setLeft(leftBox);
        inner.setBottom(downBox);
        inner.setPadding(new Insets(7));
        BorderPane.setMargin(upBox,new Insets(0,0,9,0));
        BorderPane.setMargin(downBox,new Insets(9,0,0,0));
        BorderPane.setMargin(rightBox,new Insets(0,0,0,9));
        BorderPane.setMargin(leftBox,new Insets(0,9,0,0));
        return inner;
    }

    /**
     * Creates the GUI.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception if there is trouble creating something
     */

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane outer = new BorderPane();
        outer.setPadding(new Insets(0,5,0,0));
        BorderPane inner = inner();
        HBox top = box();
        top.getChildren().add(text);
        VBox threeButtons = new VBox();
        threeButtons.setAlignment(Pos.CENTER);
        threeButtons.setSpacing(20);
        Button load = loadButton(stage, inner);
        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            model.reset();
            setBoard();
        });
        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            model.getHint();
            setBoard();
        });
        threeButtons.getChildren().addAll(load, reset, hint);
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

    /**
     * Creates the loading button.
     * @param stage the stage that choosing a file will be shown in
     * @param inner the inner BorderPane
     * @return the loading button
     */
    private Button loadButton(Stage stage, BorderPane inner) {
        Button load = new Button("Load");
        load.setOnAction(event -> {
            try {
                chooseFile(stage);
                if(model.getCurrentConfig()!=null) {
                    makeBoard();
                    setBoard();
                    inner.setCenter(center);
                }
            }
            catch (NullPointerException e){
                text.setText("No file chosen!");
            }
        });
        return load;
    }

    /**
     * Creates the center GridPane that represents the game board.
     */
    public void makeBoard() {
        center = new GridPane();
        center.setAlignment(Pos.CENTER);
        center.setVgap(25.0/dimensions);
        center.setHgap(25.0/dimensions);
        center.setBackground(BLACK);
        buttons = new Button[dimensions][dimensions];
        for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < dimensions; col++) {
                Button button = new Button();
                button.setPrefWidth(SIZE/dimensions);
                button.setPrefHeight(SIZE/dimensions);
                buttons[row][col] = button;
                center.add(button, col, row);
            }
        }
    }

    /**
     * Sets the game board so that each square is correctly represented by its image. Each square will either be empty,
     * have a green disk, a blue disk, a blocker, or be a hole.
     */
    private void setBoard(){
        BackgroundSize size = new BackgroundSize(SIZE/dimensions, SIZE/dimensions,
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

    /**
     * Used to load in game boards.
     * @param file the file to be read
     */
    public void loadFile(File file){
        String shortenedFilename = file.getAbsolutePath().substring
                (file.getAbsolutePath().lastIndexOf(File.separator)+1);
        shortFilename = shortenedFilename;
        model.loadBoardFile(file);
        if (model.getCurrentConfig()!=null) {
            dimensions = model.getCurrentConfig().getDimensions();
        }
    }

    /**
     * Allows the user to choose a game board to be loaded in.
     * @param stage the stage in which the file choosing is shown
     */
    public void chooseFile(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.lob"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        String shortenedFilename = selectedFile.getAbsolutePath().substring
                (selectedFile.getAbsolutePath().lastIndexOf(File.separator) + 1);
        shortFilename = shortenedFilename;
        model.loadBoardFile(selectedFile);
        if(model.getCurrentConfig()!=null) {
            dimensions = model.getCurrentConfig().getDimensions();
        }
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
        if(message.startsWith(TiltModel.LOADED)){
            text.setText("Loaded: " + shortFilename);
        }
        else if (message.startsWith(TiltModel.LOAD_FAILED)){
            text.setText("Failed to load: " + shortFilename);
            if(model.getCurrentConfig()==null){ // trying to launch the gui with a null config
                System.out.println(message);
                System.exit(1);
            }
        }
        else if (message.startsWith(TiltModel.HINT)){
            text.setText("Next step!");
        }
        else if (message.startsWith(TiltModel.NO_SOLUTION)){
            text.setText("No solution!");
        }
        else if (message.startsWith(TiltModel.MOVE)){
            text.setText("");
        }
        else if (message.equals(TiltModel.ILLEGAL)){
            text.setText(message);
        }
        else if (message.equals(TiltModel.RESET)){
            text.setText(message);
        }
        else if (message.startsWith(TiltModel.FINISHED)){
            text.setText("Already solved!");
            return;
        }
        if(model.getCurrentConfig()!=null) {
            if (model.gameOver()) {
                text.setText("Congratulations!");
            }
        }
    }

    /**
     * Launches the GUI if the correct number of command line arguments are present.
     * @param args [0] the Tilt board filename
     */

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
