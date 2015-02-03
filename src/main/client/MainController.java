package main.client;

import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.ChatClient;
import org.jivesoftware.smack.Chat;

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
    TabPane tabPane;

    ArrayList<FriendGroup> friendGroupArrayList;
    MessageHandler handler = new MessageHandler();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initApiVariables();
        populateAccordion();

        new Thread(() -> {
            try{
                while(true){
                    Thread.sleep(10_000);
                    populateAccordion();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }).start();

    }

    private void initApiVariables() {
        LolStatus status = new LolStatus();
        status.setStatusMessage("using custom shitty client");
        status.setRankedLeagueTier(LolStatus.Tier.CHALLENGER);
        status.setRankedLeagueDivision(LolStatus.Division.I);
        ChatClient.getApi().setStatus(status);

        ChatClient.getApi().addChatListener((friend, message) -> {
            System.out.println(friend.getName() + ": " + message);

            handler.handleMessage(friend, message);
        });

    }

    //TODO: clean dis shizz up
    private void populateAccordion() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                String openedPane = null;
                try {
                    openedPane = friendsListAccordion.getExpandedPane().getText();
                }catch (NullPointerException e){
                    System.out.println("expanded pane is null");
                }
                friendGroupArrayList = new ArrayList<>(ChatClient.getApi().getFriendGroups());
                friendsListAccordion.getPanes().clear();

                for (FriendGroup fg : ChatClient.getApi().getFriendGroups()) {
                    friendsListAccordion.getPanes().add(produceFriendGroupPane(fg));
                }
                if(openedPane!=null){
                    TitledPane toOpen;
                    for(TitledPane pane: friendsListAccordion.getPanes()){
                        if (pane.getText().equals(openedPane)){
                            friendsListAccordion.setExpandedPane(pane);
                        }
                    }

                }
            }
        });


    }

    private TitledPane produceFriendGroupPane(FriendGroup group) {
        List<String> friendList = new ArrayList<>();
        for (Friend f : group.getFriends()) {
            if (f.isOnline()) {
                friendList.add(f.getName());
            }
        }
        TitledPane pane = new TitledPane();
        ListView friendListView = new ListView(FXCollections.observableList(friendList));
        friendListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handler.openNewConversation(friendListView.getSelectionModel().getSelectedItem().toString());
            }
        });
        pane.setText(group.getName());
        pane.setContent(friendListView);

        return pane;
    }


    private class MessageHandler {
        private Map<String, MessagePaneController> activeMessagingList = new HashMap<>();

        public void handleMessage(Friend friend, String message) {
            if (activeMessagingList.containsKey(friend.getName())) {
                activeMessagingList.get(friend.getName()).handleMessage(message);
            } else {
                FXMLLoader loader = new FXMLLoader(ChatClient.class.getResource("resources/forms/MessagePane.fxml"));
                Pane root = null;
                try {
                    root = loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MessagePaneController controller = loader.getController();
                controller.initVariables(friend, message);
                activeMessagingList.put(friend.getName(), controller);

                Tab messagingPane = new Tab(friend.getName());
                messagingPane.setOnCloseRequest(event -> {
                    activeMessagingList.remove(friend.getName());
                });
                messagingPane.setContent(root);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tabPane.getTabs().add(messagingPane);
                    }
                });
            }
        }

        public void openNewConversation(String selectedFriend){
            Friend newFriend = ChatClient.getApi().getFriendByName(selectedFriend);
            handleMessage(newFriend, "Ready to receive messages.");
        }
    }
}
