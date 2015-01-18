package main.forms;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.ClientLauncher;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Nasir on 1/14/2015.
 * This is the Controller Class for the Login page
 * TODO: Hook up all functionality to buttons
 */
public class LoginController implements Initializable {
    @FXML
    public TextField userNameField;
    @FXML
    PasswordField passwordField;
    @FXML
    ComboBox<String> serverDropDownButton;
    @FXML
    Button loginButton;

    private LolChat api;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDropDownButtonItems();

        userNameField.setOnAction(e -> {
            passwordField.requestFocus();
        });

        passwordField.setOnAction(e -> {
            if (serverDropDownButton.getSelectionModel().getSelectedItem() != null) {
                loginButton.requestFocus();
                loginButton.fire();
            } else {
                serverDropDownButton.requestFocus();
                serverDropDownButton.show();
            }

        });
        loginButton.setOnAction(e -> {
            try {
                loginButtonPressed();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    private void setDropDownButtonItems() {
        ArrayList<String> itemList = new ArrayList<String>();

        for (ChatServer item : ChatServer.values()) {
            itemList.add(item.toString().toUpperCase());
        }
        serverDropDownButton.getItems().addAll(itemList);
    }

    private void loginButtonPressed() throws Exception{
        System.out.println("pressed login");
        String selectedServer = serverDropDownButton.getSelectionModel().getSelectedItem();
        api = new LolChat(determineServer(selectedServer), FriendRequestPolicy.MANUAL);
        if(api.login(userNameField.getText(),passwordField.getText(), true)){
            System.out.println("connected");
            for(Friend f: api.getFriends()){
                System.out.println(f.getName());


            }
        }

        //TODO: close this window and open next one
    }

    private ChatServer determineServer(String selection) {
        for (ChatServer cs : ChatServer.values()) {
            if (cs.name().equalsIgnoreCase(selection)) {
                return cs;
            }
        }
        return null;
    }
}
