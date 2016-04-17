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
    String length = String.format("%02d", passphrase.length());
    this.openedKeyStore.create(passphrase, length);
  }

  public byte[] openExistingKeyStore(File newKeyStoreFile, String passphrase) {
    KeyStore newKeyStore = new KeyStore(newKeyStoreFile);
    byte[] content = newKeyStore.open(passphrase);
    if (content != null) {
      if (this.openedKeyStore != null) {
        // TODO: close currently opened keystore
        this.openedKeyStore.close();
      }
      this.openedKeyStore = newKeyStore;
    }
    return content;
  }

  public void closeKeyStore(){
//    curKeyStore.writeIntoFile(curFile);
//    FileEncryptionSubsystem.encryptFile(curFile, curPass, fileMap.get(curFile));
  }

//  public void generateNewAsymmetricKeyPair(){
//    //curKeyStore.generateKeyPairs();
//    try {
//      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//      keyGen.initialize(1024);
//      KeyPair pair = keyGen.generateKeyPair();
//      PrivateKey priv = pair.getPrivate();
//      PublicKey pub = pair.getPublic();
////      this.addKeyRing(Base64.getEncoder().encodeToString(priv.getEncoded()));
////      this.addKeyRing(Base64.getEncoder().encodeToString(pub.getEncoded()));
//    } catch (NoSuchAlgorithmException e) {
//      e.printStackTrace();
//    }
//  }

}
