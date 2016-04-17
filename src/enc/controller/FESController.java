package enc.controller;

import enc.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FESController{
  Stage stage;

  public void initStage(Stage stage){
    this.stage = stage;
  }

  @FXML private SplitPane splitPane;
  @FXML private Pane methodPane;  // MethodSelection view
  @FXML private BorderPane encryptionMethodPane; // Passphrase view

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
    File file = (new FileChooser()).showOpenDialog(new Stage());
    if (file != null) {
      promptEncryptPassphrase();
      // TODO: text area
      //lblFileName.setText("File name: " + file.getAbsolutePath());
    }
  }

  @FXML
  private void decryptFile(ActionEvent event) throws IOException {
    File file = (new FileChooser()).showOpenDialog(new Stage());
    if (file != null) {
      promptDecryptPassphrase();
      // TODO: text area
      //lblFileName.setText("File name: " + file.getAbsolutePath());
    }
  }

  @FXML
  public void promptEncryptPassphrase() throws IOException {
    FXMLLoader.load(getClass().getResource("../view/MethodSelection.fxml"));
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/Passphrase.fxml"));
    Scene scene = new Scene(root);
    dialog.setScene(scene);
    dialog.show();
  }

  private void promptDecryptPassphrase() throws IOException {
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/Passphrase.fxml"));
    Scene scene = new Scene(root);
    BorderPane pane = (BorderPane) scene.lookup("#encryptionMethodPane");
    pane.setCenter(null);
    dialog.setScene(scene);
    dialog.show();
  }


  // Passphrase
  @FXML private PasswordField passphraseField;
  @FXML private PasswordField confirmField;

  private String passphraseEn;
  private String methodEn = "DES";
  @FXML
  private void confirmInfo() {
    if(passphraseField.getText().length() != 8 || !passphraseField.getText().equals(confirmField.getText())){
      passphraseField.clear();
      confirmField.clear();
    }else {
      passphraseEn = passphraseField.getText();
    }
  }

  @FXML
  private void onRadioClick(ActionEvent event){
    methodEn = ((RadioButton) event.getSource()).getText();
    System.out.println(methodEn);
  }
}