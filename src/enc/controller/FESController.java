package enc.controller;

import enc.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FESController{
  Stage stage;
  String fileName;
  String function = "encrypt"; // encrpyt, decrypt, createStore, openStore, closeStore, import, signature

  public void initStage(Stage stage){
    this.stage = stage;
  }

  @FXML private SplitPane splitPane;
  @FXML private TextArea console;



  @FXML
  private void createStore() {
    console.appendText("Action: Create a new key store\n");
    function = "createStore";
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Create New Key Store");
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
  private void openStore() {
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
    function = "generate";
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


  @FXML
  private void exportKey() throws IOException {
    // check if a key store opened
    if (App.akms.openedKeyStore()){
      console.appendText("Action: export public key\n");
      function ="export";
      Stage dialog = new Stage();
      dialog.setTitle("Set Key Pair Information");
      Parent root = FXMLLoader.load(getClass().getResource("../../view/SelectKeyDialog.fxml"));
      Scene scene = new Scene(root);
      dialog.setScene(scene);
      dialog.show();
    }else {
      console.appendText("ERROR: Please open a key store first\n");
    }

  }

  @FXML
  private void importKey(){
    // check if a key store opened
    if (App.akms.openedKeyStore()){
      console.appendText("Action: export public key\n");
      function = "import";
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Public Key File");
      File file = fileChooser.showOpenDialog(stage);
      if (file != null) {
        try {
          fileName = file.getAbsolutePath();
          console.appendText("Public key file: " + fileName + "\n");
          Stage dialog = new Stage();
          dialog.setTitle("Set Key Pair Information");
          Parent root = FXMLLoader.load(getClass().getResource("../../view/KeyPairInfoDialog.fxml"));
          Scene scene = new Scene(root);
          dialog.setScene(scene);
          dialog.show();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }else {
      console.appendText("ERROR: Please open a key store first\n");
    }

  }

  @FXML
  private void closeStore(){
    // check if a key store opened
    if (App.akms.openedKeyStore()){
      console.appendText("Action: close the key store\n");
      function = "closeStore";
      App.akms.closeKeyStore();
    }else {
      console.appendText("ERROR: Please open a key store first\n");
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


  public void promptEncryptPassphrase() throws IOException {
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/PassphraseDialog.fxml"));
    Scene scene = new Scene(root);
    dialog.setScene(scene);
    dialog.show();
  }

  // decryption will not prompt to set method
  private void promptDecryptPassphrase() throws IOException {
    Stage dialog = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource("../../view/PassphraseDialog.fxml"));
    Scene scene = new Scene(root);
    BorderPane pane = (BorderPane) scene.lookup("#encryptionMethodPane");
    pane.setCenter(null);
    dialog.setScene(scene);
    dialog.show();
  }

  @FXML
  private void generateSignature() throws IOException {
    if(App.akms.openedKeyStore()){
      console.appendText("Action: generate signature\n");
      Stage dialog = new Stage();
      Parent root = FXMLLoader.load(getClass().getResource("../../view/GenerateSignatureDialog.fxml"));
      Scene scene = new Scene(root);
      dialog.setTitle("Generate Signature");
      dialog.setScene(scene);
      dialog.show();
    } else {
      console.appendText("ERROR: you should open a key store first\n");
    }
  }


  // different action after get passphrase
  public void confirm(String passphrase,String methodEn) throws IOException {
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
        App.fes.decryptFile(fileName,passphrase,methodEn);
        console.appendText("Result: file decrypted\n");
        break;
    }

  }
  public void saveKey(String name, String desc){
    switch(function){
      case "generate":
        App.akms.generateNewAsymmetricKeyPair(name,desc);
        console.appendText("Result: key pair generated\n");
        break;
      case "import":
        App.akms.importPublicKey(fileName,name,desc);
        console.appendText("Result: key imported\n");
        break;
    }
  }

  public void setSignatureInfo(String keyName, String algo){
    App.fes.generateDigitalSignature(App.akms.getPrivateKey(keyName),fileName,algo);
  }

}