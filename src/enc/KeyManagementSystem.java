package enc;

import java.io.File;
import java.util.Map;

/**
 * Created by winona on 2/4/2016.
 */
public class KeyManagementSystem {
  private Map<String, String> fileMap;
  private KeyStore curKeyStore;

  public void openKeyStore(String fileName, String passphrase){
    String encryptionMode= fileMap.get(fileName);

    FileEncryptionSubSystem.decryption(fileName, passphrase, encryptionMode);
    // TODO: file content to KeyStore instance
  }

  public void createKeyStore(String fileName, String pass, String encryptionMode){
    FileEncryptionSubSystem.setPassphrase(pass);
    fileMap.put(fileName, encryptionMode);
    curKeyStore = new KeyStore();
    File keyFile = new File(fileName);
  }

  public void closeKeyStore(){
    FileEncryptionSubSystem.encrytion();
  }

}
