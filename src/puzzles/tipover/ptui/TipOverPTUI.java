package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;

import java.util.Arrays;
import java.util.Scanner;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private Scanner input;

    private String commands = """
                    h(int)              -- hint next move\s
                    l(oad) filename     -- load new puzzle file\s
                    m(ove) {N|S|E|W}    -- move the tipper in the given direction\s
                    q(uit)              -- quit the game\s
                    r(eset)             -- reset the current game""";

    public TipOverPTUI() {
        model = new TipOverModel();
        model.addObserver(this);
        input = new Scanner(System.in);
    }

    public void playPTUI(String file) {
        model.loadBoardFromFile(file);
        if (model.getCurrentConfig() == null) {
            return;
        }

        System.out.println(commands);
        while (true) {
            String com = input.nextLine().strip();
            System.out.print("> ");
            if (com.equalsIgnoreCase("h") || com.equalsIgnoreCase("hint")) {
                model.getHint();
            }
            else if (com.equalsIgnoreCase("q") || com.equalsIgnoreCase("quit")) {
                return;
            }
            else if (com.equalsIgnoreCase("r") || com.equalsIgnoreCase("reset")) {
                model.reset();
            }
            else {
                String[] comms = com.split("\\s+");

                if (comms.length != 2) {
                    System.out.println(commands);
                }
                else {
                    if (comms[0].equalsIgnoreCase("l") || comms[0].equalsIgnoreCase("load")) {
                        model.loadBoardFromFile(comms[1]);
                    }
                    else if (comms[1].equalsIgnoreCase("n")) {
                        model.move("North");
                    }
                    else if (comms[1].equalsIgnoreCase("e")) {
                        model.move("East");
                    }
                    else if (comms[1].equalsIgnoreCase("s")) {
                        model.move("South");
                    }
                    else if (comms[1].equalsIgnoreCase("w")) {
                        model.move("West");
                    }
                    else {
                        System.out.println("\n" + commands);
                    }
                }
            }
        }
    }

    @Override
    public void update(TipOverModel model, String message) {
        if (message.startsWith(TipOverModel.LOADED)) {
            System.out.println(message  + "\n" + model.getCopyConfig() + "\n");
        }
        else if (message.startsWith(TipOverModel.LOAD_FAILED)) {
            System.out.println(message);
            if (this.model.getCurrentConfig() != null) {
                System.out.println(this.model.getCurrentConfig() + "\n");
            }
        }
        else if (message.startsWith(TipOverModel.RESET)) {
            System.out.println(message + "\n" + model.getCurrentConfig() + "\n");
        }
        else if (message.startsWith(TipOverModel.HINT)) {
            System.out.println(message + "\n");
        }
        else if (message.startsWith(TipOverModel.MOVE)) {
            if(model.getCurrentConfig() != null) {
                System.out.println(model.getCurrentConfig() + "\n");
            }
        }
        else if (message.startsWith(TipOverModel.INVALID)) {
            System.out.println(message + "\n" + model.getCurrentConfig() + "\n");
        }
        else if (message.startsWith(TipOverModel.COMPLETE)) {
            System.out.println(message);
        }
        else if (message.startsWith(TipOverModel.NO_SOLUTION)) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        }
        else {
            TipOverPTUI pTUI = new TipOverPTUI();
            pTUI.playPTUI(args[0]);
        }
    }
}
