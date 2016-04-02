package enc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by winona on 2/4/2016.
 */
public class FileEncryptionSubSystem {
  private static String passphrase;
  private static String encryptionOrDecryptionMode; // AES, DES, or 3-DES
  private static SecretKey key;

  public static void setPassphrase(String pass){
    passphrase = pass;
  }
  public static void setMethod(String method){ encryptionOrDecryptionMode = method; }

  public static void encrytion(){

  }

  // decrypt the encrypted file with password-base key and write into a "output.txt" file
  public static void decryption(String filename, String pass, String method){
    setPassphrase(pass);
    setMethod(method);
    generateKeyFromPassphrase();
    try {
      FileInputStream file = new FileInputStream(filename);
      Cipher cipher = Cipher.getInstance(encryptionOrDecryptionMode);
      cipher.init(Cipher.DECRYPT_MODE, key);
      CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("output.txt"), cipher);
      byte[] bytes = new byte[64];
      int numBytes;
      while ((numBytes = file.read(bytes)) != -1) {
        cos.write(bytes, 0, numBytes);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }

  }

  //generate a symmetric key from passphrase with a random salt
  public static void generateKeyFromPassphrase(){
    // generate a 20-byte salt
    Random r = new SecureRandom();
    byte[] salt = new byte[20];
    r.nextBytes(salt);

    try {
      SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 65536, 256); // 65536:key derivation iteration count, 256: the key size
      SecretKey tmp = kf.generateSecret(keySpec);
      key = new SecretKeySpec(tmp.getEncoded(), encryptionOrDecryptionMode);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }

  }

}
