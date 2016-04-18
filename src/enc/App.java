/*Main entrance of the project*/

package enc;

import enc.controller.FESController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static final FileEncryptionSubsystem fes = FileEncryptionSubsystem.getInstance();
  public static final AsymmetricKeyManagementSubsystem akms = AsymmetricKeyManagementSubsystem.getInstance();
  public static FESController controller;

  public static void main(String[] args) {
    Application.launch(App.class, args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    final FXMLLoader loader =
      new FXMLLoader(getClass().getResource("../view/FileEncryptionSystem.fxml"));

    final Parent root = loader.load();
    controller = loader.getController();
    controller.initStage(primaryStage);

    primaryStage.setScene(new Scene(root, 600, 500));
    primaryStage.show();
  }
}