package enc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winona on 2/4/2016.
 */
public class KeyManagementSystem {
  private Map<String, String> fileMap = new HashMap<>();
  private String curFile;
  private String curPass;
  private KeyStore curKeyStore;

  public void openKeyStore(String fileName, String passphrase){
    String encryptionMode= fileMap.get(fileName);
    curFile = fileName;
    curPass = passphrase;
    FileEncryptionSubSystem.decryption(fileName, curPass, encryptionMode);
    curKeyStore = new KeyStore();
    curKeyStore.loadFromFile(fileName);
  }

  public void createKeyStore(String fileName, String pass, String encryptionMode)
    throws IOException {
    this.curPass = pass;
    fileMap.put(fileName, encryptionMode);
    curFile = fileName;
    curKeyStore = new KeyStore();
    File file = new File(curFile);
    file.createNewFile();
  }

  public void closeKeyStore(){
    curKeyStore.writeIntoFile(curFile);
    FileEncryptionSubSystem.encryption(curFile, curPass, fileMap.get(curFile));
  }

  public void generateNewKeys(){
    curKeyStore.generateKeyPairs();
  }

}
