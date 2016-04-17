package enc.controller;

import enc.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by winona on 18/4/2016.
 */
public class KeyPairInfoDialogController {
  @FXML private TextField pairName;
  @FXML private TextArea pairDescription;
  @FXML private Label nameWarning;
  @FXML private Label descWarning;

  @FXML // generate key pair
  private void onSubmit(ActionEvent event){
    if(pairName.getText().isEmpty()){
      nameWarning.setVisible(true);
    }
    if (pairDescription.getText().isEmpty()){
      descWarning.setVisible(true);
    }
    if(!pairName.getText().isEmpty() && !pairDescription.getText().isEmpty()){
      App.controller.saveKey(pairName.getText(),pairDescription.getText());
      ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }
  }
}
