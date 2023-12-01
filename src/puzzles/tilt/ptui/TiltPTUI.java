package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * The TUI representation for Tilt.
 */

public class TiltPTUI implements Observer<TiltModel, String> {
    /** the model */
    private TiltModel model;
    /** allows for user input */
    private Scanner scan;
    public TiltPTUI(){
        model = new TiltModel();
        model.addObserver(this);
        scan = new Scanner(System.in);
    }

    /**
     * Prints the five options the user has while playing the game.
     */
    private void help(){
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("t(ilt) {N|S|E|W}    -- tilt the board in the given direction");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
    }

    /**
     * The gameloop for the TUI that keeps getting user input until the user quits.
     * @param file the file for the game board
     */
    private void gameLoop(String file) {
        model.loadBoardFile(file);
        if(model.getCurrentConfig()==null){
            return;
        }
        help();
        while(true){
            String command = scan.nextLine().strip();
            System.out.print("> ");
            if(command.equalsIgnoreCase("h")){
                model.getHint();
            }
            else if(command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")){
                return;
            }
            else if(command.equalsIgnoreCase("r") || command.equalsIgnoreCase("reset")){
                model.reset();
            }
            else{
                TiltConfig next = null;
                String[] options = command.split("\\s+");
                if(options.length!=2){
                    help();
                }
                else {
                        if (options[0].equalsIgnoreCase("l") ||
                                command.equalsIgnoreCase("load")) {
                            model.loadBoardFile(options[1]);
                        } else if (options[1].equalsIgnoreCase("n")) {
                            next = model.makeMove("north");
                        } else if (options[1].equalsIgnoreCase("s")) {
                            next = model.makeMove("south");
                        } else if (options[1].equalsIgnoreCase("e")) {
                            next = model.makeMove("east");
                        } else if (options[1].equalsIgnoreCase("w")) {
                            next = model.makeMove("west");
                        } else {
                            System.out.println();
                            help();
                        }
                        if (next != null) {
                            model.setCurrentConfig(next);
                        }
                    }
                }
            }
        }

    /**
     * This method is called when making an action that would
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */


    @Override
    public void update(TiltModel model, String message) {
        if(message.startsWith(TiltModel.LOADED)){
            System.out.println(message  + "\n" + model.getOriginalConfig() + "\n");
        }
        else if(message.startsWith(TiltModel.LOAD_FAILED)){
            System.out.println(message);
            if(this.model.getCurrentConfig()!=null) {
                System.out.println(this.model.getCurrentConfig() + "\n");
            }
        }
        else if (message.startsWith(TiltModel.HINT)){
            System.out.println(message + "\n");
        }
        else if (message.startsWith(TiltModel.NO_SOLUTION)){
            System.out.println(message);
        }
        else if (message.startsWith(TiltModel.FINISHED)){
            System.out.println(message);
        }
        else if(message.startsWith(TiltModel.MOVE)){
            if(model.getCurrentConfig()!=null) {
                System.out.println(model.getCurrentConfig() + "\n");
            }
        }
        else if(message.equals(TiltModel.ILLEGAL)){
            System.out.println(message + "\n" + model.getCurrentConfig() + "\n");
        }
        else if(message.equals(TiltModel.RESET)){
            System.out.println(message + "\n" + model.getCurrentConfig() + "\n");
        }
    }

    /**
     * Creates tui and runs the gameloop.
     * @param args [0] the filename
     */

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else{
            TiltPTUI tui = new TiltPTUI();
            tui.gameLoop(args[0]);
        }
    }
}
