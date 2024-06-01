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
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class IndividualPrescription implements Initializable {
    @FXML
    private Label patientAgeLabel;

    @FXML
    private Label patientGenderLabel;

    @FXML
    private Label patientNameLabel;

    @FXML
    private Label presCode;

    @FXML
    private Button backButton;

    @FXML
    private Button dnldPDF;

    @FXML
    private TableColumn<MedicineDataPrescription, String> medDosage;

    @FXML
    private TableColumn<MedicineDataPrescription, String> medFreq;

    @FXML
    private TableColumn<MedicineDataPrescription, String> medName;

    @FXML
    private TableColumn<MedicineDataPrescription, Integer> medQuantity;

    @FXML
    private TableView<MedicineDataPrescription> medTable;

    private Stage stage;
    private Scene scene;

    private String prescriptionCode;

    private Database database = new Database();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // No action needed here
    }

    public void setPrescriptionCode(String prescriptionCode) {
        this.prescriptionCode = prescriptionCode;
        presCode.setText(prescriptionCode);
        loadData();
    }

    private void loadData() {
        // Load patient data
        PatientDataPrescription patientData = database.getPatientData(prescriptionCode);
        if (patientData != null) {
            patientNameLabel.setText(patientData.getName());
            patientAgeLabel.setText(patientData.getAge());
            patientGenderLabel.setText(patientData.getGender());
        }

        // Load medicine data
        ObservableList<MedicineDataPrescription> medicineDataList = database.getMedicineData(prescriptionCode);
        medName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        medQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        medFreq.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        medTable.setItems(FXCollections.observableArrayList(medicineDataList));
    }

    @FXML
    void downloadPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(dnldPDF.getScene().getWindow());

        if (file != null) {
            createPDF(file);
        }

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
            document.add(new Paragraph("Patient Name: " + patientNameLabel.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph("Patient Age: " + patientAgeLabel.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph("Patient Gender: " + patientGenderLabel.getText())
                    .setBold().setFontSize(12));
            document.add(new Paragraph("Prescription Code: " + presCode.getText())
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
            ObservableList<MedicineDataPrescription> medicineDataList = medTable.getItems();
            for (MedicineDataPrescription med : medicineDataList) {
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



    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewPrescription.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}