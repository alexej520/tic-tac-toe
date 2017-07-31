package tictactoe;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tictactoe.controllers.AIController;
import tictactoe.controllers.AbstractController;
import tictactoe.controllers.ConsoleController;
import tictactoe.views.ConsoleView;
import tictactoe.views.View;

import java.util.Optional;

import static tictactoe.Game.CROSS;
import static tictactoe.Game.NOUGHT;

public class Main extends Application implements View {

    private Game game;
    private Button[][] buttonField = new Button[3][3];
    private Text messageBox;
    private ButtonType nought = new ButtonType("O");
    private ButtonType cross = new ButtonType("X");
    private ButtonType restart = new ButtonType("restart");
    private ButtonType close = new ButtonType("close");

    private AIController aiController;


    public static void main(String[] args) throws Exception {
        if (args.length == 1 && "console".equals(args[0])){
            Game game = new Game();
            View view = new ConsoleView(game);
            AbstractController playerController = new ConsoleController(game, view);
            AbstractController otherPlayerController = new AIController(game, view);
            AbstractController controller1, controller2;

            int team = playerController.initPlayerTeam();
            otherPlayerController.setPlayerTeam(Game.enemyOf(team));
            if (team == Game.CROSS){
                controller1 = playerController;
                controller2 = otherPlayerController;
            }
            else {
                controller2 = playerController;
                controller1 = otherPlayerController;
            }
            controller1.start();
            controller2.start();

            while (true){
                view.draw();
                controller1.step();
                if (game.isGameOver()) break;
                view.draw();
                controller2.step();
                if (game.isGameOver()) break;
            }
            view.draw();
            controller1.gameOver();
            controller2.gameOver();
        }
        else {
            launch();
        }
    }

    private void initGUI(Stage primaryStage) throws Exception{

        VBox root = new VBox();
        messageBox = new Text();
        root.getChildren().add(messageBox);
        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button b = new Button();
                buttonField[i][j] = b;
                b.setPrefSize(100, 100);
                gridPane.add(b, i, j);
            }
        }
        primaryStage.setTitle("Tic-tac-toe");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };

        animationTimer.start();
    }

    private void initGame() throws Exception{
        game = new Game();
        aiController = new AIController(game, this);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++){
                final int x = i, y = j;
                buttonField[i][j].setOnMouseClicked(event -> {
                    if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                        if (game.checkBoundaries(x, y) && game.isEmpty(x, y)) {
                            game.setCharacter(x, y);
                            if (game.isGameOver()){
                                showGameOver();
                                aiController.gameOver();
                            }
                            else {
                                try {
                                    aiController.step();
                                    showMessage("Your turn!");
                                } catch (Exception e) {
                                    throw new RuntimeException("AI is not initialised");
                                }
                                if (game.isGameOver()){
                                    showGameOver();
                                    aiController.gameOver();
                                }
                            }
                        }
                    }
                });
            }
        aiController.start();

        int team = showSelectTeamDialog();
        aiController.setPlayerTeam(Game.enemyOf(team));
        if (team == NOUGHT) {
            aiController.step();
            showMessage("Your turn!");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initGUI(primaryStage);
        initGame();
    }


    public int showSelectTeamDialog() throws InterruptedException {

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Select team", cross, nought).showAndWait();

        if (result.get() == nought)
            return Game.NOUGHT;
        else if (result.get() == cross)
            return Game.CROSS;
        else
            return showSelectTeamDialog();
    }

    @Override
    public void initialize() {

    }

    public void draw() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                String s;
                switch (game.getCharacter(i, j)) {
                    case CROSS: s = "X"; break;
                    case NOUGHT: s = "O"; break;
                    default: s = " ";
                }
                buttonField[i][j].setText(s);
            }
    }


    public void showMessage(String message) {
        messageBox.setText(message);
    }


    public void showGameOver() {
        String message;
        switch (game.getWinner()){
            case CROSS: message = "Crosses 'X' win!"; break;
            case NOUGHT: message = "Noughts 'O' win!"; break;
            default: message = "Draw!"; break;
        }
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, message, restart, close).showAndWait();
        if (result.get() == restart)
            try {
                initGame();
            } catch (Exception e) {
            }
    }
}
