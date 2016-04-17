package enc.controller;

import enc.App;
import java.nio.file.OpenOption;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FESController{
  Stage stage;
  String fileName;
  String function = "encrypt"; // encrpyt, decrypt, createStore, openStore, closeStore

  public void initStage(Stage stage){
    this.stage = stage;
  }

  @FXML private SplitPane splitPane;
  @FXML private Pane methodPane;  // MethodSelection view
  @FXML private TextField pairName;
  @FXML private TextArea pairDescription;
  @FXML private Label nameWarning;
  @FXML private Label descWarning;

  @FXML
  private void createStore() {
    function = "createStore";
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save New Key Store");
    File file = fileChooser.showSaveDialog(stage);
    if (file != null) {
      try {
        fileName = file.getAbsolutePath();
        promptDecryptPassphrase();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void openStore(ActionEvent event) {
    function = "openStore";
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Key Store");
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      try {
        fileName = file.getAbsolutePath();
        promptDecryptPassphrase();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void generateKeyPairs() throws IOException {
    if (App.akms.openedKeyStore()){
      Stage dialog = new Stage();
      dialog.setTitle("Set Key Pair Information");
      Parent root = FXMLLoader.load(getClass().getResource("../../view/KeyPairInfoDialog.fxml"));
      Scene scene = new Scene(root);
      dialog.setScene(scene);
      dialog.show();
    }else {

      //TODO: text area
    }
  }

  @FXML // generate key pair
  private void onSubmit(){
    if(pairName.getText().isEmpty()){
      nameWarning.setVisible(true);
    }
    if (pairDescription.getText().isEmpty()){
      descWarning.setVisible(true);
    }
    if(!pairName.getText().isEmpty() && !pairDescription.getText().isEmpty()){
      App.akms.generateNewAsymmetricKeyPair(pairName.getText(),pairDescription.getText());
    }
  }

  @FXML
  private void encryptFile(ActionEvent event) throws IOException {
    function = "encrypt";
    File file = (new FileChooser()).showOpenDialog(new Stage());
    if (file != null) {
      fileName = file.getAbsolutePath();
      promptEncryptPassphrase();
      // TODO: text area
      //lblFileName.setText("File name: " + file.getAbsolutePath());
    }
  }

  @FXML
  private void decryptFile(ActionEvent event) throws IOException {
    function = "decrypt";
    File file = (new FileChooser()).showOpenDialog(new Stage());
    if (file != null) {
      fileName = file.getAbsolutePath();
      promptDecryptPassphrase();
      // TODO: text area
      //lblFileName.setText("File name: " + file.getAbsolutePath());
    }
  }

  @FXML
  public void promptEncryptPassphrase() throws IOException {
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/PassphraseDialog.fxml"));
    Scene scene = new Scene(root);
    dialog.setScene(scene);
    dialog.show();
  }

  private void promptDecryptPassphrase() throws IOException {
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/PassphraseDialog.fxml"));
    Scene scene = new Scene(root);
    BorderPane pane = (BorderPane) scene.lookup("#encryptionMethodPane");
    pane.setCenter(null);
    dialog.setScene(scene);
    dialog.show();
  }


  // Passphrase
  @FXML private PasswordField passphraseField;
  @FXML private PasswordField confirmField;

  private String methodEn = "DES";
  @FXML
  private void confirmInfo() {
    if(passphraseField.getText().length() != 8 || !passphraseField.getText().equals(confirmField.getText())){
      passphraseField.clear();
      confirmField.clear();
    }else {
      switch (function){
        case "createStore":
          App.akms.createNewKeyStore(new File(fileName), passphraseField.getText());
          break;
        case "openStore":
          App.akms.openExistingKeyStore(new File(fileName), passphraseField.getText());
          break;
        case "closeStore": break;
        case "encrypt":
          System.out.print(fileName);
          App.fes.encryptFile(fileName,passphraseField.getText(),methodEn,false);
          break;
        case "decrypt":
          // TODO: method
          App.fes.decryptFile(fileName,passphraseField.getText(),methodEn, false);
          break;
      }

    }
  }

  @FXML
  private void onRadioClick(ActionEvent event){
    methodEn = ((RadioButton) event.getSource()).getText();
    System.out.println(methodEn);
  }
}