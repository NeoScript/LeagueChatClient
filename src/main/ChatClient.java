package main;

import com.github.theholywaffle.lolchatapi.LolChat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Nasir on 1/14/2015.
 */

public class ChatClient extends Application {
    private static LolChat api;
    public static Image icon;
    @Override
    public void start(Stage primaryStage) throws Exception {
        icon = new Image("main/resources/icon.png");

        Parent root = FXMLLoader.load(getClass().getResource("resources/forms/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().setAll(icon);
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
        if(api!=null && api.isConnected()){
            api.disconnect();
        }

        System.exit(0);
    }


}
