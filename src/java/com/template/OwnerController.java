package com.template;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerController extends BaseController {

    @FXML private ComboBox<String> cbLoc;
    @FXML private TextField txtRent, txtSize;
    @FXML private ComboBox<Integer> cbBed, cbWash;
    @FXML private Label lblStatus;
    @FXML private Label lblImageCount;
    @FXML private Label lblFees;



    private List<File> selectedImageFiles = new ArrayList<>();

    @FXML
    public void initialize() {
        cbLoc.getItems().addAll("Dhanmondi", "Mirpur", "Uttara", "Gulshan", "Banani", "Mohammadpur", "Badda", "Bashundhara", "Farmgate", "Motijheel");
        cbBed.getItems().addAll(1, 2, 3, 4, 5);
        cbWash.getItems().addAll(1, 2, 3, 4);

    }


    @FXML
    public void handleSelectImages(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Flat Photos");
        // Only allow image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );


        Stage stage = (Stage) cbLoc.getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            selectedImageFiles.addAll(files);
            lblImageCount.setText(selectedImageFiles.size() + " photo(s) selected");
        }
    }

    @FXML
    public void handleAddFlat(ActionEvent event) {
        try (Connection conn = DBConnection.getConnection()) {

            String insertFlatSQL = "INSERT INTO flats (owner_id, location, rent, size, bedroom, washroom, status) VALUES (?, ?, ?, ?, ?, ?, 'Available')";
            PreparedStatement flatStmt = conn.prepareStatement(insertFlatSQL, Statement.RETURN_GENERATED_KEYS);

            flatStmt.setInt(1, Session.currentUser.getUserId());
            flatStmt.setString(2, cbLoc.getValue());
            flatStmt.setDouble(3, Double.parseDouble(txtRent.getText()));
            flatStmt.setInt(4, Integer.parseInt(txtSize.getText()));
            flatStmt.setInt(5, cbBed.getValue());
            flatStmt.setInt(6, cbWash.getValue());

            flatStmt.executeUpdate();


            ResultSet rs = flatStmt.getGeneratedKeys();
            int newFlatId = -1;
            if (rs.next()) {
                newFlatId = rs.getInt(1);
            }


            if (newFlatId != -1 && !selectedImageFiles.isEmpty()) {
                String insertImageSQL = "INSERT INTO flat_images (flat_id, image_path) VALUES (?, ?)";
                PreparedStatement imageStmt = conn.prepareStatement(insertImageSQL);

                for (File file : selectedImageFiles) {
                    imageStmt.setInt(1, newFlatId);
                    imageStmt.setString(2, file.getAbsolutePath());
                    imageStmt.executeUpdate();
                }
            }

            lblStatus.setText("Flat & " + selectedImageFiles.size() + " Photos Added Successfully!");
            lblStatus.setTextFill(javafx.scene.paint.Color.GREEN);

            selectedImageFiles.clear();
            lblImageCount.setText("0 photos selected");
            double rent = Double.parseDouble(txtRent.getText());
            double pr = Session.currentUser.calculatePayableAmount(rent);
            lblFees.setText("Required Platform fee: " + pr+ " BDT Total");

        } catch (NumberFormatException e) {
            lblStatus.setText("Error: Rent and Size must be numbers.");
            lblStatus.setTextFill(javafx.scene.paint.Color.RED);
        } catch (Exception e) {
            lblStatus.setText("Error: Please fill all fields.");
            lblStatus.setTextFill(javafx.scene.paint.Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) {
        Session.currentUser = null;
        switchScene(event, "LoginUI.fxml");
    }
}