package com.nioclient.host;

import com.fivechess.Chess;
import com.fivechess.ChessPane;
import com.fivechess.FiveChess;
import com.protocol.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author GOLD
 */
public class NioClientMain extends Application {

    private NioClient c;
    private String ip = "127.0.0.1";
    private int port = 9999;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(NioClient.getClientSe(), NioClient.getClientRe());
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(20, 30, 20, 30));
        primaryStage.setTitle("NioClient");
        Scene scene = new Scene(hbox, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        NioClient.getClientSe().getConnect().setOnAction(e -> {
            connect();
        });
        NioClient.getClientSe().getBattle().setOnAction(e -> {
            battle();
        });
        primaryStage.show();
    }

    private void connect() {
        String s = NioClient.getClientSe().getConnect().getText();
        String name = NioClient.getClientSe().getTextname().getText();
        if (s.compareTo("Connect") == 0 || s.compareTo("Reconnect") == 0) {
            UdpClient u = new UdpClient();
            if (!UdpClient.error) {
                u.start();
                c = new NioClient(name, ip, port, u.getPort());
                c.start();
            }
        } else {
            c.setStop();
        }
    }

    private void battle() {
        //请求对战
        User u = NioClient.getClientSe().getList().getSelectionModel().getSelectedItems().get(0);
        String s = NioClient.getClientSe().getCbo().getValue();
        if (UdpClient.number == 0 && u != null && s != "") {
            //当前正在对战，只允许一个对手
            UdpClient.number = 1;
            UdpClient.opponent = u;
            UdpClient.color = s;
            System.out.println("Opponent:" + u + "  Color:" + s);
            UdpClient.send(NioClient.getClientSe().getCbo().getValue());
        } else if (UdpClient.number == 1) {
            NioClient.getClientRe().getRetext().appendText("Please complete the current battle\n");
        } else {
            NioClient.getClientRe().getRetext().appendText("Please choosing your opponent and color\n");
        }
    }

}
