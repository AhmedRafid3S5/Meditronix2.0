package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpWindow implements Initializable {
    @FXML
    private VBox helpBox;

    @FXML
    private ScrollPane scrollField;

    private double previousHeight = 0; // Track total height for positioning

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
        Pane questionPane = null; // Load content into a Pane
        try {
            questionPane = fxmlLoader1.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
        Pane questionPane2 = null; // Load content into a Pane
        try {
            questionPane2 = fxmlLoader2.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader3 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
        Pane questionPane3 = null; // Load content into a Pane
        try {
            questionPane3 = fxmlLoader3.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Retrieve controller instances of each pane
        InfoPaneController FAQ1 = fxmlLoader1.getController();
        InfoPaneController FAQ2 = fxmlLoader2.getController();
        InfoPaneController FAQ3 = fxmlLoader3.getController();

        //Add questions and answers
        FAQ1.setQuestion("How to delete a medicine from inventory?");
        FAQ1.setAnswer("Simple!" +
                "Just click on a medicine you want to delete from the table and then press Delete.");
        FAQ1.setParent(this);
        FAQ2.setQuestion("Where is my inventory stored?");
        FAQ2.setAnswer("The database is stored on the cloud with maximum security");
        FAQ2.setParent(this);
        FAQ3.setParent(this);
        FAQ3.setQuestion("What do the highlights represent?");
        FAQ3.setAnswer("Red means medicine has expired" +
                       "Yellow indicates that this medicine is low on stock, so better stock up!" +
                       "Purple indicated that this medicine is out of stock");
        // Add the Pane to the VBox
        helpBox.getChildren().addAll(questionPane,questionPane2,questionPane3);

        // Adjust layout to accommodate the added Pane
        helpBox.layout();
        updatePositions();

        // Update positions initially

    }

    public void updatePositions() {
        Node firstChild = helpBox.getChildren().get(0);
        firstChild.setTranslateY(0);
        double currentHeight = 0;
        for (int i = 1; i < helpBox.getChildren().size(); i++) {
            Node child = helpBox.getChildren().get(i);
            Node prevChild = helpBox.getChildren().get(i-1);
            currentHeight += prevChild.prefHeight(-1)/10; // Get actual height with content
            child.setTranslateY(currentHeight); // Set Y position based on previous height
        }
    }

    @FXML
    void onHowToUseClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("helpWindow.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 760, 510);

        //For dev, use this entry point
        Scene scene = new Scene(fxmlLoader.load(), 700,500);

        InputStream inputStream = getClass().getResourceAsStream("images/settingsIcon.png");
        Image image = new Image(inputStream);
        Stage stage = new Stage();
        stage.getIcons().add(image);
        stage.setTitle("FAQs");
        stage.setScene(scene);
        stage.show();


    }
}
