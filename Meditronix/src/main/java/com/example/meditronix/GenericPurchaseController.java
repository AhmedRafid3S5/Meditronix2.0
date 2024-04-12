package com.example.meditronix;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class GenericPurchaseController implements Initializable {
    @FXML
    private TableView<Medicine> GenericTable;
    @FXML
    private TableColumn<Medicine, String> DosageColumn;

    @FXML
    private TableColumn<Medicine, String> NameColumn;

    @FXML
    private TableColumn<Medicine, Float> PriceColumn;

    @FXML
    private TableColumn<Medicine, Float> QuantityColumn;

    ObservableList<Medicine> list = FXCollections.observableArrayList();

    private Database GlobalDB;
    private Connection GlobalConnect;
    private ResultSet rs;

    @FXML
    private ComboBox<Integer> QuantityBox;

    @FXML
    private Button BackButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        GlobalDB = new Database();
        GlobalConnect = GlobalDB.dbConnect();
        NameColumn.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Name"));
        DosageColumn.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Dose"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Quantity"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Price"));
        try {
            rs = new Database().showGeneric();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            while (rs.next()) {
                // Create a new Medicine object from each row in the ResultSet
                Medicine medicine = new Medicine(rs);
                // Add the medicine object to the ObservableList
                list.add(medicine);
            }
        } catch (SQLException e) {
            // Handle potential SQLException here
            e.printStackTrace();
        }
        GenericTable.setItems(list);
        for (int i = 1; i <= 15; i++) {
            QuantityBox.getItems().add(i);
        }
      //  QuantityBox.getSelectionModel().selectFirst();
    }

    @FXML
    void BackButtonPressed(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }

}
