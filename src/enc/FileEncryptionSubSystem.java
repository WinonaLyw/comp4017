package enc;

import enc.controller.FileEncryptionController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;
import java.util.StringJoiner;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by winona on 2/4/2016.
 */
public class FileEncryptionSubSystem {
  private static final String PADDING_MODE = "CBC/PKCS5Padding";

  public static void encryptFile(String plaintextPath, String passphrase, String method) {
    try {
      Path path = Paths.get(plaintextPath);
//      String plaintextFileName = path.getFileName().toString();
//      String encryptedFileName = plaintextFileName + ""
//
//
      FileOutputStream fos = new FileOutputStream("Encrypt.txt");

      byte[] salt = generateSalt();
      SecretKey key = generateKeyFromPassphrase(passphrase, method, salt);

      fos.write(salt);

      String algorithm = getCipherAlgorithm(method);
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      AlgorithmParameters params = cipher.getParameters();
      byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();


      CipherInputStream cis = new CipherInputStream(new FileInputStream(plaintextPath), cipher);
      byte[] bytes = new byte[256];
      int numBytes;
      while ((numBytes = cis.read(bytes)) != -1) {
        fos.write(bytes, 0, numBytes);
      }
      cis.close();
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
    decrypt the encrypted file with password-base key and write into a "output.txt" file
   */
  public static void decryptFile(String filename, String passphrase, String method) {
    try {
      FileInputStream fis = new FileInputStream(filename);
      byte[] salt = new byte[20];
      fis.read(salt);
      SecretKey key = generateKeyFromPassphrase(passphrase, method, salt);

      String algorithm = getCipherAlgorithm(method);
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, key);
      CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("Decrypt.txt"), cipher);
      byte[] bytes = new byte[256];
      int numBytes;
      while ((numBytes = fis.read(bytes)) != -1) {
        cos.write(bytes, 0, numBytes);
      }
      fis.close();
      cos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String getCipherAlgorithm(String method) {
    String algorithm = method + "/" + PADDING_MODE;
    return algorithm;
  }

  private static byte[] generateSalt() {
    // generate a 20-byte salt
    Random r = new SecureRandom();
    byte[] salt = new byte[20];
    r.nextBytes(salt);
    return salt;
  }

  /*
    generate a symmetric key from passphrase with a random salt
   */
  public static SecretKey generateKeyFromPassphrase(String passphrase, String method, byte[] salt) {
    //r.nextBytes(iv);

    String algorithm = null;
    PBEKeySpec pbeKeySpec = null;
    // AES: PBKDF2WithHmacSHA256 256
    // DES: PBKDF2WithHmacSHA1 128
    // DESede:PBEWithSHA1andDESede (no need to specify key length)

    if (method.equals("DES")) {
      algorithm = "PBEWithMD5andDES";
      pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 65536);
      // 65536:key derivation iteration count, 256: the key size
    } else if (method.equals("AES")) {
      algorithm = "PBKDF2WithHmacSHA1";
      pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 65536, 256);
    } else if (method.equals("DESede")) {
      algorithm = "PBEWithSHA1andDESede";
      pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 65536);
    }

    SecretKey key = null;
    try {
      SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);
      SecretKey tmp = kf.generateSecret(pbeKeySpec);
      key = new SecretKeySpec(tmp.getEncoded(), method);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(passphrase);
    System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

    return key;
  }
}
