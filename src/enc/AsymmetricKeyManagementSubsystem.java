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
    if (this.openedKeyStore != null) {
      this.openedKeyStore.close();
      this.openedKeyStore = null;
    }
    this.openedKeyStore = new KeyStore(keyStoreFile);
    this.openedKeyStore.create(passphrase);
  }

  public byte[] openExistingKeyStore(File newKeyStoreFile, String passphrase) {

    //if (content != null) {
      if (this.openedKeyStore != null) {
        System.out.println("has opened");
        this.openedKeyStore.close();
        this.openedKeyStore = null;
      }
    //KeyStore newKeyStore = new KeyStore(newKeyStoreFile);
//    byte[] content = newKeyStore.open(passphrase, false);
    this.openedKeyStore = new KeyStore(newKeyStoreFile);
    this.openedKeyStore.open(passphrase, false);
    //}
//    return content;
    return null;
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

  public void importPublicKey(String filename, String name, String desc){
    // TODO: read file get key ring
    openedKeyStore.importPublicKey(new KeyRing());
  }

  public void exportPublicKey(String keyName, File file){
    //TODO: get public key and export

  }

}
