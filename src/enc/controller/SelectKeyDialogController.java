package enc.controller;

import enc.App;
import enc.KeyPair;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by winona on 18/4/2016.
 */
public class SelectKeyDialogController {

  @FXML private ComboBox<String> nameListBox;

  @FXML
  public void initialize(){
    setNameListBox(App.akms.getKeyPairList());
  }

  @FXML
  private void keyChoosed(ActionEvent event){
    String keyName = nameListBox.getSelectionModel().getSelectedItem();
    App.controller.setPublicKeyName(keyName);
    ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
  }

  private void setNameListBox(ArrayList<KeyPair> keyList){
    ArrayList<String> name = new ArrayList<>();
    keyList.forEach(key -> name.add(key.getName()));
    nameListBox.setItems(FXCollections.observableArrayList(name));
  }

}
