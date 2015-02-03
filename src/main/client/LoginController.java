package main.client;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.ChatClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setDropDownButtonItems();
        loginButton.setDisable(true);

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

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (passwordField.getText()!= null && userNameField.getText()!= null){
                loginButton.setDisable(false);
            }
        });

        });
        loginButton.setOnAction(e -> {
            try {
                loginButtonPressed();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        try{
            loadDefaultLogin();
        }catch (IOException e){}

    }

    private void loadDefaultLogin() throws IOException {
        File loginInfo = new File("login.txt");
        if (loginInfo.exists()){
            Scanner scan = new Scanner(loginInfo);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        userNameField.setText(scan.nextLine());
                        passwordField.setText(scan.nextLine());
                        serverDropDownButton.getSelectionModel().select(scan.nextLine());
                        loginButton.setDisable(false);
                    }catch (Exception e){
                        userNameField.clear();
                        passwordField.clear();
                        serverDropDownButton.getSelectionModel().selectFirst();
                        loginButton.setDisable(true);
                    }
                }
            });

        }
    }

    private void setDropDownButtonItems() {
        ArrayList<String> itemList = new ArrayList<String>();

        for (ChatServer item : ChatServer.values()) {
            itemList.add(item.toString().toUpperCase());
        }
        serverDropDownButton.getItems().addAll(itemList);
    }

    private void loginButtonPressed() throws Exception {
        System.out.println("pressed login");
        String selectedServer = serverDropDownButton.getSelectionModel().getSelectedItem();
        ChatClient.setApi(new LolChat(determineServer(selectedServer), FriendRequestPolicy.MANUAL));

        if (ChatClient.getApi().login(userNameField.getText(), passwordField.getText(), true)) {
            System.out.println("connected");
            for (Friend f : ChatClient.getApi().getFriends()) {
                System.out.println(f.getName());
            }

            openMainStage();
        }

    }

    private void openMainStage() throws IOException {
        Stage parent = (Stage) loginButton.getScene().getWindow();
        parent.close();

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(ChatClient.class.getResource("resources/forms/Main.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.getIcons().setAll(ChatClient.icon);
        stage.show();

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
