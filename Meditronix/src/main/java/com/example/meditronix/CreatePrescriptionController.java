package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


public class CreatePrescriptionController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField Age;

    @FXML
    private TextField Dosage;

    @FXML
    private TextField Frequency;

    @FXML
    private TextField Gender;

    @FXML
    private TextField MedicineName;

    @FXML
    private TextField Name;

    @FXML
    private TextField Quantity;

    @FXML
    private Button addMedicineButton;

    @FXML
    private Button backButton;

    @FXML
    private Button PatientID;

    @FXML
    private Button PrescriptionCode;

    @FXML
    private TextField medicineDosage;

    @FXML
    private VBox medicineEntryTemplate;

    @FXML
    private TextField medicineFrequency;

    @FXML
    private TextField medicineName;

    @FXML
    private TextField medicineQuantity;

    @FXML
    private Button removeMedicineButton;

    @FXML
    private Button stopPrescriptionButton;

    @FXML
    private Button removePatientButton;

    @FXML
    private Label MedicineCountLabel;

    @FXML
    private TableView<Patient> patientTable;


    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, Integer> ageColumn;

    @FXML
    private TableColumn<Patient, String> genderColumn;

    @FXML
    private TableView<Medicine> MedicineTableview;

    @FXML
    private TableColumn<Medicine, String> mednamecol;

    @FXML
    private TableColumn<Medicine, String> meddosagecol;

    @FXML
    private TableColumn<Medicine, String> medquantitycol;

    @FXML
    private TableColumn<Medicine, String> medfreqcol;

    private List<Medicine> medicines = new ArrayList<>();

   // private boolean prescriptionCodeGenerated = false;
    private boolean patientIDGenerated = false;
    private String prescriptionCode;

    private Database database = new Database(); // Instantiate Database class

    private int medicineCount = 0;
    private static final int MAX_MEDICINES = 15;

    // Random object for generating codes
    private Random random = new Random();

    // generate a random prescription code
    private String generatePrescriptionCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    //generate a random patient ID
    private String generatePatientID() {
        return String.format("%04d", random.nextInt(10000));
    }

    //return the prescription code
    private String getPresCode()
    {
        return prescriptionCode;
    }


    @FXML
    void addMedicine(ActionEvent event) {
        String name = MedicineName.getText();
        String dosage = Dosage.getText();
        String quantity = Quantity.getText();
        String frequency = Frequency.getText();

        if (validateMedicineFields(name, dosage, quantity, frequency)) {
            if (medicineCount < MAX_MEDICINES) {
                Medicine medicine = new Medicine(name, dosage, quantity, frequency);
                medicines.add(medicine);
                medicineCount++;

                // Update the label to display the current medicine count
                MedicineCountLabel.setText("Medicine Count: " + medicineCount);

                // Add medicine to the TableView
                MedicineTableview.getItems().add(medicine);

                System.out.println("Added medicine: " + medicine);
            } else {
                showErrorAlert("Maximum number of medicines reached.");
            }
        } else {
            showErrorAlert("Please fill in all medicine details.");
        }

        // Clear medicine input fields after adding medicine
        clearMedicineFields();
    }

    @FXML
    void addPatient(ActionEvent event) {
        String name = Name.getText();
        String ageText = Age.getText();
        String gender = Gender.getText(); // Get the gender from the TextField

        if (validatePatientFields(name, ageText, gender)) {
            int age = Integer.parseInt(ageText);
            // Pass the gender value to the Patient constructor
            Patient patient = new Patient(name, age, gender);
            patientTable.getItems().add(patient);

            // Clear the input fields after adding the patient
            clearInputFields();
        } else {
            showErrorAlert("Please fill in all patient details.");
        }
    }


