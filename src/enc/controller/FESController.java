package enc.controller;

import enc.App;
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
  Stage stage;

  public void initStage(Stage stage){
    this.stage = stage;
  }

  @FXML
  private SplitPane splitPane;

  @FXML
  private void createStore(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save New Key Store");
    File file = fileChooser.showSaveDialog(stage);
    if (file != null) {
      try {
        // TODO: prompt for passphrase
        String passphrase = "password";
        App.akms.createNewKeyStore(file, passphrase);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void openStore(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Key Store");
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      try {
        // TODO: prompt for passphrase
        String passphrase = "password";
        App.akms.openExistingKeyStore(file, passphrase);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
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