package com.template;
import javafx.event.ActionEvent; import javafx.fxml.FXML; import javafx.scene.control.*;
import java.sql.*;

public class LoginController extends BaseController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private RadioButton rbOwner;
    @FXML private Label lblError;

    @FXML public void handleLogin(ActionEvent event) {
        String role = rbOwner.isSelected() ? "Owner" : "Tenant";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT user_id, username FROM users WHERE username=? AND `password`=? AND `role`=?");
            stmt.setString(1, txtUsername.getText());
            stmt.setString(2, txtPassword.getText());
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String user = rs.getString("username");
                Session.currentUser = role.equals("Owner") ? new Owner(id, user) : new Tenant(id, user);


                if (role.equals("Owner")) switchScene(event, "OwnerDashboard.fxml");
                else switchScene(event, "TenantSearchUI.fxml");
            } else throw new TAKException("Invalid Credentials!");
        } catch (TAKException e)
        {

            lblError.setText(e.getMessage());

        }
        catch (Exception e)
        {
            lblError.setText("DB Error");

            e.printStackTrace();}
    }
    @FXML public void goToSignup(ActionEvent event) {
        switchScene(event, "SignupUI.fxml");
    }
}