package enc;

import java.io.File;

/**
 * Created by winona on 2/4/2016.
 */
public class AsymmetricKeyManagementSubsystem {
  // This class should be used as single instance
  private static AsymmetricKeyManagementSubsystem instance = new AsymmetricKeyManagementSubsystem();
  private AsymmetricKeyManagementSubsystem() {
    this.openedKeyStore = null;
  }
  public static AsymmetricKeyManagementSubsystem getInstance() {
    return instance;
  }

  private KeyStore openedKeyStore;

  public void createNewKeyStore(File keyStoreFile, String passphrase) {
    this.openedKeyStore = new KeyStore(keyStoreFile);
    this.openedKeyStore.create(passphrase);
  }

  public byte[] openExistingKeyStore(File newKeyStoreFile, String passphrase) {
    KeyStore newKeyStore = new KeyStore(newKeyStoreFile);
    byte[] content = newKeyStore.open(passphrase);
    //if (content != null) {
      if (this.openedKeyStore != null) {
        this.openedKeyStore.close();
      }
      this.openedKeyStore = newKeyStore;
    //}
    return content;
  }

  public void closeKeyStore(){
//    curKeyStore.writeIntoFile(curFile);
//    FileEncryptionSubsystem.encryptFile(curFile, curPass, fileMap.get(curFile));
  }

  public boolean openedKeyStore(){
    return (openedKeyStore != null);
  }

  public void generateNewAsymmetricKeyPair(String name, String desc){
    openedKeyStore.generateKeyPairs(name,desc);
  }

}
