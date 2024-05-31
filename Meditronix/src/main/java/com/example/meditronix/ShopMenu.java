package com.example.meditronix;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
   private TableColumn<Medicine, String> Status;

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
   @FXML
   private MenuItem stockSettings;


   @FXML
   private MenuItem logout;

   @FXML
   private MenuItem changeCredentials;

   private ResultSet rs;
   private Database GlobalDB;
   private Connection GlobalConnect;

   private int lowStockLimit;

   private AddMedicinePanel addMedicinePanel;
   public boolean addPanelOn,updatePanelOn,searchPanelOn;

   public Pane addMedicinePane;

   private final Tooltip indexTooltip = new Tooltip();

   public static final ShopMenu instance = new ShopMenu();
   ObservableList<Medicine> list = FXCollections.observableArrayList(
     //new Medicine("Lumona","125mg","20/12/25","Generic", 100.0F, 10.0F, 95.0F),
       //    new Medicine("Lexotanil","125mg","20/12/25","Specialized", 100.0F, 10.0F, 95.0F)
   );

   //Implementing singleton pattern so any class can access the Inventory properties
   public static ShopMenu getInstance(){
      return instance;
   }

   public int getLowStockLimit(){return instance.lowStockLimit;}
   public void setLowStockLimit(int value){
      instance.lowStockLimit = value;
   }


   //A styling function to return buttons to previous visual state
   String buttonStyling(){
      String style = "-fx-background-color:\n" +
              "            linear-gradient(#00d9ff, #5c00be),\n" +
              "            linear-gradient(#ffeedd 0%, #ffeedd 20%, #66e3ff 100%),\n" +
              "            linear-gradient(#ffeedd 0%, #ffeedd 20%, #88ffff 100%),\n" +
              "            linear-gradient(to bottom left, #88c1e0 , #ffe4e4 );\n" +
              "\n" +
              "    -fx-background-insets: 0,1,2,3;\n" +
              "        -fx-background-radius: 20,15,15,15;\n" +
              "        -fx-border-color: white;\n" +
              "        -fx-border-radius: 20;\n" +
              "        -fx-text-fill: black;\n" +
              "        -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.6) , 4,0,0,1 );";

      return style;

   }

   TableColumn<Medicine, LocalDate> dateAddedColumn = new TableColumn<>("Date Added");

   private void setupDateAddedColumn() {
      dateAddedColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
   }


   //Returns the inventory list to any class requesting it
   public ObservableList<Medicine> getList(){
      return this.list;
   }

   public Connection getConnection(){return this.GlobalConnect;}

   public Database getGlobalDB(){return this.GlobalDB;}

   public TableView<Medicine> getInventoryTable() {
      return this.inventoryTable;
   }

   public void addMedicineToList(Medicine medicine) {
      this.list.add(medicine);
   }

   private void animateButton(Button button) {
      // Create a scale transition
      ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
      st.setByX(-0.08); // Scale by 10% in X direction
      st.setByY(-0.08); // Scale by 10% in Y direction
      st.setAutoReverse(true); // Automatically reverse the animation
      st.setCycleCount(2); // Play forwards and then backwards

      // Play the animation
      st.play();
   }

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {


      GlobalDB = new Database();
      GlobalConnect = GlobalDB.dbConnect();
      addMedicinePanel = new AddMedicinePanel();


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
      Status.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Status"));
      dateAddedColumn.setCellValueFactory(new PropertyValueFactory<Medicine,LocalDate>("serial_id"));


      try {
         rs = GlobalDB.showInventory();
         //get low stock limit marker
         lowStockLimit = GlobalDB.fetchLowStockValue(GlobalConnect);
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
      // Row coloring based on quantity
      detectStockEmpty();


      //Transfer object reference from created ShopMenu class to static object
      instance.GlobalDB = this.GlobalDB;
      instance.inventoryTable = this.inventoryTable;
      instance.list = this.list;
      instance.rs = this.rs;
      instance.GlobalConnect = this.GlobalConnect;
      instance.HPanel = this.HPanel;
      instance.addMedicinePane = this.addMedicinePane;
      instance.lowStockLimit = this.lowStockLimit;


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

     //--------------------------Tool tip ends ---------------------------------------------------

   }

   public  void changeHoverStyle(Button button) {
      button.hoverProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue) {
            // Mouse is hovering over the button
            button.setStyle("-fx-background-color: #118ab2;"); // Change the color when hovering
            // You can also set other style properties or effects if needed
         }else {
            if(updatePanelOn)
               Update.setStyle("-fx-background-color: #118ab2;");
            else if(addPanelOn)
               Add.setStyle("-fx-background-color: #118ab2;");
            else if(searchPanelOn)
               Search.setStyle("-fx-background-color: #118ab2;");
            else
               button.setStyle(""); // Default style
         }
      });
   }

   //Utility function to compare 2 dates
   boolean medExpired(String expiryDate)
   {
      LocalDate currentDate = LocalDate.parse(GlobalDB.currentDate());
      LocalDate medExpiryDate = LocalDate.parse(expiryDate);

      return currentDate.isAfter(medExpiryDate);
   }

   //Highlight rows which have expired or low on stock
   public void detectStockEmpty()
   {
      inventoryTable.setRowFactory(tv -> new TableRow<Medicine>() {
         @Override
         protected void updateItem(Medicine item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
               setStyle("");
            } else {
               //Set priority with higher priority given to expiry date,then to stock level
               // If quantity is 0, color the row red
               if(medExpired(item.getExpiry())){
                  item.setStatus("Expired!");
                  setStyle("-fx-background-color: #c94059;-fx-font-weight: bold;");
               }
               else if (item.getQuantity() == 0) {
                  item.setStatus("Out of Stock");

                  setStyle("-fx-background-color: #8a61bd;-fx-font-weight: bold;");
               }
               else if (item.getQuantity() <= instance.lowStockLimit){
                  item.setStatus("Low Stock");
                  setStyle("-fx-background-color: #d2b939;-fx-font-weight: bold;");
               }
               else {
                  item.setStatus("Valid");
                  setStyle(""); // Default style
               }
            }
         }
      });
   }


   @FXML
   public void searchClicked(ActionEvent event) throws IOException {

       animateButton(Search);

      if(!updatePanelOn && !addPanelOn && !searchPanelOn)
      {
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchPanel.fxml"));
         addMedicinePane = fxmlLoader.load();
         // Add the Pane to the HBox
         HPanel.getChildren().add(addMedicinePane);

         // Adjust layout to accommodate the added Pane
         HPanel.layout();

         displaySearchResults();
         setPanelOn(false,false,true);
      }
      else
      {
         HPanel.getChildren().remove(addMedicinePane);
         exitSearchMode();
         refreshList();
         setPanelOn(false,false,false);
      }
   }


   public void displaySearchResults() {
      int nameColumnIndex = inventoryTable.getColumns().indexOf(Name);  // Get 'Name' column index
      int dateAddedColumnIndex = nameColumnIndex + 1;

      if (!inventoryTable.getColumns().contains(dateAddedColumn)) {
         dateAddedColumn.setMinWidth(0);  // Start with 0 width
         dateAddedColumn.setPrefWidth(0);
         inventoryTable.getColumns().add(dateAddedColumnIndex, dateAddedColumn);

         // Create a timeline to animate the width of the column
         Timeline timeline = new Timeline();
         KeyValue kv = new KeyValue(dateAddedColumn.prefWidthProperty(), 120); // Assume 120 is the target width
         KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
         timeline.getKeyFrames().add(kf);
         timeline.play();
      }
   }


   public void exitSearchMode() {
      if (inventoryTable.getColumns().contains(dateAddedColumn)) {
         // Animation to reduce the width of the column before removing it
         Timeline timeline = new Timeline(
                 new KeyFrame(Duration.ZERO, new KeyValue(dateAddedColumn.prefWidthProperty(), dateAddedColumn.getWidth())),
                 new KeyFrame(Duration.millis(300), new KeyValue(dateAddedColumn.prefWidthProperty(), 0))
         );
         timeline.setOnFinished(event -> inventoryTable.getColumns().remove(dateAddedColumn));
         timeline.play();
      }
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


     detectStockEmpty();


   }

   public void populateWithNewList( ObservableList<Medicine> Newlist){
      this.list.clear();
      this.list = Newlist;
      inventoryTable.setItems(list);
      detectStockEmpty();
   }

   //Delete function to remove an item from list and database
   @FXML
   void removeFromInventory() throws SQLException {

      animateButton(Delete);

      int selectID = inventoryTable.getSelectionModel().getSelectedIndex();

      if(selectID >= 0 ) {
         Medicine m = inventoryTable.getSelectionModel().getSelectedItem();
         inventoryTable.getItems().remove(selectID);

         //Reflect change on the database
         //Database db = new Database();
         GlobalDB.deleteMedicine(m.getSerial_id(), GlobalConnect);
      }
      else
      {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("Error");
         alert.setHeaderText("No item selected");
         alert.setContentText("Please select a medicine from the list to delete.");
         alert.showAndWait();
      }
   }



   @FXML
   public void showAddPanel(ActionEvent event) {
      animateButton(Add);
      try {
         //changeHoverStyle(Update);
         //changeHoverStyle(Add);
         if(!updatePanelOn && !addPanelOn && !searchPanelOn) {

            //set visual button status
            //Update.setStyle(buttonStyling());
            //Add.setStyle("-fx-background-color:#118ab2;");


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
            //Add.setStyle(buttonStyling());

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
      animateButton(Update);
      try {
         int selected_index = inventoryTable.getSelectionModel().getSelectedIndex();
         //changeHoverStyle(Update);
         //changeHoverStyle(Add);
         if(!updatePanelOn && !addPanelOn && !searchPanelOn && selected_index>= 0) {

            //set visual button status
            //Update.setStyle("-fx-background-color:#118ab2;");
            //Add.setStyle(buttonStyling());


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

            //Update.setStyle(buttonStyling());

            //setting flag values
            setPanelOn(false,false,false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No item selected");
            alert.setContentText("Please select a medicine from the list to update.");
            alert.showAndWait();
         }

      } catch (Exception e) {
         System.out.println("Can't load add panel");
         e.printStackTrace(); // Add this for better error logging
      }

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

   @FXML
   void onLowStockSettingClicked(ActionEvent event) throws IOException {
      stockSettingController settingController = new stockSettingController();
      settingController.onLowStockSettingClicked(event);
   }

   @FXML
   void onHowToUseClicked(ActionEvent event) throws IOException{
      HelpWindow helpWindow = new HelpWindow();
      helpWindow.onHowToUseClicked(event);
   }


   @FXML
   void logout(ActionEvent event) throws IOException{
// Load the new FXML file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
      Parent root = loader.load();

      // Get the current stage from the event's source
      Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

      // Create a new scene with the loaded root
      Scene scene = new Scene(root);

      // Set the new scene on the stage
      stage.setScene(scene);
      stage.show();
   }

}
