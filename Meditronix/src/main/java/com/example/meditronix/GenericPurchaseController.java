package com.example.meditronix;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;



public class GenericPurchaseController implements Initializable {

    //Inventory Column
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


    // Codes for the Cart Table
    @FXML
    private TableView<Medicine> CartTable;
    @FXML
    private TableColumn<Medicine, String> CartNameColumn;
    @FXML
    private TableColumn<Medicine, String> CartDosageColumn;
    @FXML
    private TableColumn<Medicine, Float> CartQuantityColumn;
    @FXML
    private TableColumn<Medicine, Float> CartPriceColumn;

    @FXML
    private ComboBox<Integer> QuantityBox;
    @FXML
    private Button AddToCart;
    @FXML
    private Button deleteButton;

    @FXML
    private Label subtotalLabel;

    ObservableList<Medicine> cartList = FXCollections.observableArrayList();
    private Database GlobalDB;
    private Connection GlobalConnect;
    private ResultSet rs;

    @FXML
    private Button BackButton;
    @FXML
    private Button Checkout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        GlobalDB = new Database();
        GlobalConnect = GlobalDB.dbConnect();
        NameColumn.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Name"));
        DosageColumn.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Dose"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Quantity"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Price"));


        CartNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        CartDosageColumn.setCellValueFactory(new PropertyValueFactory<>("Dose"));
        CartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        CartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
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

