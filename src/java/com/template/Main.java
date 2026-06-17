package com.template;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/template/LoginUI.fxml"));
        stage.setScene(new Scene(root, 900, 650));
        stage.setTitle("TAK LIMITED");
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}