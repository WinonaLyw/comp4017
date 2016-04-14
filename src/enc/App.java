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
    String method = "DES";
    FileEncryptionSubSystem.encryptFile("src/test.txt", "password", method);
    FileEncryptionSubSystem.decryptFile("encrypt.txt","password", method);

    // generate key test
    KeyManagementSystem kgs = new KeyManagementSystem();
    try {
      kgs.createKeyStore("keyTest.txt","password","DES");
    } catch (IOException e) {
      System.out.println("File exists!");
      e.printStackTrace();
    }
    kgs.generateNewKeys();
    kgs.closeKeyStore();

    Application.launch(App.class, args);
  }
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../view/FileEncryptionSystem.fxml"));
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();
  }
}