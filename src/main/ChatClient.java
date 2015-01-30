package main;

import com.github.theholywaffle.lolchatapi.LolChat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Nasir on 1/14/2015.
 */

public class ChatClient extends Application {
    private static LolChat api;
    public static final Image icon = new Image("main/resources/icon.png");
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/forms/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().setAll(icon);
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
        api.disconnect();
        System.exit(0);
    }


}
