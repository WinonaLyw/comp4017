package enc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/**
 * Created by winona on 17/4/2016.
 */
public class PassphraseController {
  @FXML private PasswordField passphraseField;
  @FXML
  private void checkPassphrase() {
    if(passphraseField.getText().length() != 8){
      passphraseField.clear();
    }else {

    }
  }
}
