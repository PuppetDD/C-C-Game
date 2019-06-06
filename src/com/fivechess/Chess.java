package com.fivechess;

import javafx.application.Application;
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
    private ChessPane chesspane;
    private Stage stage;

    public Chess(String color) {
        stage = new Stage();
        fiveChess = new FiveChess(20, 20, 28.0);
        fiveChess.setColor(color);
        chesspane = new ChessPane(fiveChess);
        //等待对方确认后再进行事件源绑定处理器
        chesspane.setOnMouseClicked(new PlayAction(fiveChess, chesspane));
        Scene scene = new Scene(chesspane, 700, 700);
        stage.setScene(scene);
        stage.setTitle("FiveChess   YourColor:" + fiveChess.getColor());
        stage.show();
    }

    public FiveChess getFiveChess() {
        return fiveChess;
    }

    public ChessPane getChesspane() {
        return chesspane;
    }

}

