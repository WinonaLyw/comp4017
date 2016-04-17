package enc.controller;

import enc.App;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by winona on 17/4/2016.
 */
public class PassphraseDialogController {

  private String fileName;
  private String function;

  // Passphrase
  @FXML private PasswordField passphraseField;
  @FXML private PasswordField confirmField;

  private String methodEn = "DES";
  @FXML
  private void confirmInfo(ActionEvent event) {
    System.out.println(this.fileName);
    if(passphraseField.getText().length() != 8 || !passphraseField.getText().equals(confirmField.getText())){
      passphraseField.clear();
      confirmField.clear();
    }else {
      App.controller.confirm(passphraseField.getText(),methodEn);
      ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }
  }

  @FXML
  private void onRadioClick(ActionEvent event){
    methodEn = ((RadioButton) event.getSource()).getText();
  }


  public void setInfo(String fileName, String function){
    this.fileName = fileName;
    this.function = function;
  }



}
