/*Main entrance of the project*/

package enc;

import enc.controller.FESController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static final FileEncryptionSubSystem fes = FileEncryptionSubSystem.getInstance();
  public static final AsymmetricKeyManagementSubsystem akms = AsymmetricKeyManagementSubsystem.getInstance();

  private static String keyStoreFilePath = "keyTest.txt";

  public static void main(String[] args) {
    // Test
    // encrypt/ decrypt test
//    String method = "DESede";
//    fes.encryptFile("src/test.txt", "password", method);
//    fes.decryptFile("test_Encrypted.txt","password", method);

    // signature test
//    String algorithm = "MD5withRSA";
//    FileEncryptionSubsystem.generateDigitalSignature("src/test.txt", algorithm);

    // generate key test
//    try {
//      kgs.createKeyStore(keyStoreFilePath,"password","DES");
//    } catch (IOException e) {
//      System.out.println("File exists!");
//      e.printStackTrace();
//    }
//    kgs.generateNewAsymmetricKeyPair();
////    kgs.closeKeyStore();
//
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