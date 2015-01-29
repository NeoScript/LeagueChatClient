package main.client;

import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nasir on 1/27/2015.
 */
public class MessagePaneController implements Initializable {
    @FXML
    private TextField inputField;
    @FXML
    private ListView messageListView;
    @FXML
    private Button sendButton;

    private Friend currFriend;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendButton.setOnAction(e -> sendButtonClicked());
        inputField.setOnAction(e -> sendButton.fire());
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                sendButton.setDisable(true);
            } else {
                sendButton.setDisable(false);
            }
        });
    }

    public void setFriend(Friend f) {
        currFriend = f;

        currFriend.setChatListener((friend, message) -> {
            System.out.println(friend.getName().concat(":::").concat(message));
            messageListView.getItems().add(friend.getName() + ": " + message);
        });

    }

    private void sendButtonClicked() {
        String chatMessage = inputField.getText();
        if (!chatMessage.isEmpty() && currFriend != null) {
            currFriend.sendMessage(chatMessage);
            messageListView.getItems().add("Me: " + chatMessage);
        }
    }
}
