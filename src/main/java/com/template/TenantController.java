package com.template;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TenantController extends BaseController {
    @FXML private ComboBox<String> cbLoc;
    @FXML private TextField txtRent, txtSize; // Added txtSize
    @FXML private Label lblError;

    @FXML public void initialize() {
        cbLoc.getItems().addAll("Dhanmondi", "Mirpur", "Uttara", "Gulshan", "Banani", "Mohammadpur", "Badda", "Bashundhara", "Farmgate", "Motijheel");
    }

    @FXML public void handleSearch(ActionEvent event) {
        try {
            if (cbLoc.getValue() == null || txtRent.getText().isEmpty() || txtSize.getText().isEmpty()) {
                throw new TAKException("Location, Rent, and Size are mandatory!");
            }
            Session.searchLoc = cbLoc.getValue();
            Session.searchRent = Double.parseDouble(txtRent.getText());
            Session.searchSize = Integer.parseInt(txtSize.getText());

            switchScene(event, "ResultsUI.fxml");
        } catch (TAKException e) { lblError.setText(e.getMessage());
        } catch (Exception e) { lblError.setText("Invalid format. Use numbers for Rent and Size."); }
    }
    @FXML public void logout(ActionEvent event) {
        Session.currentUser = null;
        switchScene(event, "LoginUI.fxml");
    }
}