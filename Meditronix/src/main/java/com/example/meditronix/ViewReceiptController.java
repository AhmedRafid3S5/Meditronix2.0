package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ViewReceiptController {


    @FXML
    private Button backfromreceiptview;

    @FXML
    private Button FindButton;


    @FXML
    private TextField invoiceNo;


    @FXML
    void FindButtonPressed(ActionEvent event) throws IOException {
        // Get the invoice number from the TextField
        String invoiceNumberText = invoiceNo.getText();

        try {
            // Parse the invoice number as an integer
            int invoiceNumber = Integer.parseInt(invoiceNumberText);

            // Construct the file path for the PDF file
            String filePath = "C:\\Users\\Rafid\\IdeaProjects\\Meditronix2.0-main-Merged\\Meditronix2.0-main\\Meditronix\\memos\\" + invoiceNumber + ".pdf";
            File pdfFile = new File(filePath);

            // Check if the PDF file exists
            if (pdfFile.exists()) {
                // Open the PDF file using the default PDF viewer
                Desktop.getDesktop().open(pdfFile);
            } else {
                // Show an error alert if the PDF file does not exist
                showAlert("Error", "Invoice not found!");
            }
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            showAlert("Error", "Please enter a valid invoice number.");
        } catch (IOException e) {
            // Handle any other IO exceptions
            e.printStackTrace();
            showAlert("Error", "An error occurred while opening the PDF file.");
        }
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }

    // Method to show an error alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void Backfromreceiptviewpress(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();

    }

}
