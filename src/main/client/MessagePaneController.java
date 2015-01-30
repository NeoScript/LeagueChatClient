package main.client;

import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.text.IconView;
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

    public void initVariables(Friend f, String firstMessage) {
        currFriend = f;
        Platform.runLater(() -> messageListView.getItems().add(f.getName() + ": " + firstMessage));
    }

    private void sendButtonClicked() {
        String chatMessage = inputField.getText();
        if (!chatMessage.isEmpty() && currFriend != null) {
            currFriend.sendMessage(chatMessage);
            Platform.runLater(() -> {
                messageListView.getItems().add("Me: " + chatMessage);
                inputField.clear();
            });

        }
    }

    public void handleMessage(String message) {
        Platform.runLater(() -> {
            messageListView.getItems().add(currFriend.getName() + ": " + message);
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.toFront();
            if(!stage.isFocused()){
                stage.requestFocus();
            }
        });
    }


}
