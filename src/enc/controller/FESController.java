package enc.controller;

import enc.App;
import java.nio.file.OpenOption;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
  @FXML private TextArea console;
  @FXML private TextField pairName;
  @FXML private TextArea pairDescription;
  @FXML private Label nameWarning;
  @FXML private Label descWarning;

  @FXML
  private void createStore() {
    console.appendText("Action: Create a new key store\n");
    function = "createStore";
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save New Key Store");
    File file = fileChooser.showSaveDialog(stage);
    if (file != null) {
      try {
        fileName = file.getAbsolutePath();
        console.appendText("Key store file: " + fileName + "\n");
        promptDecryptPassphrase();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void openStore(ActionEvent event) {
    console.appendText("Action: Open an existed key store\n");
    function = "openStore";
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Key Store");
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      try {
        fileName = file.getAbsolutePath();
        console.appendText("Key store file: " + fileName + "\n");
        promptDecryptPassphrase();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void generateKeyPairs() throws IOException {
    if (App.akms.openedKeyStore()){
      console.appendText("Action: Generate a key pair\n");
      Stage dialog = new Stage();
      dialog.setTitle("Set Key Pair Information");
      Parent root = FXMLLoader.load(getClass().getResource("../../view/KeyPairInfoDialog.fxml"));
      Scene scene = new Scene(root);
      dialog.setScene(scene);
      dialog.show();
    }else {
      console.appendText("ERROR: Please open a key store first\n");
    }
  }

  @FXML // generate key pair
  private void onSubmit(ActionEvent event){
    if(pairName.getText().isEmpty()){
      nameWarning.setVisible(true);
    }
    if (pairDescription.getText().isEmpty()){
      descWarning.setVisible(true);
    }
    if(!pairName.getText().isEmpty() && !pairDescription.getText().isEmpty()){
      App.akms.generateNewAsymmetricKeyPair(pairName.getText(),pairDescription.getText());
      ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
      console.appendText("Result: key pair generated\n");
    }
  }

  @FXML
  private void encryptFile(ActionEvent event) throws IOException {
    console.appendText("Action: Open an file to encrypt\n");
    function = "encrypt";
    File file = (new FileChooser()).showOpenDialog(new Stage());
    if (file != null) {
      fileName = file.getAbsolutePath();
      console.appendText("File to encrypt: " + fileName + "\n");
      promptEncryptPassphrase();
    }
  }

  @FXML
  private void decryptFile(ActionEvent event) throws IOException {
    console.appendText("Action: Open an file to decrypt\n");
    function = "decrypt";
    File file = (new FileChooser()).showOpenDialog(new Stage());
    if (file != null) {
      fileName = file.getAbsolutePath();
      promptDecryptPassphrase();
      console.appendText("File to decrypt: " + fileName + "\n");
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

  public void confirm(String passphrase,String methodEn){
    switch (function){
      case "createStore":
        App.akms.createNewKeyStore(new File(fileName), passphrase);
        console.appendText("Result: new key store opened\n");
        break;
      case "openStore":
        App.akms.openExistingKeyStore(new File(fileName), passphrase);
        console.appendText("Result: selected key store opened\n");
        break;
      case "closeStore": break;
      case "encrypt":
        App.fes.encryptFile(fileName,passphrase,methodEn,false);
        console.appendText("Result: file encrypted\n");
        break;
      case "decrypt":
        // TODO: method
        App.fes.decryptFile(fileName,passphrase,methodEn,false);
        console.appendText("Result: file decrypted\n");
        break;
    }

  }

}