package com.csslab.controller;

import com.csslab.model.BorrowRecord;
import com.csslab.model.Equipment;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class MainController {

    // Borrowing Form Controls
    @FXML private TextField studentIdField;
    @FXML private TextField studentNameField;
    @FXML private TextField sectionField;
    @FXML private ComboBox<Equipment> equipmentComboBox;

    // Borrowing Logs Table
    @FXML private TableView<BorrowRecord> borrowTable;
    @FXML private TableColumn<BorrowRecord, String> colRefId;
    @FXML private TableColumn<BorrowRecord, String> colStudent;
    @FXML private TableColumn<BorrowRecord, String> colBorrowedItem;
    @FXML private TableColumn<BorrowRecord, String> colDate;
    @FXML private TableColumn<BorrowRecord, String> colBorrowStatus;

    // Inventory Form Controls
    @FXML private TextField eqCodeField;
    @FXML private TextField eqNameField;
    @FXML private ComboBox<String> categoryBox;

    // Inventory Table
    @FXML private TableView<Equipment> equipmentTable;
    @FXML private TableColumn<Equipment, String> colEqCode;
    @FXML private TableColumn<Equipment, String> colEqName;
    @FXML private TableColumn<Equipment, String> colEqCategory;
    @FXML private TableColumn<Equipment, String> colEqStatus;

    // In-Memory Data Lists
    private final ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();
    private final ObservableList<BorrowRecord> borrowRecords = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTables();
        seedInitialData();
        updateAvailableEquipmentDropdown();

        categoryBox.setItems(FXCollections.observableArrayList(
                "Networking Tools", "Hand Tools", "Diagnostic & Testing", "PC Hardware", "Peripherals"
        ));
    }

    private void setupTables() {
        // Borrow Logs Table Columns
        colRefId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRecordId()));
        colStudent.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStudentName() + " (" + data.getValue().getSection() + ")"));
        colBorrowedItem.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEquipment().getName()));
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateBorrowed()));
        colBorrowStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        borrowTable.setItems(borrowRecords);

        // Equipment Inventory Table Columns
        colEqCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        colEqName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colEqCategory.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        colEqStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        equipmentTable.setItems(equipmentList);
    }

    @FXML
    private void handleBorrowEquipment() {
        String id = studentIdField.getText().trim();
        String name = studentNameField.getText().trim();
        String section = sectionField.getText().trim();
        Equipment selectedEq = equipmentComboBox.getValue();

        if (id.isEmpty() || name.isEmpty() || section.isEmpty() || selectedEq == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Form", "Please fill out all fields and select an available tool.");
            return;
        }

        selectedEq.setStatus("Borrowed");
        equipmentTable.refresh();
        updateAvailableEquipmentDropdown();

        BorrowRecord record = new BorrowRecord(
                "BRW-" + (borrowRecords.size() + 101),
                id, name, section, selectedEq,
                LocalDate.now().toString(),
                "Active"
        );
        borrowRecords.add(0, record);

        studentIdField.clear();
        studentNameField.clear();
        sectionField.clear();
        equipmentComboBox.setValue(null);

        showAlert(Alert.AlertType.INFORMATION, "Success", selectedEq.getName() + " issued to " + name + ".");
    }

    @FXML
    private void handleReturnEquipment() {
        BorrowRecord selectedRecord = borrowTable.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Select a borrowing record to mark as returned.");
            return;
        }
        if ("Returned".equals(selectedRecord.getStatus())) {
            showAlert(Alert.AlertType.INFORMATION, "Already Returned", "This item has already been returned.");
            return;
        }

        selectedRecord.setStatus("Returned");
        selectedRecord.getEquipment().setStatus("Available");

        borrowTable.refresh();
        equipmentTable.refresh();
        updateAvailableEquipmentDropdown();
    }

    @FXML
    private void handleAddEquipment() {
        String code = eqCodeField.getText().trim();
        String name = eqNameField.getText().trim();
        String category = categoryBox.getValue();

        if (code.isEmpty() || name.isEmpty() || category == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Info", "Enter asset code, equipment name, and category.");
            return;
        }

        equipmentList.add(new Equipment(code, name, category, "Available"));
        updateAvailableEquipmentDropdown();

        eqCodeField.clear();
        eqNameField.clear();
        categoryBox.setValue(null);
    }

    private void updateAvailableEquipmentDropdown() {
        ObservableList<Equipment> available = FXCollections.observableArrayList();
        for (Equipment eq : equipmentList) {
            if ("Available".equals(eq.getStatus())) {
                available.add(eq);
            }
        }
        equipmentComboBox.setItems(available);
    }

    private void seedInitialData() {
        equipmentList.addAll(
                new Equipment("CSS-01", "RJ45 Crimping Tool (Heavy Duty)", "Networking Tools", "Available"),
                new Equipment("CSS-02", "LAN Cable Tester (Master/Remote)", "Diagnostic & Testing", "Available"),
                new Equipment("CSS-03", "Punch Down Impact Tool", "Networking Tools", "Available"),
                new Equipment("CSS-04", "Precision Screwdriver Set (32-in-1)", "Hand Tools", "Available"),
                new Equipment("CSS-05", "Digital Multimeter", "Diagnostic & Testing", "Available"),
                new Equipment("CSS-06", "Anti-Static Wrist Strap", "Hand Tools", "Available")
        );
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}