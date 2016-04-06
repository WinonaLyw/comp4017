package enc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by winona on 2/4/2016.
 */
public class FileEncryptionSubSystem {
  private static SecretKey key;
  private static byte[] iv = new byte[16];

  public static void encryption(String filename, String passphrase, String method){
    generateKeyFromPassphrase(passphrase, method);
    try{
      FileOutputStream outfile = new FileOutputStream("Encrypt.txt");
      Cipher cipher = Cipher.getInstance(method); //"AES/CBC/PKCS5Padding"); // "AES/CFB/NoPadding"); //encryptionOrDecryptionMode);
      // AES
      cipher.init(Cipher.ENCRYPT_MODE, key); //,new IvParameterSpec(iv));
      CipherInputStream cis = new CipherInputStream(new FileInputStream(filename), cipher);
      byte[] bytes = new byte[256];
      int numBytes;
      while ((numBytes = cis.read(bytes)) != -1) {
        outfile.write(bytes, 0, numBytes);
      }
      cis.close();
      outfile.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    //} catch (InvalidAlgorithmParameterException e) {
    //  e.printStackTrace();
    }
  }

  // decrypt the encrypted file with password-base key and write into a "output.txt" file
  public static void decryption(String filename, String passphrase, String method){
    generateKeyFromPassphrase(passphrase, method);
    try {
      FileInputStream file = new FileInputStream(filename);
      Cipher cipher = Cipher.getInstance(method); //"AES/CBC/PKCS5Padding"); // "AES/CFB/NoPadding"); //method);
      AlgorithmParameters params = cipher.getParameters();
      //iv = params.getParameterSpec(IvParameterSpec.class).getIV();
      cipher.init(Cipher.DECRYPT_MODE, key); //,new IvParameterSpec(iv));
      CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("Decrypt.txt"), cipher);
      byte[] bytes = new byte[256];
      int numBytes;
      while ((numBytes = file.read(bytes)) != -1) {
        cos.write(bytes, 0, numBytes);
      }
      file.close();
      cos.close();
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
    //} catch (InvalidParameterSpecException e) {
    //  e.printStackTrace();
    //} catch (InvalidAlgorithmParameterException e) {
    //  e.printStackTrace();
    }

  }

  //generate a symmetric key from passphrase with a random salt
  public static void generateKeyFromPassphrase(String passphrase, String method){
    // generate a 20-byte salt
    Random r = new SecureRandom();
    byte[] salt = new byte[20];
    r.nextBytes(salt);
    r.nextBytes(iv);

    try {
      SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithSHA1andDESede");
      // AES: PBKDF2WithHmacSHA256 256
      // DES: PBKDF2WithHmacSHA1 128
      // DESede:PBEWithSHA1andDESede (no need to specify key length)
      PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 65536); // 65536:key derivation iteration count, 256: the key size
      SecretKey tmp = kf.generateSecret(keySpec);
      key = new SecretKeySpec(tmp.getEncoded(), method);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
    System.out.println(passphrase);
    System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

  }

}
