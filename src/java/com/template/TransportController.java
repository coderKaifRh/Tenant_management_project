package com.template;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

public class TransportController extends BaseController {

    @FXML private ComboBox<String> cbTruck;
    @FXML private TextField txtWorkers, txtAddress, txtTime;
    @FXML private DatePicker dpDate;
    @FXML private Label lblCost, lblError,lblBook;

    @FXML
    public void initialize() {
        cbTruck.getItems().addAll("Small", "Medium", "Large");
    }


    public void calculateCost() {
        try {
            if (cbTruck.getValue() != null && !txtWorkers.getText().isEmpty()) {
                // OOP Polymorphism calculating cost
                TAKService transport = new TransportService(cbTruck.getValue(), Integer.parseInt(txtWorkers.getText()));
                lblCost.setText("Total Cost: " + transport.calculateTotalCost() + " BDT");
                lblError.setText("");
            }
        } catch (NumberFormatException e) {
            lblCost.setText("Total Cost: 0.0 BDT");
        } catch (Exception e) {
            lblCost.setText("Total Cost: - BDT");
        }
    }

    @FXML
    public void confirmTransport(ActionEvent event) {
        try {
            LocalDate date = dpDate.getValue();
            if (txtAddress.getText().isEmpty() || cbTruck.getValue() == null || txtWorkers.getText().isEmpty() || date == null || txtTime.getText().isEmpty()) {
                throw new TAKException("All fields (including Date and Time) must be filled.");
            }

            TAKService transport = new TransportService(cbTruck.getValue(), Integer.parseInt(txtWorkers.getText()));
            double finalCost = transport.calculateTotalCost();

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO transport (booking_id, pickup_address, moving_date, `time`, truck_size, manpower, cost) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );

                stmt.setInt(1, Session.currentBookingId);
                stmt.setString(2, txtAddress.getText());
                stmt.setDate(3, java.sql.Date.valueOf(date)); // Saving the Date!
                stmt.setString(4, txtTime.getText());         // Saving the Time!
                stmt.setString(5, cbTruck.getValue());
                stmt.setInt(6, Integer.parseInt(txtWorkers.getText()));
                stmt.setDouble(7, finalCost);

                stmt.executeUpdate();
                lblBook.setText("Shifting confirmed");

                // Done! Send back to search page
                //switchScene(event, "TenantSearchUI.fxml");
            }
        } catch (TAKException e) {
            lblError.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Database error occurred.");
        }
    }

    @FXML
    public void skipTransport(ActionEvent event) {
        switchScene(event, "TenantSearchUI.fxml");
    }
}