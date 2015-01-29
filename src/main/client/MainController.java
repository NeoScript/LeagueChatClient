package main.client;

import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import main.ChatClient;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Nasir on 1/18/2015.
 */
public class MainController implements Initializable {
    @FXML
    Accordion friendsListAccordion;
    @FXML
    Button sendButton;
    @FXML
    TabPane tabPane;

    ArrayList<FriendGroup> friendGroupArrayList;
    Map<Friend, Tab> chatPaneMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initApiVariables();
        populateAccordion();

        sendButton.setOnAction(e -> {
            System.out.println("send button pressed");
            System.out.println(ChatClient.getApi().getOnlineFriends());
        });
    }

    private void initApiVariables(){
        LolStatus status = new LolStatus();
        status.setStatusMessage("if u see this, my shitty client is working");
        ChatClient.getApi().setStatus(status);

        ChatClient.getApi().addChatListener((friend, message) -> {
            System.out.println(friend.getName()+": "+message);

            if(!chatPaneMap.containsKey(friend)){
                Tab messagingPane = new Tab(friend.getName());
                Pane content = produceMessagingPane(friend);
                messagingPane.setContent(content);
                chatPaneMap.put(friend, messagingPane);
                Platform.runLater(() -> tabPane.getTabs().add(messagingPane));
            }
            /*Scanner keyboard = new Scanner(System.in);
            String msg = keyboard.nextLine();
            friend.sendMessage(msg);*/
        });

    }

    private void populateAccordion() {
        friendGroupArrayList = new ArrayList<>(ChatClient.getApi().getFriendGroups());
        friendsListAccordion.getPanes().clear();

        for (FriendGroup fg : ChatClient.getApi().getFriendGroups()) {
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
    private Pane produceMessagingPane(Friend f) {
        FXMLLoader loader = new FXMLLoader(ChatClient.class.getResource("forms/MessagePane.fxml"));
        Pane root = new Pane();
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessagePaneController controller = loader.getController();
        controller.setFriend(f);
        return root;
    }
}
