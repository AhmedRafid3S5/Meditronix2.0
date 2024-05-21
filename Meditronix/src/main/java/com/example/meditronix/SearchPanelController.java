package com.example.meditronix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class SearchPanelController implements Initializable {
    @FXML
    private TextField dosageField;

    @FXML
    private Button searchButton;

    @FXML
    private DatePicker searchDateField;

    @FXML
    private Label searchMatchStatus;

    @FXML
    private TextField searchNameField;

    @FXML
    private AnchorPane searchPanel;

    @FXML
    private ChoiceBox<String> searchType;

    @FXML
    private Spinner<String> unitSelector;


    //non FXML variables
    private final String[] types = {"Name","Date added","Name & Dose","Name,Date & Dose"};
    private final String[] units = {"g","mg","ug","L","mL"};

    private String searchMode;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ObservableList<String> Units = FXCollections.observableArrayList(
            Arrays.asList(units)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchType.getItems().addAll(types);
        searchType.setOnAction(this::returnSearchType);

        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(
              Units
        );

        // Set the factory to the spinner
        unitSelector.setValueFactory(valueFactory);

        // Set the default value (optional)
        unitSelector.getValueFactory().setValue(Units.get(0));

        // Add change listener
        unitSelector.valueProperty().addListener((obs, oldValue, newValue) -> {
            // Your action here
            System.out.println("Selected unit: " + newValue);
        });


        //Inititalize with all text fields & unit selector disabeled
        searchDateField.setDisable(true);
        dosageField.setDisable(true);
        unitSelector.setDisable(true);
        searchNameField.setDisable(true);

    }

    private void returnSearchType(ActionEvent event) {
        searchMode = searchType.getValue();
        searchMatchStatus.setText("");
        if(Objects.equals(searchMode, types[0]))
        {
            searchNameField.setDisable(false);
            searchDateField.setDisable(true);
            dosageField.setDisable(true);
            unitSelector.setDisable(true);
        }

        else if(Objects.equals(searchMode,types[1]))
        {
            searchNameField.setDisable(true);
            dosageField.setDisable(true);
            unitSelector.setDisable(true);
            searchDateField.setDisable(false);
        }
        else if(Objects.equals(searchMode,types[2]))
        {
            searchDateField.setDisable(true);
            dosageField.setDisable(false);
            unitSelector.setDisable(false);
            searchNameField.setDisable(false);
        }
        else
        {
            searchDateField.setDisable(false);
            dosageField.setDisable(false);
            unitSelector.setDisable(false);
            searchNameField.setDisable(false);
        }
    }

    private void setWarnLabel(String warning){
        searchMatchStatus.setText(warning);

        //searchMatchStatus.setVisible(true);

        // Create a Timeline to hide the label after 3 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Hide the warning label after the specified duration
                        searchMatchStatus.setText("");
                    }
                })
        );

        // Play the timeline once to hide the label after 3 seconds
        timeline.setCycleCount(1);
        timeline.play();
    }

    private boolean emptyFieldAlert(){
        if(Objects.equals(searchMode, types[0]))
        {
            if(searchNameField.getText().isEmpty()) {
                setWarnLabel("Name field is empty! To search,please enter a medicine name");
                return true;
            }
        }
        else if(Objects.equals(searchMode, types[1]))
        {
            if(searchDateField.getValue() == null){
                setWarnLabel("Date field is empty! To search,please enter a entry date");
                return true;
            }
        }
        else if(Objects.equals(searchMode, types[2]))
        {
             if(searchNameField.getText().isEmpty() || dosageField.getText().isEmpty())
             {
                 setWarnLabel("Field(s) are empty! To search,please enter a medicine name & dose");
                 return true;
             }
        }
        else if(Objects.equals(searchMode, types[3]))
        {
            if(searchNameField.getText().isEmpty() || dosageField.getText().isEmpty() || searchDateField.getValue() == null)
            {
                setWarnLabel("Field(s) are empty! To search,please fill up all required search parameters");
                return true;
            }
        }
        return false;

    }

    @FXML
    public void onSearch(ActionEvent event){

        if(emptyFieldAlert()!= true) {
            String name = searchNameField.getText();

            ObservableList<Medicine> Templist = FXCollections.observableArrayList(

            );
            ResultSet rs;
            if (Objects.equals(searchMode, types[0])) {
                int matches = 0;
                try {
                    rs = ShopMenu.getInstance().getGlobalDB().searchByName(ShopMenu.getInstance().getConnection(), name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {

                    while (rs.next()) {
                        matches++;
                        // Create a new Medicine object from each row in the ResultSet
                        Medicine medicine = new Medicine(rs);
                        // Add the medicine object to the ObservableList
                        Templist.add(medicine);
                    }
                } catch (SQLException e) {
                    // Handle potential SQLException here
                    e.printStackTrace();
                }
                ShopMenu.getInstance().populateWithNewList(Templist);
                searchMatchStatus.setText(matches + " matche(s) found!");
            } else if (Objects.equals(searchMode, types[1])) {
                int matches = 0;
                String date;
                date = searchDateField.getValue().format(formatter);
                try {
                    rs = ShopMenu.getInstance().getGlobalDB().searchByDate(ShopMenu.getInstance().getConnection(), date);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {

                    while (rs.next()) {
                        matches++;
                        // Create a new Medicine object from each row in the ResultSet
                        Medicine medicine = new Medicine(rs);
                        // Add the medicine object to the ObservableList
                        Templist.add(medicine);
                    }
                } catch (SQLException e) {
                    // Handle potential SQLException here
                    e.printStackTrace();
                }
                ShopMenu.getInstance().populateWithNewList(Templist);
                searchMatchStatus.setText(matches + " matche(s) found!");
            } else if (Objects.equals(searchMode, types[2])) {
                int matches = 0;
                //Format the dose to concat the value with the unit
                String doseValue = dosageField.getText();
                String doseUnit = unitSelector.getValue();
                String dose = doseValue + doseUnit;
                try {
                    rs = ShopMenu.getInstance().getGlobalDB().searchByNameDose(ShopMenu.getInstance().getConnection(), name, dose);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {

                    while (rs.next()) {
                        matches++;
                        // Create a new Medicine object from each row in the ResultSet
                        Medicine medicine = new Medicine(rs);
                        // Add the medicine object to the ObservableList
                        Templist.add(medicine);
                    }
                } catch (SQLException e) {
                    // Handle potential SQLException here
                    e.printStackTrace();
                }
                ShopMenu.getInstance().populateWithNewList(Templist);
                searchMatchStatus.setText(matches + " matche(s) found!");
            } else if (Objects.equals(searchMode, types[3])) {
                int matches = 0;
                String date;
                date = searchDateField.getValue().format(formatter);
                //Format the dose to concat the value with the unit
                String doseValue = dosageField.getText();
                String doseUnit = unitSelector.getValue();
                String dose = doseValue + doseUnit;
                try {
                    rs = ShopMenu.getInstance().getGlobalDB().strictSearch(ShopMenu.getInstance().getConnection(), name, dose, date);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {

                    while (rs.next()) {
                        matches++;
                        // Create a new Medicine object from each row in the ResultSet
                        Medicine medicine = new Medicine(rs);
                        // Add the medicine object to the ObservableList
                        Templist.add(medicine);
                    }
                } catch (SQLException e) {
                    // Handle potential SQLException here
                    e.printStackTrace();
                }
                ShopMenu.getInstance().populateWithNewList(Templist);
                searchMatchStatus.setText(matches + " matche(s) found!");

            }
        }
    }




}
