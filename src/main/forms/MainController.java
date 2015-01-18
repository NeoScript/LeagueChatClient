package main.forms;

import com.github.theholywaffle.lolchatapi.LolChat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import main.ClientLauncher;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nasir on 1/18/2015.
 */
public class MainController implements Initializable {
    @FXML
    Accordion friendsListAccordion;
    @FXML
    Button sendButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sendButton.setOnAction(e -> {
            System.out.println("send button pressed");
            System.out.println(ClientLauncher.getApi().getOnlineFriends());
        });
    }


}
