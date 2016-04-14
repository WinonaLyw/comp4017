package enc.controller;

import enc.FileEncryptionSubSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FESController{
  @FXML
  private SplitPane splitPane;

  @FXML
  private void createStore(ActionEvent event) {
  }

  @FXML
  private void openStore(ActionEvent event) {

  }

  @FXML
  private void encryptFile(ActionEvent event) throws IOException {
    Stage stage = (Stage) splitPane.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/FileEncryption.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void decryptFile(ActionEvent event) {
  }
}