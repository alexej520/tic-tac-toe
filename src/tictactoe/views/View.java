package tictactoe.views;

/**
 * Created by alexej520 on 29.07.2017.
 */
public interface View {
    void initialize();
    void draw();
    void showMessage(String message);
    void showGameOver();
}
