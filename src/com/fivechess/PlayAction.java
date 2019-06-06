package com.fivechess;

import com.nioclient.host.UdpClient;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

/**
 * C-C-Game
 * com.fivechess
 *
 * @author GOLD
 * @date 2019/6/6
 */
public class PlayAction implements EventHandler<MouseEvent> {

    private FiveChess fiveChess;
    private ChessPane chessPane;

    public PlayAction(FiveChess fiveChess, ChessPane chessPane) {
        this.chessPane = chessPane;
        this.fiveChess = fiveChess;
    }

    @Override
    public void handle(MouseEvent event) {
        if (fiveChess.getCurrentSide() != fiveChess.getFlag()) {
            //处理鼠标点击事件
            double cell = fiveChess.getCellLen();

            //event.getX()获取鼠标点击x坐标，返回double类型
            double x = event.getX();
            double y = event.getY();

            int i = (int) ((x - 50 + cell / 2) / cell);
            int j = (int) ((y - 50 + cell / 2) / cell);

            System.out.println(i + "    " + j);
            fiveChess.play(i, j, fiveChess.getCurrentSide());
            chessPane.drawChess(cell);
            //客户端之间通信发送坐标
            UdpClient.send(i + "," + j);
            if (!fiveChess.judgeGame(i, j, fiveChess.getCurrentSide())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FiveChess");
                alert.setHeaderText("Note");
                alert.setContentText((fiveChess.getCurrentSide() == 'W' ? "White" : "Black") + " is the winner！");
                alert.showAndWait();
                //点击确定后关闭五子棋窗口，并且设置UdpClient.number=0
            }
        }
    }

}
