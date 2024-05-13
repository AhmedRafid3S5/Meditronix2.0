package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.EventObject;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainScreen implements Initializable{

    @FXML
    private Button CustomerButton;
    @FXML
    private Label loginNotify;

    @FXML
    private Button DoctorButton;

    @FXML
    private Button ShopOwnerButton;

    @FXML
    private Button login;

    @FXML
    private PasswordField passWordInput;

    @FXML
    private Button signUp;

    @FXML
    private TextField userNameInput;
    @FXML
    private  String state;
    @FXML
    private Stage stage;
    @FXML
    private Parent root;
    @FXML
    private Scene scene;
    @FXML
    private Database db;
    @FXML
    private Connection con;
    @FXML
    public void doctorButtonPressed()
    {
        state = "doctor";
        DoctorButton.setStyle("-fx-border-color: #09178f;" +
                              "-fx-background-radius: 10;" +
                               "-fx-border-radius: 10");
        ShopOwnerButton.setStyle("-fx-background-radius: 10");
        CustomerButton.setStyle("-fx-background-radius: 10");
    }
    @FXML
    public void customerButtonPressed()
    {
        state = "customer";
        DoctorButton.setStyle("-fx-background-radius: 10");
        ShopOwnerButton.setStyle("-fx-background-radius: 10");
        CustomerButton.setStyle("-fx-border-color: #09178f;" +
                                "-fx-border-radius: 10;" +
                                "-fx-background-radius: 10");
    }
    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void shopButtonPressed(ActionEvent event) throws IOException {
        state = "pharmacist";
        DoctorButton.setStyle("-fx-background-radius: 10");
        ShopOwnerButton.setStyle("-fx-border-color: #09178f;" +
                                 "-fx-background-radius: 10;" +
                                 "-fx-border-radius: 10");
        CustomerButton.setStyle("-fx-background-radius: 10");
    }
    @FXML
    public void signUpButtonPressed(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignUpMenu.fxml")));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
        Database db = new Database();
        db.dbConnect();

    }
    @FXML
    public  void switchToShop(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }
 @FXML

    public  void switchToInventory(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ShopMenu.fxml")));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
        //Database db = new Database();
        //db.dbConnect();
    }

    public  void switchToDoctorMenu(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DoctorMenu.fxml")));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();

    }

@FXML
    public void login(ActionEvent event) throws IOException, SQLException {

        if(state == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Role not defined");
            alert.setHeaderText("Role not selected");
            alert.setContentText("Please select a role to sign in as");
            alert.showAndWait();
        }

        db = new Database();
        con = db.dbConnect();

        String username = userNameInput.getText();
        String password = passWordInput.getText();
        String returned_username;
        String returned_password;
        String role;

        String sql = "Select * From users where username = ? ";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,username);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            returned_username = rs.getString("username");
            returned_password = rs.getString("password");
            role = rs.getString("role");
        }
        else
        {
            userNameInput.setStyle("-fx-text-fill: red;");
            userNameInput.setText("Username does not exist or is invalid");
           // userNameInput.setStyle("-fx-text-fill: #0e0707;");
            return;
        }

        if(state.equals(role) && username.equals(returned_username) && password.equals(returned_password))
        {
            loginNotify.setStyle("-fx-text-fill: #36e036");
            loginNotify.setText("Access Granted!! Logging in.....");
            //user is verified and showed the corresponding screen
            if(state.equals("pharmacist"))
            {
                con.close();

                switchToInventory(event);
            }
            else if(state.equals("customer"))
            {
                con.close();

                //switch to customer scene

                switchToShop(event);
            }
            else if (state.equals("doctor"))
            {
                con.close();

                //switch to doctor scene
                switchToDoctorMenu(event);
            }
        }

        else
        {
            userNameInput.setStyle("-fx-text-fill: red;");
            userNameInput.setText("Credentials or requested role doesn't match");
            //userNameInput.setStyle("-fx-text-fill: #0e0707;");
            return;
        }




    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameInput.setStyle("-fx-text-fill: #0e0707;");
        userNameInput.setStyle("-fx-background-radius: 20");
    }
}

