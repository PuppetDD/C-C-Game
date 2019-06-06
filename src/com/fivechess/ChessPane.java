package com.fivechess;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * C-C-Game
 * com.fivechess
 *
 * @author GOLD
 * @date 2019/6/6
 */
public class ChessPane extends Pane {

    private Canvas canvas;
    private GraphicsContext gc;
    private FiveChess fiveChess;

    public ChessPane(FiveChess fiveChess) {
        this.fiveChess = fiveChess;
        double cell = fiveChess.getCellLen();
        drawPane(cell);
        drawChess(cell);
        getChildren().add(canvas);
    }

    public void drawPane(double cell) {
        canvas = new Canvas(700, 800);
        gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //绘制棋盘
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < fiveChess.getWidth(); i++) {
            for (int j = 0; j < fiveChess.getHeight(); j++) {
                //清理一个矩形取区域的内容
                gc.strokeRect(50 + i * cell, 50 + cell * j, cell, cell);
            }
        }
    }

    public void drawChess(double cell) {
        char[][] chess = fiveChess.getChess();
        for (int i = 0; i <= fiveChess.getHeight(); i++) {
            for (int j = 0; j <= fiveChess.getWidth(); j++) {
                if (chess[i][j] == 'B') {
                    //设置填充色
                    gc.setFill(Color.BLACK);
                    gc.fillOval(50 + i * cell - cell / 2, 50 + j * cell - cell / 2, cell, cell);
                } else if (chess[i][j] == 'W') {
                    gc.setFill(Color.WHITE);
                    //填充椭圆
                    gc.fillOval(50 + i * cell - cell / 2, 50 + j * cell - cell / 2, cell, cell);
                    //绘制轮廓
                    gc.strokeOval(50 + i * cell - cell / 2, 50 + j * cell - cell / 2, cell, cell);
                }
            }
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public FiveChess getFiveChess() {
        return fiveChess;
    }

    public void setFiveChess(FiveChess fiveChess) {
        this.fiveChess = fiveChess;
    }

}
