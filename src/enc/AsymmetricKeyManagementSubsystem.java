package enc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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

//  private Map<String, String> fileMap = new HashMap<>();
//  private String curFile;
//  private String curPass;

  private KeyStore openedKeyStore;

  private static final String KEYSTORE_TYPE = "jks";

  public void createKeyStore(File keyStoreFile, String passphrase) {
    // TODO: Passphrase cannot exceed 20 bits
    String length = String.format("%02d", passphrase.length());
    this.openedKeyStore = new KeyStore(keyStoreFile, passphrase, length);

//    this.curPass = pass;
//    fileMap.put(keyStoreFilePath, encryptionMode);
//    curFile = keyStoreFilePath;
//    curKeyStore = new KeyStore();
//    File file = new File(curFile);
//    file.createNewFile();
  }

  public void openKeyStore() {
    if (this.openedKeyStore != null) {
      // TODO: close currently opened keystore
      this.openedKeyStore.close();
    }
    // TODO: open an existing keystore
  }

//  public void openKeyStore(String keyStoreFilePath, String passphrase){
//    try {
//      this.openedKeyStore = KeyStore.getInstance(KEYSTORE_TYPE);
//      FileInputStream fis = new FileInputStream(keyStoreFilePath);
//      this.openedKeyStore.load(fis, passphrase.toCharArray());
//      fis.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
////    String encryptionMode= fileMap.get(fileName);
////    curFile = fileName;
////    curPass = passphrase;
////    FileEncryptionSubsystem.decryptFile(fileName, curPass, encryptionMode);
////    curKeyStore = new KeyStore();
////    curKeyStore.loadFromFile(fileName);
//  }

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
