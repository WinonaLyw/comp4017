package enc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FileEncryptionController {
  @FXML
  private GridPane gridPane;
  @FXML
  private Label lblFileName;

  private FileChooser fileChooser;

  public FileEncryptionController() {
    this.fileChooser = new FileChooser();
  }

  @FXML
  private void openFileChooser() throws IOException {
    File file = this.fileChooser.showOpenDialog(new Stage());
    promptPassphrase();
    if (file != null) {
      lblFileName.setText("File name: " + file.getAbsolutePath());
    }
  }

  @FXML
  private void backToMainMenu() throws IOException {
    Stage stage = (Stage) gridPane.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/FileEncryptionSystem.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  private void promptPassphrase() throws IOException {
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/passphrase.fxml"));
    Scene scene = new Scene(root);
    dialog.setScene(scene);
    dialog.show();
  }
}
