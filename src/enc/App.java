/*Main entrance of the project*/

package enc;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(App.class, args);
  }
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../view/FileEncryptionSystem.fxml"));
    primaryStage.show();
  }
}