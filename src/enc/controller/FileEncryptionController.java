package enc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
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
  private void openFileChooser() {
    File file = this.fileChooser.showOpenDialog(new Stage());
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
}
