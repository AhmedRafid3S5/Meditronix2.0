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


        try {
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
        Pane questionPane = null; // Load content into a Pane

            questionPane = fxmlLoader1.load();



        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
        Pane questionPane2 = null; // Load content into a Pane

            questionPane2 = fxmlLoader2.load();


        FXMLLoader fxmlLoader3 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
        Pane questionPane3 = null; // Load content into a Pane

            questionPane3 = fxmlLoader3.load();

            FXMLLoader fxmlLoader4 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
            Pane questionPane4 = null; // Load content into a Pane

            questionPane4 = fxmlLoader4.load();

            FXMLLoader fxmlLoader5 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
            Pane questionPane5 = null; // Load content into a Pane

            questionPane5 = fxmlLoader5.load();

            FXMLLoader fxmlLoader6 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
            Pane questionPane6 = null; // Load content into a Pane

            questionPane6= fxmlLoader6.load();


            FXMLLoader fxmlLoader7 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
            Pane questionPane7 = null; // Load content into a Pane

            questionPane7= fxmlLoader7.load();

            FXMLLoader fxmlLoader8 = new FXMLLoader(getClass().getResource("infoPane.fxml"));
            Pane questionPane8 = null; // Load content into a Pane

            questionPane8= fxmlLoader8.load();


        //Retrieve controller instances of each pane
        InfoPaneController FAQ1 = fxmlLoader1.getController();
        InfoPaneController FAQ2 = fxmlLoader2.getController();
        InfoPaneController FAQ3 = fxmlLoader3.getController();
        InfoPaneController FAQ4 = fxmlLoader4.getController();
        InfoPaneController FAQ5 = fxmlLoader5.getController();
        InfoPaneController FAQ6 = fxmlLoader6.getController();
        InfoPaneController FAQ7 = fxmlLoader7.getController();
        InfoPaneController FAQ8 = fxmlLoader8.getController();

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

        FAQ4.setParent(this);
        FAQ4.setQuestion("How is the status determined?");
        FAQ4.setAnswer("Status is based on priority. 1st priority is to check if medicine has valid expiry" +
                " If valid, the stock is check to see if it is below a threshold" +
                " If below a threshold we check if the quantity is 0 or not.");

        FAQ5.setParent(this);
        FAQ5.setQuestion("Can I set a custom Low stock value?");
        FAQ5.setAnswer("Absolutely! Go to Settings -> Set Low Stock Value and type in a new limit. This limit is also saved.");

        FAQ6.setParent(this);
        FAQ6.setQuestion("How does the Add function work?");
        FAQ6.setAnswer("Any new medicine you type in is cross matched with the existing inventory. If a newly added medicine has the " +
                "same dose,expiry, type and name as an existing one, the added medicine is added to the existing record . Do keep " +
                "in mind that the price will be equal to the new price of the added medicine." +
                "  If no such record exists, this medicine will be considered a new entry and added as a new row.");


            FAQ7.setQuestion("How updating works");
            FAQ7.setAnswer("Fist select a medicine from the table you want to update. You can click on the medicin and hover the cursor" +
                    " to reveal its current index in the inventory. Then click on update to modify any data. Want to continue updating? " +
                    "You can type in the index and update the specific medicine. To exit, click on the update button again.");
            FAQ7.setParent(this);

            FAQ8.setQuestion("How to use the search panel");
            FAQ8.setAnswer("Click on the search button. Then select the type of search you want to perform. " +
                    "In Search by name mode, type an uppercase letter to find medicines starting with those characters " +
                    "Any lowercase letter search will search for medicines with that substring. " +
                    "You can see the date a medicine was added in and search medicines by name, date, dose or combinations there of."
            );
            FAQ8.setParent(this);
        // Add the Pane to the VBox
        helpBox.getChildren().addAll(questionPane,questionPane2,questionPane3,
                                     questionPane4,questionPane5,questionPane6,questionPane7,questionPane8);

        // Adjust layout to accommodate the added Pane
        helpBox.layout();
            scrollField.setContent(helpBox);
            scrollField.layout();
            updatePositions();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
