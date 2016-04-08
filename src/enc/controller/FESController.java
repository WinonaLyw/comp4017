package enc.controller;


import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FESController{
  Stage stage;
  final FileChooser fileChooser = new FileChooser();

  public void onCreateNewFile(){
    fileChooser.setTitle("Select Key Store File");
    fileChooser.setInitialDirectory(
      new File(System.getProperty("user.dir") + File.separator)
    );
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      openFile(file);
    }
  }

  public void initStage(Stage stage){
    this.stage = stage;
  }


  private void openFile(File file){

  }

}