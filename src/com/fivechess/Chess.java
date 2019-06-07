package com.fivechess;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * C-C-Game
 * com.fivechess
 *
 * @author GOLD
 * @date 2019/6/6
 */
public class Chess {

    private FiveChess fiveChess;
    private ChessPane chessPane;
    private static Stage stage;

    public Chess(String color) {
        stage = new Stage();
        fiveChess = new FiveChess(20, 20, 28.0);
        fiveChess.setColor(color);
        chessPane = new ChessPane(fiveChess);
        //等待对方确认后再进行事件源绑定处理器
        chessPane.setOnMouseClicked(new PlayAction(fiveChess, chessPane));
        Scene scene = new Scene(chessPane, 650, 700);
        stage.setScene(scene);
        stage.setTitle("FiveChess   YourColor:" + fiveChess.getColor());
        stage.show();
    }

    public FiveChess getFiveChess() {
        return fiveChess;
    }

    public ChessPane getChessPane() {
        return chessPane;
    }

    public static void close() {
        stage.close();
    }

}

