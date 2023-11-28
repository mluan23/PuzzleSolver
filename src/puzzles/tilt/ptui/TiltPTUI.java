package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import java.io.IOException;
import java.util.Scanner;


public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;
    private Scanner scan;
    public TiltPTUI(){
        model = new TiltModel();
        model.addObserver(this);
        scan = new Scanner(System.in);
    }
    public void help(){
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("t(ilt) {N|S|E|W}    -- tilt the board in the given direction");
        System.out.println("q(uit)              -- quit the current game");
        System.out.println("r(eset)             -- reset the current game");
    }
    public void gameLoop(String file) throws IOException{
        model.setCurrentConfig(model.loadBoardFile(file));
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
                    System.out.println();
                    help();
                }
                else {
                    if (options[0].equalsIgnoreCase("l") || command.equalsIgnoreCase("load")){
                        TiltConfig t = model.loadBoardFile(options[1]);
                        if (t != null) {
                            model.setCurrentConfig(t);
                        }
                    } else {
                        if (options[1].equalsIgnoreCase("n")) {
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
    }

    @Override
    public void update(TiltModel model, String message) {
        if(message.startsWith(TiltModel.LOADED)){
            System.out.println(message  + "\n" + model.getOriginalConfig() + "\n");
        }
        else if(message.startsWith(TiltModel.LOAD_FAILED)){
            System.out.println(message);
            if(this.model.getCurrentConfig()!=null) {
                System.out.println(this.model.getCurrentConfig());
            }
        }
        else if (message.startsWith(TiltModel.HINT_PREFIX)){
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
                System.out.println("\n" + model.getCurrentConfig() + "\n");
            }
        }
        else if(message.equals(TiltModel.ILLEGAL)){
            System.out.println(message + "\n" + model.getCurrentConfig() + "\n");
        }
        else if(message.equals(TiltModel.RESET)){
            System.out.println(message + model.getCurrentConfig() + "\n");
        }
//        if(model.getCurrentConfig()!=null) {
//            if (model.gameOver()) {
//                System.out.println("You win!");
//            }
//        }
    }

    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else{
            TiltPTUI tui = new TiltPTUI();
            tui.gameLoop(args[0]);
        }
    }
}
