package com.example.meditronix;

import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoPaneController implements Initializable {
    @FXML
    private TextField answer;

    @FXML
    private Label question;
    @FXML
    private VBox FAQBox;
    @FXML
    private ImageView showHideImage;


    private  boolean buttonPressed;



    @FXML
    private Button showHideButton;

    public HelpWindow helpWindow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonPressed = false;
        answer = new TextField();
        answer.setPrefWidth(700); // Set answer label width
        answer.setStyle("-fx-border-radius: 10;" +
                "-fx-border-color: #5413b6;"+
                "-fx-background-radius: 10;" +
                "-fx-font-size: 14"
        ); // Add padding to answer label

        answer.setVisible(false); // Initially hide answer
        //answer.setWrapText(true);
    }

    @FXML
    public void onButtonClicked(ActionEvent event){

        if(!buttonPressed) {
            answer.setVisible(true);
            //add transition animation
            FAQBox.getChildren().add(answer);
            FAQBox.setPrefHeight(FAQBox.getPrefHeight()+answer.getHeight());
           // FAQBox.setPrefHeight(FAQBox.getPrefHeight()+answer.getPrefHeight());
            buttonPressed = true;
            InputStream inputStream = getClass().getResourceAsStream("images/icons8-collapse-arrow-64.png");
            Image image = new Image(inputStream);
            showHideImage.setImage(image);
            helpWindow.updatePositions();
        }
        else if(buttonPressed){
            answer.setVisible(false);
            //add transition animation
            FAQBox.getChildren().remove(answer);
            FAQBox.setPrefHeight(FAQBox.getPrefHeight()-answer.getHeight());
            buttonPressed = false;
            InputStream inputStream = getClass().getResourceAsStream("images/icons8-down-arrow-64.png");
            Image image = new Image(inputStream);
            showHideImage.setImage(image);
            helpWindow.updatePositions();
        }

        System.out.println(FAQBox.getPrefHeight());
    }

    public void setQuestion(String Myquestion)
    {
        question.setText(Myquestion);
    }
    public void setAnswer(String myAnswer){
        answer.setText(myAnswer);
        answer.setEditable(false);
        //answer.setWrapText(true); // Ensure wrapText is enabled

    }

    public void setParent(HelpWindow helpWindow)
    {
        this.helpWindow = helpWindow;
    }

}
