package com.nioclient.pane;

import com.protocol.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author GOLD
 */
public class ClientSend extends VBox {

    private Label ipaddress = new Label("IP:");
    private Label name = new Label("User:");
    private Label lport = new Label("Port:");
    private Label online = new Label("Opponent:");
    private Label color = new Label("Choose Color:");
    private TextField textip = new TextField(null);
    private TextField textname = new TextField(null);
    private TextField textlport = new TextField(null);
    private ListView<User> list = new ListView<>();
    private ComboBox<String> cbo = new ComboBox<>();
    private Button connect = new Button("Connect");
    private Button battle = new Button("Battle");
    private ObservableList<User> items = FXCollections.observableArrayList();

    public ClientSend() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        textip.setMaxWidth(130);
        textip.setEditable(false);
        textname.setMaxWidth(130);
        textlport.setMaxWidth(90);
        textlport.setEditable(false);
        grid.add(ipaddress, 0, 0);
        grid.add(textip, 1, 0);
        grid.add(name, 0, 1);
        grid.add(textname, 1, 1);
        grid.add(lport, 2, 1);
        grid.add(textlport, 3, 1);
        list.setMaxSize(350, 170);
        list.setMinSize(350, 170);
        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cbo.getItems().add("White");
        cbo.getItems().add("Black");
        cbo.setPrefWidth(150);
        cbo.setValue("");
        connect.setMaxSize(100, 30);
        connect.setMinSize(100, 30);
        battle.setMaxSize(100, 30);
        battle.setMinSize(100, 30);
        HBox h = new HBox();
        h.setSpacing(100);
        h.setPadding(new Insets(20, 0, 20, 0));
        h.getChildren().addAll(color, cbo);
        HBox h1 = new HBox();
        h1.setSpacing(150);
        h1.setPadding(new Insets(10, 0, 10, 0));
        h1.getChildren().addAll(connect, battle);
        this.getChildren().addAll(grid, online, list, h, h1);
        this.setSpacing(20);
    }

    public TextField getTextip() {
        return textip;
    }

    public TextField getTextname() {
        return textname;
    }

    public TextField getTextlport() {
        return textlport;
    }

    public ListView<User> getList() {
        return list;
    }

    public ComboBox<String> getCbo() {
        return cbo;
    }

    public Button getConnect() {
        return connect;
    }

    public Button getBattle() {
        return battle;
    }

    public ObservableList<User> getItems() {
        return items;
    }

}
