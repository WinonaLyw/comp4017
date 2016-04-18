package enc.controller;

import enc.App;
import enc.KeyPair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by winona on 18/4/2016.
 */
public class GenerateSignatureDialogController {
  @FXML private ComboBox<String> nameListBox;

  private String algorithm = "MD5";
  @FXML
  public void initialize(){
    setNameListBox(App.akms.getKeyPairList());
  }

  @FXML
  private void generateSignature(ActionEvent event) throws IOException {
    String keyName = nameListBox.getSelectionModel().getSelectedItem();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Data File to Sign");
    File file = fileChooser.showOpenDialog(App.controller.stage);
    if (file != null) {
      String dataPath = file.getPath();
      fileChooser = new FileChooser();
      fileChooser.setTitle("Save Signature File");
      file = fileChooser.showSaveDialog(App.controller.stage);
      if(file != null){
        App.controller.setSignature(dataPath, keyName,file.getAbsolutePath(),algorithm);
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
      }
    }

  }

  @FXML
  private void setAlgorithm(ActionEvent e){
    this.algorithm = ((RadioButton) e.getSource()).getText();
  }

  private void setNameListBox(ArrayList<KeyPair> keyList){
    ArrayList<String> name = new ArrayList<>();
    keyList.forEach(key -> name.add(key.getName()));
    nameListBox.setItems(FXCollections.observableArrayList(name));
  }
}
