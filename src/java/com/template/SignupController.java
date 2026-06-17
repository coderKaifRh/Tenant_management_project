package com.template;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class SignupController extends BaseController {
    @FXML private TextField txtUser, txtName, txtPhone, txtEmail;
    @FXML private PasswordField txtPass;
    @FXML private RadioButton rbOwner;
    @FXML private Label lblError;

    @FXML public void handleSignup(ActionEvent event) {
        String role = rbOwner.isSelected() ? "Owner" : "Tenant";
        try (Connection conn = DBConnection.getConnection()) {
            // OOP ENCAPSULATION: Validate phone number before touching the database
            Owner tempOwner = new Owner(0, txtUser.getText());
            Tenant tempTenant = new Tenant(0, txtUser.getText());
            if (role.equals("Owner")) {
                tempOwner.setPhone(txtPhone.getText()); // Throws TAKException if invalid BD number
            }
            else
            {
                tempTenant.setPhone(txtPhone.getText());
            }


            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, `password`, `role`) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, txtUser.getText());
            stmt.setString(2, txtPass.getText());
            stmt.setString(3, role);
            stmt.executeUpdate();


            if (role.equals("Owner")) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int newUserId = rs.getInt(1);
                    PreparedStatement infoStmt = conn.prepareStatement("INSERT INTO owner_info (user_id, name, phone, email) VALUES (?, ?, ?, ?)");
                    infoStmt.setInt(1, newUserId);
                    infoStmt.setString(2, txtName.getText());
                    infoStmt.setString(3, txtPhone.getText());
                    infoStmt.setString(4, txtEmail.getText());
                    infoStmt.executeUpdate();
                }
            }
            else
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int newUserId = rs.getInt(1);
                    PreparedStatement infoStmt = conn.prepareStatement("INSERT INTO tenant_info (user_id, name, phone, email) VALUES (?, ?, ?, ?)");
                    infoStmt.setInt(1, newUserId);
                    infoStmt.setString(2, txtName.getText());
                    infoStmt.setString(3, txtPhone.getText());
                    infoStmt.setString(4, txtEmail.getText());
                    infoStmt.executeUpdate();
                }
            }
            switchScene(event, "LoginUI.fxml");

        }
        catch (TAKException e) {
            lblError.setText(e.getMessage());
        }
        catch (Exception e) {
            lblError.setText("Error: Username may already exist.");
            e.printStackTrace();
        }
    }
    @FXML public void goToLogin(ActionEvent event) {
        switchScene(event, "LoginUI.fxml");
    }
}