private void clearInputFields() {
    Name.clear();
    Age.clear();
    Gender.clear();
}
    private boolean validateMedicineFields(String name, String dosage, String quantity, String frequency) {
        return !name.isEmpty() && !dosage.isEmpty() && !quantity.isEmpty() && !frequency.isEmpty();
    }

    private boolean validatePatientFields(String name, String age, String gender) {
        return !name.isEmpty() && !age.isEmpty() && !gender.isEmpty();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void nameTfPressed(ActionEvent event) {
        String nameText = Name.getText();

        if (!isValidName(nameText)) {
            showErrorAlert("Patient name cannot contain numerical values.");
        }
        else{
            System.out.println("Name: " + nameText);
        }
    }

    private boolean isValidName(String name) {
        return !name.matches(".*\\d.*");
    }

    @FXML
    void ageTfPressed(ActionEvent event) {
        String ageText = Age.getText();

        if (!isValidAge(ageText)) {
            showErrorAlert("Age must be a non-negative numerical value.");
        }
        else{
            System.out.println("Age: " + ageText);
        }
    }

    private boolean isValidAge(String age) {
        return age.matches("\\d+") && Integer.parseInt(age) >= 0;
    }
    @FXML
    void genderTfPressed(ActionEvent event) {
        String genderText = Gender.getText();

        if (!isValidGender(genderText)) {
            showErrorAlert("Gender must be 'M' or 'F'.");
        }
        else{
            System.out.println("Gender: " + genderText);
        }
    }

    private boolean isValidGender(String gender) {
        return gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F");
    }

    @FXML
    void PrescriptionCodePressed(ActionEvent event) {
        if (prescriptionCode == null) {
            showErrorAlert("No prescription has been created yet.");
        } else {
            // Create a custom dialog pane
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Prescription Code");
            dialog.setHeaderText(null);

            // Create labels for the prescription code and instruction
            Label codeLabel = new Label("Prescription Code: " + prescriptionCode);
            Label instructionLabel = new Label("Click the 'Copy' button to copy the code to clipboard.");

            // Create a button for copying the prescription code
            Button copyButton = new Button("Copy");
            copyButton.setOnAction(e -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(prescriptionCode);
                clipboard.setContent(content);
                dialog.close();
            });

            // Create a VBox to hold the components
            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(codeLabel, instructionLabel, copyButton);

            // Set the dialog content to the VBox
            dialog.getDialogPane().setContent(vbox);

            // Add a button for closing the dialog
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            // Show the dialog
            dialog.showAndWait();
        }
    }


    @FXML
    void PatientIDPressed(ActionEvent event) {
        if (!patientIDGenerated) {
            String patientID = generatePatientID();
            System.out.println("Patient ID: " + patientID);
            patientIDGenerated = true; // Set flag to true indicating ID has been generated

            // Show the dialog to display the patient ID and allow copying
            showCopyDialog("Patient ID", patientID);
        } else {
            // Show the dialog to display the already generated patient ID and allow copying
            showCopyDialog("Patient ID", "Already generated: " + generatePatientID());
        }
    }

    private void showCopyDialog(String title, String content) {
        // Create a custom dialog pane
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);

        // Create labels for the content and instruction
        Label contentLabel = new Label(content);
        Label instructionLabel = new Label("Click the 'Copy' button to copy the content to clipboard.");

        // Create a button for copying the content
        Button copyButton = new Button("Copy");
        copyButton.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(content);
            clipboard.setContent(clipboardContent);
            dialog.close();
        });

        // Create a VBox to hold the components
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(contentLabel, instructionLabel, copyButton);

        // Set the dialog content to the VBox
        dialog.getDialogPane().setContent(vbox);

        // Add a button for closing the dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Show the dialog
        dialog.showAndWait();
    }





    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DoctorMenu.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearMedicineFields() {
        MedicineName.clear();
        Dosage.clear();
        Quantity.clear();
        Frequency.clear();
    }

    @FXML
    void dosageTfPressed(ActionEvent event) {
        String dosageText = Dosage.getText();
        System.out.println("Dosage: " + dosageText);
    }

    @FXML
    void frequencyTfPressed(ActionEvent event) {
        String frequencyText = Frequency.getText();
        System.out.println("Frequency: " + frequencyText);
    }



    @FXML
    void medicineNameTfPressed(ActionEvent event) {
        String medicineNameText = MedicineName.getText();
        System.out.println("Medicine Name: " + medicineNameText);
    }



    @FXML
    void quantityTfPressed(ActionEvent event) {
        String quantityText = Quantity.getText();
        System.out.println("Quantity: " + quantityText);
    }

    @FXML
    void removeMedicine(ActionEvent event) {
        // Get the selected medicine
        Medicine selectedMedicine = MedicineTableview.getSelectionModel().getSelectedItem();
        if (selectedMedicine != null) {
            // Remove the selected medicine from the TableView
            MedicineTableview.getItems().remove(selectedMedicine);
            // Remove the selected medicine from the medicines list
            medicines.remove(selectedMedicine);
            // Decrease the medicine count
            medicineCount--;
            // Update the label to display the current medicine count
            MedicineCountLabel.setText("Medicine Count: " + medicineCount);
            // Delete the corresponding data from the database
            database.deleteMedicineData(prescriptionCode, selectedMedicine.getName(), selectedMedicine.getDosage(), selectedMedicine.getQuantity(), selectedMedicine.getFrequency());
        } else {
            System.out.println("No medicine selected to remove.");
        }
    }

    @FXML
    void removePatientPressed(ActionEvent event) {
        // Get the selected patient
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Remove the selected patient from the TableView
            patientTable.getItems().remove(selectedPatient);
            // Delete the corresponding data from the database
            //database.deletePatientData(selectedPatient.getName());
        } else {
            System.out.println("No patient selected to remove.");
        }
    }

    @FXML
    void stopPrescription(ActionEvent event) {

        // Check if at least one medicine has been added
        if (medicines.isEmpty()) {
            showErrorAlert("Please add medicine before creating a prescription.");
            return;
        }

        System.out.println("Prescription stopped.");

        // Generate prescription code

            prescriptionCode = generatePrescriptionCode();
            System.out.println("Prescription Code: " + prescriptionCode);


        // Create a new table for the prescription
        database.createPrescriptionTable(prescriptionCode);

        // Insert medicine data into the table
        for (Medicine medicine : medicines) {
            try {
                int quantity = Integer.parseInt(medicine.getQuantity());
                database.insertMedicineData(prescriptionCode, medicine.getName(), medicine.getDosage(), quantity, medicine.getFrequency());
            } catch (NumberFormatException e) {
                showErrorAlert("Quantity must be a valid integer.");
            }

        }

        medicines.clear();
        medicineCount = 0;
        if(medicineCount>=0) {
            MedicineCountLabel.setText("Medicine Count: " + medicineCount);
        }


    }



    public class Medicine {
        public String name;
        public String dosage;
        public String quantity;
        public String frequency;

        public Medicine(String name, String dosage, String quantity, String frequency) {
            this.name = name;
            this.dosage = dosage;
            this.quantity = quantity;
            this.frequency = frequency;
        }

        public String getName() {
            return name;
        }

        public String getDosage() {
            return dosage;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getFrequency() {
            return frequency;
        }

        @Override
        public String toString() {
            return "Medicine{" +
                    "name='" + name + '\'' +
                    ", dosage='" + dosage + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", frequency='" + frequency + '\'' +
                    '}';
        }
    }

    public class Patient {
        private String name;
        private int age;
        private String gender;

        public Patient(String name, int age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }
    }

    @FXML
    public void initialize() {
        // Initialize TableColumn objects for patient TableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Initialize TableColumn objects for medicine TableView
        mednamecol.setCellValueFactory(new PropertyValueFactory<>("name"));
        meddosagecol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        medquantitycol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        medfreqcol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
    }
}
