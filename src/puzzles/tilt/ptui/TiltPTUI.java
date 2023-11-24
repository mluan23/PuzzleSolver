package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;
import puzzles.tilt.solver.Tilt;

import java.io.IOException;
import java.util.Scanner;


public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;
    private Scanner scan;
    private Tilt tilt;
    private boolean gameOn;
    public TiltPTUI(){
        model = new TiltModel();
        model.addObserver(this);
        scan = new Scanner(System.in);
        this.tilt = new Tilt();
        gameOn = false;
    }
    public void display(){

    }
    public void gameStart(String file) throws IOException{
        model.setCurrentConfig(model.loadBoardFile(file));
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("t(ilt) {N|S|E|W}    -- tilt the board in the given direction");
        System.out.println("q(uit)              -- quit the current game");
        System.out.println("r(eset)             -- reset the current game");
        gameOn=true;
    }
    public void gameLoop(){
        String msg;
        while(gameOn){
            msg = "> ";
            System.out.println();

        }


    }
    public boolean loadFromFile() throws IOException {
        boolean ready = false;
        while(!ready){
            System.out.println("Enter a valid file name or type Q to go back.");
            String command = scan.next();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("going back...");
                return false;
            }
            if(model.loadBoardFile(command)!=null){
                ready=true;
        }
    }
        return true;
    }

    @Override
    public void update(TiltModel model, String message) {
        if(message.startsWith(TiltModel.LOADED)){
            System.out.println(message);
        }
        else if(message.equals(TiltModel.LOAD_FAILED)){
            System.out.println("Error loading game");
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else{
        }
    }
}
