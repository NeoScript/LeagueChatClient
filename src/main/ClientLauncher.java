package main;

import com.github.theholywaffle.lolchatapi.LolChat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nasir on 1/14/2015.
 */

public class ClientLauncher extends Application {
    private static LolChat api;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("forms/Login.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args){
        launch(args);
    }

    public static LolChat getApi(){
        return api;
    }
    public static void setApi(LolChat lolChat){
         api = lolChat;
    }
    @Override
    public void stop(){
        System.exit(0);
    }
}
