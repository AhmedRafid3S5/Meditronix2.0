package com.example.meditronix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShopMenu.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 760, 510);

        //For dev, use this entry point
        Scene scene = new Scene(fxmlLoader.load(), 957, 722);
        String css = "src/main/resources/com/example/meditronix/ShopInventory.css";
        scene.getStylesheets().add(css);
        stage.setTitle("MEDITRONIX");
        stage.setScene(scene);
        stage.show();
        Database db = new Database();
        db.dbConnect();
    }

    public static void main(String[] args) {
        launch();
    }
}