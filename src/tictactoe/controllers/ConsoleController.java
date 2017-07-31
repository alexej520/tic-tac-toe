package tictactoe.controllers;

import tictactoe.Game;
import tictactoe.views.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by alexej520 on 29.07.2017.
 */
public class ConsoleController extends AbstractController {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    public ConsoleController(Game game, View view) {
        super(game, view);
    }


    @Override
    public int initPlayerTeam() throws Exception {
        if (team != Game.EMPTY) return team;
        System.out.println("Select side. Enter 'X' or 'O'");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String playerTeam = br.readLine();
        if ("X".equalsIgnoreCase(playerTeam)) {
            team = Game.CROSS;
            initEnemy();
        }
        else if ("O".equalsIgnoreCase(playerTeam) || "0".equals(playerTeam)) {
            team = Game.NOUGHT;
            initEnemy();
        }
        return initPlayerTeam();
    }

    private int getCoordinate(String name) throws IOException{
        int c;
        try {
            view.showMessage(String.format("Enter '%s' coordinate", name));
            c = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e){
            view.showMessage("Coordinate must be integer from 0 to 2");
            return getCoordinate(name);
        }
        return c;
    }


    @Override
    public void step() throws Exception{
        view.showMessage("Your turn!");
        int x = 0, y = 0;

        try {
            x = getCoordinate("x");
            y = getCoordinate("y");
        }catch (IOException e){
            e.printStackTrace();
        }

        if (game.checkBoundaries(x, y)){
            if (game.isEmpty(x, y)) {
                game.setCharacter(x, y);
            }
            else {
                view.showMessage(String.format("Cell: [%d, %d] is already full", x, y));
                step();
            }
        }
        else {
            view.showMessage(String.format("Cell: [%d, %d] is out of bounds.", x, y));
            step();
        }
    }
}
