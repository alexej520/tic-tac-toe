package tictactoe;

/**
 * Created by alexej520 on 29.07.2017.
 */
public class Game {

    public static final int EMPTY = 0;
    public static final int NOUGHT = 1;
    public static final int CROSS = 2;

    public static int enemyOf(int team){
        if (team == NOUGHT)
            return CROSS;
        else if (team == CROSS)
            return NOUGHT;
        else
            return EMPTY;
    }

    private int [][] field = new int[3][3];

    private int stepCounter = 0;
    private boolean hasWinner = false;

    private int lastX = 0, lastY = 0;

    public int getStepCounter() {
        return stepCounter;
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public boolean checkBoundaries(int x, int y){
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }


    public boolean isEmpty(int x, int y) {
        return field[y][x] == EMPTY;
    }


    public int getCharacter(int x, int y) {
        return field[y][x];
    }


    public void setCharacter(int x, int y) throws RuntimeException{
        if (!isEmpty(x, y)) throw new RuntimeException(String.format("Cell [%d, %d] is full!", x, y));
        stepCounter++;
        lastX = x;
        lastY = y;
        field[y][x] = stepCounter % 2 == 1 ? CROSS : NOUGHT;
    }


    public boolean isGameOver(){
        for (int i = 0; i < 3; i++) {
            if (field[i][0] == EMPTY) continue;
            boolean horizontal = true;
            for (int j = 0; j < 2 && horizontal; j++)
                horizontal = field[i][j] == field[i][j + 1];
            if (horizontal) return hasWinner = true;
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i] == EMPTY) continue;
            boolean vertical = true;
            for (int j = 0; vertical && j < 2; j++)
                vertical = field[j][i] == field[j + 1][i];
            if (vertical) return hasWinner = true;
        }
        if (field[1][1] != EMPTY){
            boolean leftDiagonal = true;
            for (int i = 0; leftDiagonal && i < 2; i++)
                leftDiagonal = field[i][i] == field[i + 1][i + 1];
            if (leftDiagonal) return hasWinner = true;

            boolean rightDiagonal = true;
            for (int i = 0; rightDiagonal && i < 2; i++)
                rightDiagonal = field[i][2 - i] == field[i + 1][1 - i];
            if (rightDiagonal) return hasWinner = true;
        }
        return stepCounter == 9;
    }


    public int getWinner(){
        if (hasWinner)
            return stepCounter % 2 == 1 ? CROSS : NOUGHT;
        return EMPTY;
    }
}
