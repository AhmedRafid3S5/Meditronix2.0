package com.example.meditronix;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ShopMenu implements Initializable {

   @FXML
   private TableColumn<Medicine, String> Dose;

   @FXML
   private TableColumn<Medicine, String> Expiry;

   @FXML
   private HBox HPanel;

   @FXML
   private TableColumn<Medicine, String> Name;

   @FXML
   private TableColumn<Medicine, Float> Price;

   @FXML
   private TableColumn<Medicine, Float> Quantity;

   @FXML
   private TableColumn<Medicine, String > Type;

   @FXML
   private TableColumn<Medicine, Float> UnitCost;

   @FXML
   private TableView<Medicine> inventoryTable;

   @FXML
   private VBox vpanel;

   @FXML
   private Button Add;

   @FXML
   private Button Delete;

   @FXML
   private Button Update;
   @FXML
   private Button Search;

   private ResultSet rs;
   private Database GlobalDB;
   private Connection GlobalConnect;


   ObservableList<Medicine> list = FXCollections.observableArrayList(
     //new Medicine("Lumona","125mg","20/12/25","Generic", 100.0F, 10.0F, 95.0F),
       //    new Medicine("Lexotanil","125mg","20/12/25","Specialized", 100.0F, 10.0F, 95.0F)
   );
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      GlobalDB = new Database();
      GlobalConnect = GlobalDB.dbConnect();
      //Format
      //tableColumnName.setCellValueFactory(new PropertyValueFactory<Class name,Data type>("Class attribute name"));
      Name.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Name"));
      Dose.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Dose"));
      Quantity.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Quantity"));
      Type.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Type"));
      UnitCost.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("UnitCost"));
      Price.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("price"));
      Expiry.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Expiry"));

      try {
         rs = new Database().showInventory();
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
      inventoryTable.setItems(list);
   }

   @FXML
   void removeFromInventory() throws SQLException {
      int selectID = inventoryTable.getSelectionModel().getSelectedIndex();
      Medicine m = inventoryTable.getSelectionModel().getSelectedItem();
      inventoryTable.getItems().remove(selectID);

      //Reflect change on the database
      Database db = new Database();
      db.deleteMedicine(m.getSerial_id(),GlobalConnect);
   }
}
