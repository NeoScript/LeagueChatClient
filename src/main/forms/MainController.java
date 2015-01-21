package main.forms;

import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import main.ClientLauncher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Nasir on 1/18/2015.
 */
public class MainController implements Initializable {
    @FXML
    Accordion friendsListAccordion;
    @FXML
    Button sendButton;

    ArrayList<FriendGroup> friendGroupArrayList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitStatus();
        populateAccordion();

        sendButton.setOnAction(e -> {
            System.out.println("send button pressed");
            System.out.println(ClientLauncher.getApi().getOnlineFriends());
        });
    }

    private void setInitStatus() {
        LolStatus status = new LolStatus();
        status.setStatusMessage("if u see this, my shitty client is working");
        ClientLauncher.getApi().setStatus(status);
    }

    private void populateAccordion() {
        friendGroupArrayList = new ArrayList<>(ClientLauncher.getApi().getFriendGroups());
        friendsListAccordion.getPanes().clear();

        for (FriendGroup fg : ClientLauncher.getApi().getFriendGroups()) {
            friendsListAccordion.getPanes().add(produceFriendGroupPane(fg));
        }

    }

    private TitledPane produceFriendGroupPane(FriendGroup group) {
        List<String> friendList = new ArrayList<>();
        for(Friend f: group.getFriends()){
            if(f.isOnline()){
                friendList.add(f.getName());
            }
        }
        TitledPane pane = new TitledPane();
        ListView friendListView = new ListView(FXCollections.observableList(friendList));
        pane.setText(group.getName());
        pane.setContent(friendListView);

        return pane;
    }

}
