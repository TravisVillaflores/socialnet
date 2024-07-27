package org.example.socialnet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SocialNetApplication extends Application {
    private Map<String, UserProfile> profiles = new HashMap<>();
    private UserProfile currentUserProfile;

    private TextField nameField = new TextField();
    private TextField statusField = new TextField();
    private TextField quoteField = new TextField();
    private TextField imageField = new TextField();
    private ListView<String> friendsListView = new ListView<>();
    private Label actionMessage = new Label();
    private ImageView profileImageView = new ImageView();
    private Label statusLabel = new Label();
    private Label quoteLabel = new Label();
    private Label friendsLabel = new Label(); // New Label for friends

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SocialNet");

        BorderPane mainLayout = new BorderPane();
        VBox leftPane = new VBox(10);
        VBox rightPane = new VBox(10);

        // Left pane: User profile and controls
        Label nameLabel = new Label("Name:");
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addProfile());

        Label statusChangeLabel = new Label("Status:");
        Button statusButton = new Button("Change Status");
        statusButton.setOnAction(e -> changeStatus());

        Label quoteChangeLabel = new Label("Favorite Quote:");
        Button quoteButton = new Button("Change Quote");
        quoteButton.setOnAction(e -> changeQuote());

        Label imageLabel = new Label("Image Path:");
        Button imageButton = new Button("Change Picture");
        imageButton.setOnAction(e -> changeImage());

        leftPane.getChildren().addAll(nameLabel, nameField, addButton, statusChangeLabel, statusField, statusButton,
                quoteChangeLabel, quoteField, quoteButton, imageLabel, imageField, imageButton, actionMessage);

        // Right pane: Display profile info
        Button unfriendButton = new Button("Unfriend");
        unfriendButton.setOnAction(e -> unfriendSelectedProfile());

        rightPane.getChildren().addAll(new Label("Profile Image:"), profileImageView, statusLabel, quoteLabel,
                friendsLabel, new Label("Friends:"), friendsListView, unfriendButton);

        mainLayout.setLeft(leftPane);
        mainLayout.setCenter(rightPane);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add sample profiles and set an empty default profile display
        addSampleProfiles();
        friendsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentUserProfile = profiles.get(newValue);
                updateProfileDisplay();
            }
        });
        updateProfileDisplay();
    }

    private void addProfile() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            actionMessage.setText("Name cannot be empty.");
            return;
        }

        if (!profiles.containsKey(name)) {
            UserProfile profile = new UserProfile(name);
            profiles.put(name, profile);
            updateFriendsList();
            actionMessage.setText("Profile added for " + name);
        } else {
            actionMessage.setText("Profile already exists for " + name);
        }
    }

    private void changeStatus() {
        if (currentUserProfile != null) {
            currentUserProfile.setStatus(statusField.getText());
            actionMessage.setText("Status updated");
            updateProfileDisplay();
        }
    }

    private void changeQuote() {
        if (currentUserProfile != null) {
            currentUserProfile.setFavoriteQuote(quoteField.getText());
            actionMessage.setText("Favorite quote updated");
            updateProfileDisplay();
        }
    }

    private void changeImage() {
        if (currentUserProfile != null) {
            currentUserProfile.setImagePath(imageField.getText());
            actionMessage.setText("Picture updated");
            updateProfileDisplay();
        }
    }

    private void unfriendSelectedProfile() {
        if (currentUserProfile != null) {
            String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
            if (selectedFriend != null && !selectedFriend.isEmpty()) {
                // Remove selected friend from the current user's profile
                currentUserProfile.removeFriend(selectedFriend);

                // Remove current user from the selected friend's profile
                UserProfile selectedFriendProfile = profiles.get(selectedFriend);
                if (selectedFriendProfile != null) {
                    selectedFriendProfile.removeFriend(currentUserProfile.getName());
                }

                // Update the profile display and the friends list
                updateProfileDisplay();
                actionMessage.setText("Unfriended " + selectedFriend);
            } else {
                actionMessage.setText("No friend selected.");
            }
        } else {
            actionMessage.setText("No profile selected.");
        }
    }

    private void updateProfileDisplay() {
        if (currentUserProfile != null) {
            try {
                InputStream imageStream = getClass().getResourceAsStream("/images/" + currentUserProfile.getImagePath());
                if (imageStream != null) {
                    profileImageView.setImage(new Image(imageStream));
                } else {
                    System.err.println("Image not found: " + currentUserProfile.getImagePath());
                    profileImageView.setImage(null); // Fallback if image is not found
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                profileImageView.setImage(null); // Fallback if image is not found
            }
            statusLabel.setText("Status: " + currentUserProfile.getStatus());
            quoteLabel.setText("Favorite Quote: " + currentUserProfile.getFavoriteQuote());
            friendsLabel.setText("Friends with: " + String.join(", ", currentUserProfile.getFriends()));
        } else {
            profileImageView.setImage(null);
            statusLabel.setText("");
            quoteLabel.setText("");
            friendsLabel.setText("");
        }
        updateFriendsList();
    }

    private void updateFriendsList() {
        friendsListView.getItems().clear();
        for (String profileName : profiles.keySet()) {
            friendsListView.getItems().add(profileName);
        }
    }

    private void addSampleProfiles() {
        UserProfile gojo = new UserProfile("Gojo Satoru");
        gojo.setStatus("Strongest sorcerer");
        gojo.setFavoriteQuote("Throughout heaven and earth, I alone am the honored one.");
        gojo.setImagePath("gojo_satoru.png");
        gojo.addFriend("Sun Goku");
        gojo.addFriend("Mahoraga");
        profiles.put("Gojo Satoru", gojo);
        System.out.println("Added profile: Gojo Satoru");

        UserProfile goku = new UserProfile("Sun Goku");
        goku.setStatus("Saiyan warrior");
        goku.setFavoriteQuote("I am the hope of the universe.");
        goku.setImagePath("sun_goku.png");
        goku.addFriend("Gojo Satoru");
        goku.addFriend("Nino Nakano");
        goku.addFriend("Celine Minglana");
        profiles.put("Sun Goku", goku);
        System.out.println("Added profile: Sun Goku");

        UserProfile mahoraga = new UserProfile("Mahoraga");
        mahoraga.setStatus("Cursed spirit");
        mahoraga.setFavoriteQuote("...");
        mahoraga.setImagePath("mahoraga.png");
        mahoraga.addFriend("Gojo Satoru");
        profiles.put("Mahoraga", mahoraga);
        System.out.println("Added profile: Mahoraga");

        UserProfile nino = new UserProfile("Nino Nakano");
        nino.setStatus("High school student");
        nino.setFavoriteQuote("You can't handle the Nakano charm.");
        nino.setImagePath("nino_nakano.png");
        nino.addFriend("Sun Goku");
        nino.addFriend("Celine Minglana");
        profiles.put("Nino Nakano", nino);
        System.out.println("Added profile: Nino Nakano");

        UserProfile celine = new UserProfile("Celine Minglana");
        celine.setStatus("Adventurer");
        celine.setFavoriteQuote("Adventure awaits!");
        celine.setImagePath("celine_minglana.png");
        celine.addFriend("Sun Goku");
        celine.addFriend("Nino Nakano");
        profiles.put("Celine Minglana", celine);
        System.out.println("Added profile: Celine Minglana");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
