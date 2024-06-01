package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.pdf.PdfDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerViewPrescription {
    @FXML
    private Label ageLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button downloadPDF;

    @FXML
    private Label genderLabel;

    @FXML
    private Button loadPrescriptionButton;

    @FXML
    private TableColumn<MedicineDataPrescription, String> medDosageColumn;

    @FXML
    private TableColumn<MedicineDataPrescription, String> medFrequencyColumn;

    @FXML
    private TableColumn<MedicineDataPrescription, String> medNameColumn;

    @FXML
    private TableColumn<MedicineDataPrescription, Integer> medQuantityColumn;

    @FXML
    private TableView<MedicineDataPrescription> medTable;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField prescriptionCode;

    private Database database = new Database();

    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PurchaseTypeSelection.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void downloadPDFPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(downloadPDF.getScene().getWindow());

        if (file != null) {
            createPDF(file);
        }
    }

    @FXML
    void loadPrescriptionButtonPressed(ActionEvent event) {
        String code = prescriptionCode.getText();
        if (!code.isEmpty()) {
            loadPrescriptionData(code);
        }
    }

    @FXML
    void prescriptionCodeTfPressed(ActionEvent event) {
        // No action needed here
    }

    private void loadPrescriptionData(String prescriptionCode) {
        // Load patient data
        PatientDataPrescription patientData = database.getPatientData(prescriptionCode);
        if (patientData != null) {
            nameLabel.setText(patientData.getName());
            ageLabel.setText(patientData.getAge());
            genderLabel.setText(patientData.getGender());
        }

        // Load medicine data
        medNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medDosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        medQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        medFrequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        medTable.setItems(database.getMedicineData(prescriptionCode));
    }

    private void createPDF(File file) {
        try {
            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Prescription")
                    .setBold().setFontSize(18).setUnderline());
            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------------------")
                    .setBold().setFontSize(12));
            // Add patient details with specific styling
            document.add(new Paragraph("Patient Name: " + nameLabel.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph("Patient Age: " + ageLabel.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph("Patient Gender: " + genderLabel.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph("Prescription Code: " + prescriptionCode.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph(" ").setMarginBottom(20)); // Add a blank line

            // Create a table for medicine details with specific styling
            Table table = new Table(4); // 4 columns
            table.addCell(new Cell().add(new Paragraph("Medicine Name")
                    .setBold().setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph("Dosage")
                    .setBold().setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph("Quantity")
                    .setBold().setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph("Frequency")
                    .setBold().setFontSize(10)));

            // Add medicine data to the table with specific styling
            for (MedicineDataPrescription med : medTable.getItems()) {
                table.addCell(new Cell().add(new Paragraph(med.getMedicineName())
                        .setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(med.getDosage())
                        .setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(med.getQuantity()))
                        .setFontSize(10)));
                table.addCell(new Cell().add(new Paragraph(med.getFrequency())
                        .setFontSize(10)));
            }

            // Add table to the document
            document.add(table);

            // Add timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            document.add(new Paragraph("Downloaded on: " + timestamp).setFontSize(10));

            // Close the document
            document.close();

            System.out.println("PDF created successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}