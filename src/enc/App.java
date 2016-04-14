/*Main entrance of the project*/

package enc;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static void main(String[] args) {

    // encrypt/ decrypt test
    //FileEncryptionSubSystem.encryption("src/test.txt", "password", "DES");
    //FileEncryptionSubSystem.decryption("encrypt.txt","password","DES");

    // generate key test
    //KeyManagementSystem kgs = new KeyManagementSystem();
    //try {
    //  kgs.createKeyStore("keyTest.txt","password","DES");
    //} catch (IOException e) {
    //  System.out.println("File exists!");
    //  e.printStackTrace();
    //}
    //kgs.generateNewKeys();
    //kgs.closeKeyStore();

    Application.launch(App.class, args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    final FXMLLoader loader =
      new FXMLLoader(getClass().getResource("../view/FileEncryptionSystem.fxml"));

    final Parent root = loader.load();
    final FESController controller = loader.getController();
    controller.initStage(primaryStage);

    primaryStage.setScene(new Scene(root, 600, 550));
    primaryStage.show();
  }
}