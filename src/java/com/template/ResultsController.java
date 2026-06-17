package com.template;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.sql.*;

public class ResultsController extends BaseController {

    @FXML private VBox resultsContainer;
    @FXML private Label lblFee;

    @FXML
    public void initialize() {
        double advanceAmount = Session.currentUser.calculatePayableAmount(Session.searchRent);
        lblFee.setText("Required Advance Payment: " + advanceAmount + " BDT Total");

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM flats WHERE location=? AND rent<=? AND `size`>=? AND `status`='Available' ORDER BY rent ASC, `size` DESC"
            );
            stmt.setString(1, Session.searchLoc);
            stmt.setDouble(2, Session.searchRent);
            stmt.setInt(3, Session.searchSize);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                int flatId = rs.getInt("flat_id");
                double rent = rs.getDouble("rent");
                int size = rs.getInt("size");
                int bed = rs.getInt("bedroom");
                int wash = rs.getInt("washroom");


                HBox card = new HBox(20);
                card.setPadding(new Insets(15));
                card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));


                HBox imageGallery = new HBox(10);


                PreparedStatement imgStmt = conn.prepareStatement("SELECT image_path FROM flat_images WHERE flat_id=?");
                imgStmt.setInt(1, flatId);
                ResultSet imgRs = imgStmt.executeQuery();

                boolean hasImages = false;
                while (imgRs.next()) {
                    String path = imgRs.getString("image_path");
                    File file = new File(path);
                    if (file.exists()) {
                        ImageView imgView = new ImageView(new Image("file:" + file.getAbsolutePath()));
                        imgView.setFitWidth(250);
                        imgView.setFitHeight(210);
                        imageGallery.getChildren().add(imgView);
                        hasImages = true;
                    }
                }

                if (!hasImages) {
                    Label noImg = new Label("No Photos Available");
                    noImg.setTextFill(Color.GRAY);
                    imageGallery.getChildren().add(noImg);
                }


                ScrollPane imageScroll = new ScrollPane(imageGallery);
                imageScroll.setPrefViewportWidth(420);
                imageScroll.setPrefViewportHeight(220);
                imageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                imageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                imageScroll.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));


                VBox details = new VBox(8);
                Label title = new Label(bed + " Bed, " + wash + " Wash Flat in " + Session.searchLoc);
                title.setFont(Font.font("System", FontWeight.BOLD, 18));
                title.setTextFill(Color.web("#001933"));

                Label info = new Label("Rent: " + rent + " BDT  |  Size: " + size + " sqft");
                info.setFont(Font.font("System", 14));

                Button btnBook = new Button("BOOK FLAT");
                btnBook.setBackground(new Background(new BackgroundFill(Color.web("#004c99"), new CornerRadii(5), Insets.EMPTY)));
                btnBook.setTextFill(Color.WHITE);
                btnBook.setFont(Font.font("System", FontWeight.BOLD, 14));


                btnBook.setOnAction(e -> handleBook(flatId, e));

                details.getChildren().addAll(title, info, btnBook);


                card.getChildren().addAll(imageScroll, details);


                resultsContainer.getChildren().add(card);
            }

            if (!found) {
                Label noMatch = new Label("No available flats match your budget and size.");
                noMatch.setFont(Font.font("System", 16));
                resultsContainer.getChildren().add(noMatch);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void handleBook(int flatId, ActionEvent event) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement update = conn.prepareStatement("UPDATE flats SET `status`='Booked' WHERE flat_id=?");
            update.setInt(1, flatId);
            update.executeUpdate();

            PreparedStatement insert = conn.prepareStatement("INSERT INTO bookings (flat_id, tenant_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            insert.setInt(1, flatId);
            insert.setInt(2, Session.currentUser.getUserId());
            insert.executeUpdate();

            ResultSet rs = insert.getGeneratedKeys();
            if(rs.next()) Session.currentBookingId = rs.getInt(1);

            switchScene(event, "TransportUI.fxml");
        } catch (Exception e) { e.printStackTrace(); }
    }
    public void Backtosearch(ActionEvent event) {

        switchScene(event, "TenantSearchUI.fxml");
    }
}