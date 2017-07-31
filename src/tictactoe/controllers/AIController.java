package tictactoe.controllers;

import javafx.util.Pair;
import tictactoe.Game;
import tictactoe.views.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexej520 on 29.07.2017.
 */
public class AIController extends AbstractController {
    private List<Pair<Integer, Integer>> corners = new ArrayList<>();
    private List<Pair<Integer, Integer>> sides = new ArrayList<>();
    private List<Pair<Integer, Integer>> unsortedCorners;

    public AIController(Game game) throws Exception{
        this(game, null);
    }

    public AIController(Game game, View view) throws Exception{
        super(game, view);

        corners.add(new Pair<>(0, 0));
        sides.add(new Pair<>(0, 1));
        corners.add(new Pair<>(0, 2));
        sides.add(new Pair<>(1, 2));
        corners.add(new Pair<>(2, 2));
        sides.add(new Pair<>(2, 1));
        corners.add(new Pair<>(2, 0));
        sides.add(new Pair<>(1, 0));

        unsortedCorners = new ArrayList<>(corners);
        corners = Collections.unmodifiableList(corners);
        sides = Collections.unmodifiableList(sides);
    }

    @Override
    public int initPlayerTeam() throws Exception {
        if (team == Game.EMPTY) {
            team = Math.random() < 0.5 ? Game.CROSS : Game.EMPTY;
            initEnemy();
        }
        return team;
    }

    private Pair<Integer, Integer> getWinnerCoordinate(int team) throws Exception{
        if (team == Game.EMPTY) throw new Exception("Wrong team");
        int lastEmpty = 0;

        nextVertical:
        for (int i = 0; i < 3; i++) {
            int verticalCounter = 0;
            for (int j = 0; j < 3 ; j++){
                int character = game.getCharacter(i, j);
                if (character == team)
                    verticalCounter++;
                else if (character != Game.EMPTY)
                    continue nextVertical;
                else
                    lastEmpty = j;
            }
            if (verticalCounter == 2){
                return new Pair<>(i, lastEmpty);
            }
        }

        nextHorizontal:
        for (int i = 0; i < 3; i++) {
            int horizontalCounter = 0;
            for (int j = 0; j < 3 ; j++){
                int character = game.getCharacter(j, i);
                if (character == team)
                    horizontalCounter++;
                else if (character != Game.EMPTY)
                    continue nextHorizontal;
                else
                    lastEmpty = j;
            }
            if (horizontalCounter == 2){
                return new Pair<>(lastEmpty, i);
            }
        }

        nextDiagonal:
        {
            int leftDiagonalCounter = 0;
            for (int i = 0; i < 3; i++){
                int character = game.getCharacter(i, i);
                if (character == team)
                    leftDiagonalCounter++;
                else if (character != Game.EMPTY)
                    break nextDiagonal;
                else
                    lastEmpty = i;
            }
            if (leftDiagonalCounter == 2)
                return new Pair<>(lastEmpty, lastEmpty);
        }

        nextDiagonal:
        {
            int rightDiagonalCounter = 0;
            for (int i = 0; i < 3; i++){
                int character = game.getCharacter(i, 2 - i);
                if (character == team)
                    rightDiagonalCounter++;
                else if (character != Game.EMPTY)
                    break nextDiagonal;
                else
                    lastEmpty = i;
            }
            if (rightDiagonalCounter == 2)
                return new Pair<>(lastEmpty, 2 - lastEmpty);
        }
        return null;
    }

    private Pair<Integer, Integer> checkSides() {
        int lastX = game.getLastX();
        int lastY = game.getLastY();
        int sideIndex;
        if ((sideIndex = sides.indexOf(new Pair<>(lastX, lastY))) >= 0) {
            Pair<Integer, Integer> lSide, rSide;
            int lIndex = (sideIndex + 1) % 4;
            int rIndex = (sideIndex + 3) % 4;
            lSide = sides.get(lIndex);
            if (checkFork(lSide, rIndex))
                return corners.get(lIndex);
            rSide = sides.get(rIndex);
            if (checkFork(rSide, rIndex))
                return corners.get(sideIndex);
        }
        return null;
    }


    @Override
    public void start() {

    }

    private boolean checkFork(Pair<Integer, Integer> side, int rIndex){
        Pair<Integer, Integer> corner;
        if (game.getCharacter(side.getKey(), side.getValue()) == enemy) {
            for (int i = 0; i < 3; i++){
                corner = corners.get((rIndex + i) % 4);
                if (!game.isEmpty(corner.getKey(), corner.getValue()))
                    return false;
            }
            return true;
        }
        return false;
    }


    private Pair<Integer, Integer> checkCorners(){
        int lastX = game.getLastX();
        int lastY = game.getLastY();

        unsortedCorners.sort((Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) -> {
            int delta1 = Math.abs(lastX - o1.getKey()) + Math.abs(lastY - o1.getValue());
            int delta2 = Math.abs(lastX - o2.getKey()) + Math.abs(lastY - o2.getValue());
            return delta1 - delta2;
        });

        Pair<Integer, Integer> corner = null;
        for (Pair<Integer, Integer> p : unsortedCorners){
            if (game.isEmpty(p.getKey(), p.getValue())) {
                corner = p;
                break;
            }
        }
        return corner;
    }

    @Override
    public void step() throws Exception {
        if (view!= null) view.showMessage("AI turn!");
        Pair<Integer, Integer> coord;
        if ((coord = getWinnerCoordinate(team)) != null);
        else if ((coord = getWinnerCoordinate(enemy)) != null);
        else if ((coord = checkSides()) != null);
        else if (game.isEmpty(1, 1)) coord = new Pair<>(1, 1);
        else if ((coord = checkCorners()) != null);
        else {
            loop:
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (game.isEmpty(i, j)){
                        coord = new Pair<>(i, j);
                        break loop;
                    }
        }
        game.setCharacter(coord.getKey(), coord.getValue());
    }

    @Override
    public void gameOver() {

    }
}
