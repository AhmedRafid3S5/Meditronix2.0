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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShopMenu implements Initializable {

   @FXML
   private TableColumn<Medicine, String> Dose;

   @FXML
   private TableColumn<Medicine, String> Expiry;

   @FXML
   public HBox HPanel;

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

   private AddMedicinePanel addMedicinePanel;
   public boolean addPanelOn;
   public boolean updatePanelOn;
   public boolean searchPanelOn;
   public Pane addMedicinePane;

   private final Tooltip indexTooltip = new Tooltip();

   public static final ShopMenu instance = new ShopMenu();
   ObservableList<Medicine> list = FXCollections.observableArrayList(
     //new Medicine("Lumona","125mg","20/12/25","Generic", 100.0F, 10.0F, 95.0F),
       //    new Medicine("Lexotanil","125mg","20/12/25","Specialized", 100.0F, 10.0F, 95.0F)
   );

   //Implementing singleton pattern so any class can access the observable list
   public static ShopMenu getInstance(){

      return instance;
   }



   //Returns the inventory list to any class requesting it
   public ObservableList<Medicine> getList(){
      return this.list;
   }

   public Connection getConnection(){return this.GlobalConnect;}

   public TableView<Medicine> getInventoryTable() {
      return this.inventoryTable;
   }

   public void addMedicineToList(Medicine medicine) {
      this.list.add(medicine);
   }

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      GlobalDB = new Database();
      GlobalConnect = GlobalDB.dbConnect();
      addMedicinePanel = new AddMedicinePanel();
      //addPanelOn = false; //set addPanel fxml flag to false on bootup

      //set all panel flags to false on initialization
      setPanelOn(false,false,false);
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


      //Transfer object reference from created ShopMenu class to static object
      instance.GlobalDB = this.GlobalDB;
      instance.inventoryTable = this.inventoryTable;
      instance.list = this.list;
      instance.rs = this.rs;
      instance.GlobalConnect = this.GlobalConnect;
      instance.HPanel = this.HPanel;
      instance.addMedicinePane = this.addMedicinePane;


      //set tooltips for buttons
      Tooltip updateTip = new Tooltip("Select a medicine before pressing button");

      Update.setTooltip(updateTip);
      Delete.setTooltip(updateTip);

      String idx = String.valueOf(inventoryTable.getSelectionModel().getSelectedIndex());

      inventoryTable.getSelectionModel().selectedItemProperty().addListener(
              (observable, oldValue, newValue) -> {
                 if (newValue != null) {
                    int selectedIndex = inventoryTable.getSelectionModel().getSelectedIndex();
                    indexTooltip.setText("Index: " + String.valueOf(selectedIndex));

                 } else {
                    indexTooltip.hide(); // Hide tooltip if selection is cleared
                 }
              }
      );

      inventoryTable.setTooltip(indexTooltip);

   }



   public void refreshList(){
      try {
         this.rs = GlobalDB.showInventory();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }

      try {
         this.list.clear();
         while (this.rs.next()) {
            // Create a new Medicine object from each row in the ResultSet
            Medicine medicine = new Medicine(rs);
            // Add the medicine object to the ObservableList
            this.list.add(medicine);
         }
      } catch (SQLException e) {
         // Handle potential SQLException here
         e.printStackTrace();
      }
      inventoryTable.setItems(list);
   }

   public void removePanel(){
      HPanel.getChildren().remove(addMedicinePane);
   }

   @FXML
   void removeFromInventory() throws SQLException {
      int selectID = inventoryTable.getSelectionModel().getSelectedIndex();
      Medicine m = inventoryTable.getSelectionModel().getSelectedItem();
      inventoryTable.getItems().remove(selectID);

      //Reflect change on the database
      //Database db = new Database();
      GlobalDB.deleteMedicine(m.getSerial_id(),GlobalConnect);
   }

   @FXML
   public void showAddPanel(ActionEvent event) {
      try {
         if(!updatePanelOn && !addPanelOn && !searchPanelOn) {

            //set visual button status
            Update.setStyle("-fx-background-color: linear-gradient(to bottom , #77EED8, rgba(131, 179, 234, 0.5));");
            Add.setStyle("-fx-background-color:#118ab2;");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addMedicinePanel.fxml"));
            addMedicinePane = fxmlLoader.load(); // Load content into a Pane

            // Add the Pane to the HBox
            HPanel.getChildren().add(addMedicinePane);

            // Adjust layout to accommodate the added Pane
            HPanel.layout();

            //setting flag values
            setPanelOn(false,true,false);
         }
         else {
            HPanel.getChildren().remove(addMedicinePane);
            Add.setStyle("-fx-background-color: linear-gradient(to bottom , #77EED8, rgba(131, 179, 234, 0.5));");

            //setting flag values
            setPanelOn(false,false,false);
         }

      } catch (Exception e) {
         System.out.println("Can't load add panel");
         e.printStackTrace(); // Add this for better error logging
      }
   }


   @FXML
   void showUpdatePanel(ActionEvent event){
      try {
         if(!updatePanelOn && !addPanelOn && !searchPanelOn) {

            //set visual button status
            Update.setStyle("-fx-background-color:#118ab2;");
            Add.setStyle("-fx-background-color: linear-gradient(to bottom , #77EED8, rgba(131, 179, 234, 0.5));");


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("updateMedicinePanel.fxml"));
            addMedicinePane = fxmlLoader.load(); // Load content into a Pane

            // Add the Pane to the HBox
            HPanel.getChildren().add(addMedicinePane);


            // Adjust layout to accommodate the added Pane
            HPanel.layout();

            //setting flag values
            setPanelOn(true,false,false);
         }
         else {
            HPanel.getChildren().remove(addMedicinePane);

            Update.setStyle("-fx-background-color: linear-gradient(to bottom , #77EED8, rgba(131, 179, 234, 0.5));");

            //setting flag values
            setPanelOn(false,false,false);
         }

      } catch (Exception e) {
         System.out.println("Can't load add panel");
         e.printStackTrace(); // Add this for better error logging
      }

   }

   @FXML
   public void cancelAddRequest(ActionEvent event)
   {
      HPanel.getChildren().remove(addMedicinePane);
   }

   private void setPanelOn(boolean updatePanel,boolean addPanel,boolean searchPanel)
   {
      //set local flags
      updatePanelOn = updatePanel;
      addPanelOn = addPanel;
      searchPanelOn = searchPanel;

      //set singleton flags
      instance.updatePanelOn = this.updatePanelOn;
      instance.addPanelOn = this.addPanelOn;
      instance.searchPanelOn = this.searchPanelOn;
   }

}
