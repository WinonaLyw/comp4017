package enc;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

/**
 * Created by winona on 2/4/2016.
 */
public class KeyManagementSystem {
  private Map<String, String> fileMap;
  private String curFile;

  public void openKeyStore(String fileName, String passphrase){
    String encryptionMode= fileMap.get(fileName);
    curFile = fileName;
    FileEncryptionSubSystem.decryption(fileName, passphrase, encryptionMode);
    // TODO: file content to KeyStore instance
  }

  public void createKeyStore(String fileName, String pass, String encryptionMode){
    fileMap.put(fileName, encryptionMode);
    File keyFile = new File(fileName);
  }

  public void closeKeyStore(){
    FileEncryptionSubSystem.encryption(curFile,"", fileMap.get(curFile));
  }

  public void generateNewKeys(){
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
      keyGen.initialize(1024, random);
      KeyPair pair = keyGen.generateKeyPair();
      PrivateKey priv = pair.getPrivate();
      PublicKey pub = pair.getPublic();

      System.out.println(Base64.getEncoder().encodeToString(priv.getEncoded()));
      System.out.println(Base64.getEncoder().encodeToString(pub.getEncoded()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }

  }

}
