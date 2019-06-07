package com.nioclient.host;

import com.fivechess.Chess;
import com.protocol.Message;
import com.protocol.User;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * C-C
 * com.nioclient.host
 *
 * @author GOLD
 * @date 2019/4/7
 */
public class UdpClient extends Thread {

    private int port;
    private static DatagramChannel channel = null;
    /**
     * 对手，请求对战或接受对战的时候设立
     **/
    public static User opponent;
    public static int number = 0;
    public static String color;
    public static Boolean error;
    public Boolean one;
    private Chess chess;
    private String sender;

    public UdpClient() {
        System.out.println("\nNew UdpClient successful");
        error = false;
        try {
            if (channel != null) {
                //关闭上一次的线程通道，造成线程异常退出，释放socket以及端口
                channel.close();
                System.out.println("Close the previous thread");
            }
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(0));
            this.port = channel.socket().getLocalPort();
            System.out.println("Local Port:" + channel.socket().getLocalPort());
        } catch (IOException e) {
            NioClient.getClientRe().getRetext().appendText("The listening port is already in use\n");
            error = true;
        }
        this.one = true;
    }

    @Override
    public void run() {
        System.out.println("Listening port:" + port);
        while (one) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                ByteBuffer line = ByteBuffer.allocate(1024);
                /*阻塞，等待发来的数据*/
                channel.receive(buffer);
                System.out.println("Receive UDP:");
                /*设置缓冲区可读*/
                buffer.flip();
                /*循环读出所有字符*/
                int n = 0;
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    if (b == 10 || b == 13) {
                        line.flip();
                        String message = Charset.forName("UTF-8").decode(line).toString();
                        if (n == 0 && message.compareTo(NioClient.local.unique()) != 0) {
                            //拒收收信方不是当前用户的信息
                            break;
                        } else if (message.compareTo(NioClient.local.unique()) != 0) {
                            NioClient.getClientRe().getRetext().appendText(message + "\n");
                        }
                        n++;
                        //信息内容
                        if (n == 2) {
                            //获取信息中对方的用户信息
                            String[] attribute = message.split("--");
                            sender = attribute[0];
                        }
                        if (n == 3) {
                            String[] attribute = message.split(",");
                            if (attribute.length == 1) {
                                System.out.println(message);
                                if (message.compareTo("Accept") == 0) {
                                    //接受对战
                                    setOpponent(sender);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            chess = new Chess(color);
                                        }
                                    });
                                }
                                if (message.compareTo("Refuse") == 0) {
                                    number = 0;
                                }
                                if (message.compareTo("Lose") == 0) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            chess.getFiveChess().setEnd(true);
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("FiveChess");
                                            alert.setHeaderText("Note");
                                            alert.setContentText((chess.getFiveChess().getLocalSide() == 'W' ? "Black" : "White") + " conceded defeat");
                                            alert.showAndWait();
                                            Chess.close();
                                            number = 0;
                                        }
                                    });
                                }
                                if (message.compareTo("White") == 0 || message.compareTo("Black") == 0) {
                                    //弹出对战请求，确认后开始对战
                                    color = message.compareTo("White") == 0 ? "Black" : "White";
                                    battleRequest();
                                }
                            } else if (attribute.length == 3) {
                                //接受对方的落子信息
                                int x = Integer.valueOf(attribute[0]);
                                int y = Integer.valueOf(attribute[1]);
                                String c = attribute[2];
                                opponentPlay(x, y, c);
                            }
                        }
                        System.out.println(message);
                        line.clear();
                    } else {
                        line.put(b);
                    }
                }
                buffer.clear();
            } catch (Exception e) {
                //由于channel关闭导致channel.receive(buffer)异常退出线程
                //一个客户端只允许一个监听UDP消息的线程
                this.one = false;
            }
        }
        System.out.println("Stop listening port:" + port);
    }

    public void battleRequest() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to accept this battle", no, yes);
                //设置窗口的标题
                alert.setTitle("FiveChess");
                alert.setHeaderText("Battle Request from " + sender);
                //设置对话框的 icon 图标，参数是主窗口的 stage
                //alert.initOwner();
                //showAndWait() 将在对话框消失以前不会执行之后的代码
                Optional<ButtonType> choose = alert.showAndWait();
                //根据点击结果返回
                System.out.println("Choosing:" + choose);
                if (choose.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    System.out.println("Accept");
                    number = 1;
                    chess = new Chess(color);
                    setOpponent(sender);
                    send("Accept");
                    NioClient.getClientSe().getCbo().setValue(color);
                } else {
                    setOpponent(sender);
                    send("Refuse");
                }
            }
        });
    }

    public void opponentPlay(int x, int y, String color) {
        double cell = chess.getFiveChess().getCellLen();
        char opponentColor = color.compareTo("W") == 0 ? 'W' : 'B';
        chess.getFiveChess().play(x, y, opponentColor);
        chess.getChessPane().drawChess(cell);
        if (!chess.getFiveChess().judgeGame(x, y, opponentColor)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chess.getFiveChess().setEnd(true);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("FiveChess");
                    alert.setHeaderText("Note");
                    alert.setContentText((color.compareTo("W") == 0 ? "White" : "Black") + " is the winner！");
                    alert.showAndWait();
                    Chess.close();
                    number = 0;
                }
            });
        }
    }

    public static void send(String data) {
        System.out.println("Send");
        if (opponent != null) {
            System.out.println("opponent:" + opponent);
            try {
                DatagramChannel channel = DatagramChannel.open();
                String message = new Message(opponent, NioClient.local, data).toString();
                byte[] bytes = new byte[0];
                try {
                    bytes = message.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println("Coding failure");
                }
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                buffer.clear();
                buffer.put(bytes);
                buffer.flip();
                /*发送UDP数据包*/
                channel.send(buffer, new InetSocketAddress(opponent.getIp(), opponent.getPort()));
                System.out.println("send to " + opponent);
                System.out.println("Send successful");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void setOpponent(String sender) {
        for (User u : NioClient.getList()) {
            if (u.unique().compareTo(sender) == 0) {
                opponent = u;
            }
        }
    }

    public int getPort() {
        return port;
    }

}
