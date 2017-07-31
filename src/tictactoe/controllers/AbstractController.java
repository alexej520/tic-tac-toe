package tictactoe.controllers;

import tictactoe.Game;
import tictactoe.views.View;

/**
 * Created by alexej520 on 29.07.2017.
 */
public abstract class AbstractController {
    protected Game game;
    protected View view;
    protected int team = Game.EMPTY;
    protected int enemy = game.EMPTY;

    public AbstractController(Game game, View view) {
        this.game = game;
        this.view = view;
    }

    public int getEnemyTeam() {
        return enemy;
    }

    public void start(){
        view.initialize();
    }

    public abstract void step() throws Exception;

    public abstract int initPlayerTeam() throws Exception;

    public void setPlayerTeam(int team) throws Exception{
        this.team = team;
        initEnemy();
    }

    protected void initEnemy() throws Exception{
        enemy = Game.enemyOf(team);
        if (enemy == Game.EMPTY)
            throw new Exception("Wrong team!");
    }

    public void gameOver(){
        view.showGameOver();
    }
}
