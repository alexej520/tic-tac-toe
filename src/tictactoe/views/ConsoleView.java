package tictactoe.views;

import tictactoe.Game;

import static tictactoe.Game.CROSS;
import static tictactoe.Game.NOUGHT;

/**
 * Created by alexej520 on 29.07.2017.
 */
public class ConsoleView implements View {

    private Game game;


    public ConsoleView(Game game) {
        this.game = game;
    }


    private char drawCharacter(int ch){
        switch (ch){
            case Game.NOUGHT: return 'O';
            case Game.CROSS: return 'X';
            default: return ' ';
        }
    }

    @Override
    public void initialize() {

    }

    public void draw(){
        System.out.println("+---+---+---+");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(String.format("| %c ", drawCharacter(game.getCharacter(j, i))));
            System.out.println("|");
            System.out.println("+---+---+---+");
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showGameOver() {
        String message;
        switch (game.getWinner()){
            case CROSS: message = "Crosses 'X' win!"; break;
            case NOUGHT: message = "Noughts 'O' win!"; break;
            default: message = "Draw!"; break;
        }
        showMessage(message);
    }
}