        CartTable.setItems(cartList);
        for (int i = 1; i <= 15; i++) {
            QuantityBox.getItems().add(i);
        }
        QuantityBox.setValue(0);
        //  QuantityBox.getSelectionModel().selectFirst();
    }

    @FXML
    void BackButtonPressed(ActionEvent event) throws IOException {
        // Iterate over each medicine in the cartList
        for (Medicine medInCart : cartList) {
            try {
                // Retrieve the ResultSet containing all medicines from the database
                ResultSet rs = new Database().showGeneric();


                boolean found = false;

                // Find the corresponding medicine in the ResultSet
                while (rs.next()) {
                    Medicine medicine = new Medicine(rs);

                    // Check if the current medicine matches the one selected in the cart
                    if (medicine.getName().equals(medInCart.getName()) &&
                            medicine.getDose().equals(medInCart.getDose()) &&
                            medicine.getPrice().equals(medInCart.getPrice())) {

                        // Update the corresponding entry in the database
                        medicine.setQuantity(medInCart.getQuantity() + medicine.getQuantity());

                        // Attempt to update the database entry
                        if (GlobalDB.updateMedicine(medicine, medicine, GlobalConnect)) {
                            found = true;
                        } else {
                            showAlert("Database Update Error", "Failed to update the database.");
                        }
                    }

                }

                // Check if the medicine was found in the ResultSet
                if (!found) {
                    showAlert("Medicine Not Found", "Selected medicine not found in the database.");
                }



            } catch (SQLException e) {
                showAlert("Database Error", "An error occurred while accessing the database: " + e.getMessage());
                e.printStackTrace(); // Print the stack trace for debugging
            }
        }



        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void AddToCartButtonPressed(ActionEvent event) {
        Medicine selectedMedicine = GenericTable.getSelectionModel().getSelectedItem();
        Integer quantityValue = QuantityBox.getValue();

        if (quantityValue == null || quantityValue == 0) {
            showAlert("No Quantity", "No quantity selected. Please enter a quantity.");
            return;
        }

        Float selectedQuantity = quantityValue.floatValue();

        if (selectedMedicine == null) {
            showAlert("No Selection", "No medicine selected. Please select a medicine.");
        } else {
            if (selectedMedicine.getQuantity() >= selectedQuantity) {
                // Calculate the new quantity
                Float newQuantity = selectedMedicine.getQuantity() - selectedQuantity;

                // Update the database
                try {
                    Medicine updatedMedicine = selectedMedicine;
                    updatedMedicine.setQuantity(newQuantity);
                    if (GlobalDB.updateMedicine(selectedMedicine,updatedMedicine, GlobalConnect)) {
                        // Update the in-memory table
                        GenericTable.refresh();

                        // Add to the CartTable
                        Medicine medicineInCart = new Medicine(selectedMedicine.getName(), selectedMedicine.getDose(), selectedQuantity, selectedMedicine.getPrice());
                        cartList.add(medicineInCart);
                        CartTable.refresh();
                    } else {
                        showAlert("Database Update Error", "Failed to update the database.");
                    }
                } catch (SQLException e) {
                    showAlert("Database Update Error", "An error occurred while updating the database.");
                }
            } else {
                // Handle case when selected quantity is more than available quantity
                showAlert("Insufficient Stock", "Selected quantity exceeds available stock.");
            }
        }
        calculateAndSetSubtotal();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void DeleteButtonPressed(ActionEvent event) {
        Medicine medInCart = CartTable.getSelectionModel().getSelectedItem();

        if (medInCart == null) {
            showAlert("No Selection", "No medicine selected in the cart. Please select a medicine to remove.");
            return;
        }

        try {
            // Retrieve the ResultSet containing all medicines from the database
            ResultSet rs;
            try {
                rs = new Database().showGeneric();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to retrieve data from the database", e);
            }
            list.clear();
            // Find the corresponding medicine in the ResultSet
            while (rs.next()) {
                Medicine medicine = new Medicine(rs);
                if (medicine.getName().equals(medInCart.getName()) &&
                        medicine.getDose().equals(medInCart.getDose()) &&
                        medicine.getPrice().equals(medInCart.getPrice())) {
                    // Update the corresponding entry in the database

                    medicine.setQuantity(medInCart.getQuantity()+ medicine.getQuantity());
                    if (GlobalDB.updateMedicine(medicine, medicine, GlobalConnect)) {

                    } else {
                        showAlert("Database Update Error", "Failed to update the database.");
                    }
                    list.add(medicine);
                }
                else
                {
                    list.add(new Medicine(rs));
                }
            }


            GenericTable.setItems(list);
            GenericTable.refresh();
            cartList.remove(medInCart);
            CartTable.refresh();

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while accessing the database: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
        calculateAndSetSubtotal();

    }

    private void calculateAndSetSubtotal() {
        float subtotal = 0.0f;
        for (Medicine medicine : cartList) {
            subtotal += medicine.getQuantity() * medicine.getPrice();
        }
        // Update the subtotal label with the calculated subtotal
        subtotalLabel.setText(String.format("Subtotal:        %.2f   Tk", subtotal));
    }

    @FXML
    void checkoutButtonPressed(ActionEvent event) throws IOException {
        try {
            // Step 1: Call addMemo function
            boolean memoAdded = GlobalDB.addMemo();

            if (memoAdded) {
                // Step 2: Get the last memo value
                int lastMemoValue = GlobalDB.getLastMemoValue();

                // Step 3: Generate the PDF
                generatePdfForCart(lastMemoValue);

                showCheckoutSuccessAlert("Checkout Successful", "Your cart has been checked out and a memo has been generated.");
            } else {
                showAlert("Checkout Failed", "Failed to add memo. Please try again.");
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred during checkout. Please try again.");
        }

        //Back to previous menu
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }



    public void showCheckoutSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText("Thank You For Your Purchase");
        alert.showAndWait();
    }
    private void generatePdfForCart(int memoNo) throws FileNotFoundException {
        String dest = "memos/" + memoNo + ".pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Add heading "Meditronix"
            Paragraph heading = new Paragraph("Meditronix")
                    .setFont(boldFont)
                    .setFontSize(48)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(heading);

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            // Add title "Memo No: <memoNo>" and current date and time
            Paragraph title = new Paragraph("Invoice No: " + memoNo + "\nTime: " + formattedDateTime)
                    .setFont(regularFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginBottom(20);
            document.add(title);

            // Create a table with the same columns as the cartTable
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            // Add header row with custom formatting
            table.addHeaderCell(new Cell().add(new Paragraph("Name").setFont(boldFont).setFontSize(12).setPadding(5)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Dose").setFont(boldFont).setFontSize(12).setPadding(5)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setFont(boldFont).setFontSize(12).setPadding(5)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Price").setFont(boldFont).setFontSize(12).setPadding(5)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Calculate subtotal
            float subtotal = 0;

            // Add rows from cartList with custom formatting
            for (Medicine medicine : cartList) {
                table.addCell(new Cell().add(new Paragraph(medicine.getName()).setFont(regularFont).setFontSize(12).setPadding(5)));
                table.addCell(new Cell().add(new Paragraph(medicine.getDose()).setFont(regularFont).setFontSize(12).setPadding(5)));
                table.addCell(new Cell().add(new Paragraph(medicine.getQuantity().toString()).setFont(regularFont).setFontSize(12).setPadding(5)));
                table.addCell(new Cell().add(new Paragraph(medicine.getPrice().toString()).setFont(regularFont).setFontSize(12).setPadding(5)));
                subtotal += medicine.getQuantity() * medicine.getPrice();
            }

            // Add table to document
            document.add(table);

            // Add subtotal to document
            Paragraph subtotalParagraph = new Paragraph("Subtotal: " + subtotal + "  Tk")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(subtotalParagraph);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close document
            document.close();
        }

        String filePath =  "memos/" + memoNo + ".pdf";

        try {
            File file = new File(filePath);

            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("The specified file does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
