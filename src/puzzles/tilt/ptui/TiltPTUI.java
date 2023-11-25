package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
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
    public void gameLoop() throws IOException{
        String msg;
        while(gameOn){
            msg = "> ";
            String command = scan.nextLine().strip();
            if(command.equals("h") || command.equals("H")){
                model.setCurrentConfig(model.getHint());
            }
            else if(command.equals("l") || command.equals("L")){
                loadFromFile();
            }
            else if(command.equals("q") || command.equals("Q")){
                System.out.println("Quitting to main menu.");
                gameOn = false;
                return;
            }
            else if(command.equals("r") || command.equals("R")){
                System.out.println("Puzzle reset!");
                System.out.println(model.getOriginalConfig());
                model.setCurrentConfig(model.getOriginalConfig());
            }
            else{
                TiltConfig next = null;
                String[] options = command.split("\\s+");
                if(options.length!=2){
                    System.out.println("h(int)              -- hint next move");
                    System.out.println("l(oad) filename     -- load new puzzle file");
                    System.out.println("t(ilt) {N|S|E|W}    -- tilt the board in the given direction");
                    System.out.println("q(uit)              -- quit the current game");
                    System.out.println("r(eset)             -- reset the current game");
                }
                else{
                    if(options[1].equals("n")|| options[1].equals("N")){
                        next = model.getCurrentConfig().up();
                    }
                    else if(options[1].equals("s") || options[1].equals("S")){
                        next = model.getCurrentConfig().down();
                    }
                    else if(options[1].equals("e") || options[1].equals("E")){
                       next = model.getCurrentConfig().right();
                    }
                    else if(options[1].equals("w") || options[1].equals("W")){
                        next = model.getCurrentConfig().left();
                    }
                    else{
                        System.out.println("Format: T {N|S|E|W}");
                    }
                    if(next!=null){
                        model.setCurrentConfig(next);
                        System.out.println(model.getCurrentConfig());
                    }
                    else{
                        System.out.println("Illegal move. A blue slider will fall through the hole!");
                    }
                }

            }


        }


    }
    public boolean loadFromFile() throws IOException {
        boolean ready = false;
        while(!ready) {
            System.out.println("Enter a valid file name or type Q to go back.");
            String command = scan.next();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("going back...");
                return false;
            }
            if (model.loadBoardFile(command) != null) {
                ready = true;
            } else {
                return false;
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
        else if (message.startsWith(TiltModel.HINT_PREFIX)){
            System.out.println(message);
        }
    }

    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else{
            TiltPTUI tui = new TiltPTUI();
            tui.gameStart(args[0]);
            tui.gameLoop();
        }
    }
}
