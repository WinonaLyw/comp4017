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
  String sigFile; // for encAndSign and decAndVerify
  String pubName; // for decAndVerify
  String functionUsing = "encrypt"; // encrpyt, decrypt, createStore, openStore, closeStore, import, signature

  public void initStage(Stage stage){
    this.stage = stage;
  }

  @FXML private SplitPane splitPane;
  @FXML private TextArea console;



  @FXML
  private void createStore() {
    console.appendText("Action: Create a new key store\n");
    functionUsing = "createStore";
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
    functionUsing = "openStore";
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
    functionUsing = "generate";
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
      functionUsing = "export";
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
      functionUsing = "import";
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
      functionUsing = "closeStore";
      App.akms.closeKeyStore();
    }else {
      console.appendText("ERROR: Please open a key store first\n");
    }
  }


  @FXML
  private void encryptFile(ActionEvent event) throws IOException {
    console.appendText("Action: Open an file to encrypt\n");
    functionUsing = "encrypt";
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
    functionUsing = "decrypt";
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

  @FXML
  private void encryptAndSign() throws IOException{
    if(App.akms.openedKeyStore()){
      functionUsing = "encAndSign";
      console.appendText("Action: File Encryption and Digital Signature\n");
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

  @FXML
  private void verifySignature() throws IOException {
    if(App.akms.openedKeyStore()) {
      functionUsing = "verify";
      console.appendText("Action: verify signature\n");
      Stage dialog = new Stage();
      Parent root = FXMLLoader.load(getClass().getResource("../../view/SelectKeyDialog.fxml"));
      Scene scene = new Scene(root);
      dialog.setTitle("Select Public Key");
      dialog.setScene(scene);
      dialog.show();
    } else {
      console.appendText("ERROR: you should open a key store first\n");
    }
  }

  @FXML
  private void decryptAndVerif() throws IOException {
    if(App.akms.openedKeyStore()) {
      functionUsing = "decAndVerify";
      console.appendText("Action: file decryption and digital signature verification\n");
      Stage dialog = new Stage();
      Parent root = FXMLLoader.load(getClass().getResource("../../view/SelectKeyDialog.fxml"));
      Scene scene = new Scene(root);
      dialog.setTitle("Select Public Key");
      dialog.setScene(scene);
      dialog.show();
    } else {
      console.appendText("ERROR: you should open a key store first\n");
    }
  }

  // different action after get passphrase
  public void confirm(String passphrase,String methodEn) throws IOException {
    switch (functionUsing){
      case "createStore":
        App.akms.createNewKeyStore(new File(fileName), passphrase);
        console.appendText("Result: new key store opened\n");
        break;
      case "openStore":
        App.akms.openExistingKeyStore(new File(fileName), passphrase);
        console.appendText("Result: selected key store opened\n");
        break;
      case "closeStore":
        App.akms.closeKeyStore();
        break;
      case "encrypt":
        App.fes.encryptFile(fileName,passphrase,methodEn,false);
        console.appendText("Result: file encrypted\n");
        break;
      case "decrypt":
        App.fes.decryptFile(fileName,passphrase);
        console.appendText("Result: file decrypted\n");
        break;
      case "encAndSign":
        App.fes.encryptFile(fileName,passphrase,methodEn,false);
        App.fes.encryptFile(sigFile,passphrase,methodEn,false);
        break;
      case "decAndVerify":
        // decypt
        String resFile = App.fes.decryptFile(fileName,passphrase);
        String resSign =  App.fes.decryptFile(sigFile,passphrase);
        // verify
        if(App.fes.verifyDigitalSignature(pubName, resSign, resFile)){
          console.appendText("Result: selected signature is verified ^ ___ ^\n");
        }else {
          console.appendText("Result: selected signature is not verified T .... T\n");
        }
        break;
    }
  }
  public void saveKey(String name, String desc){
    switch(functionUsing){
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


  public void setPublicKeyName(String keyName) throws IOException {
    FileChooser fileChooser = new FileChooser();
    switch (functionUsing){
      case "export":
        console.appendText("Action: choose a file to save exported public key\n");
        File file = fileChooser.showSaveDialog(App.controller.stage);
        if(file != null){
          App.akms.exportPublicKey(keyName,file.getAbsolutePath());
        }
        break;
      case "verify":
        console.appendText("Action: open a data file\n");
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
          fileName = file.getPath();
          console.appendText("Action: open the to verify signature file\n");
          file = fileChooser.showOpenDialog(stage);
          if (file != null) {
            try {
              sigFile = file.getAbsolutePath();
              console.appendText("Signature key file: " + sigFile + "\n");
              if(App.fes.verifyDigitalSignature(keyName, sigFile, fileName)){
                console.appendText("Result: selected signature is verified ^ ___ ^\n");
              }else {
                console.appendText("Result: selected signature is not verified T .... T\n");
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        break;
      case "decAndVerify":
        console.appendText("Action: open an encrypted data file\n");
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
          fileName = file.getPath();
          console.appendText("Action: open an encrypted signature file\n");
          file = fileChooser.showOpenDialog(stage);
          if (file != null) {
            try {
              sigFile = file.getAbsolutePath();
              pubName = keyName;
              // get passphrase
              console.appendText("Action: get a passphrase for decryption\n");
              promptDecryptPassphrase();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        break;
    }
  }

  public void setSignature(String dataPath, String keyName, String sigName, String algorithm)
    throws IOException {
    App.fes.generateDigitalSignature(dataPath, App.akms.getPrivateKey(keyName),sigName,algorithm);
    if(functionUsing.equals("encAndSign")){
      this.fileName = dataPath;
      sigFile = sigName;
      promptEncryptPassphrase();
    }
  }

}