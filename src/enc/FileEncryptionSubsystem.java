package enc;

import java.io.*;
import java.nio.file.Paths;
import java.security.*;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by winona on 2/4/2016.
 */
public class FileEncryptionSubsystem {
  // This class should be used as single instance
  private static FileEncryptionSubsystem instance = new FileEncryptionSubsystem();
  private FileEncryptionSubsystem() {}
  public static FileEncryptionSubsystem getInstance() {
    return instance;
  }

  private static final String PADDING_MODE = "CBC/PKCS5Padding";
  private static final int SALT_LENGTH = 20;
  private static final int ITERATION_COUNT = 65536;

  public void encryptFile(String plaintextPath, String passphrase, String method, boolean isKeyStoreFile) {
    try {
      String ciphertextPath = null;
      // 1. Get output encrypted file path
      if (!isKeyStoreFile) {
        ciphertextPath = plaintextPath.substring(0, plaintextPath.lastIndexOf("."));
      } else {
         ciphertextPath = getOutputFilePath(plaintextPath, "enc");
      }

      FileOutputStream fos = new FileOutputStream(ciphertextPath);

      // 2. Write method to file (6 bytes)
      if (method.length() == 3) {
        fos.write(method.getBytes());
        fos.write(method.getBytes());
      } else {
        fos.write(method.getBytes());
      }

      // 3. Generate salt and write it to file
      byte[] salt = generateSalt();
      fos.write(salt);

      // 4. Initialize cipher
      Cipher cipher = getCipherInstance(passphrase, method, salt, null);

      // 5. Get IV and write it to file
      AlgorithmParameters params = cipher.getParameters();
      byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
      fos.write(iv);

      // 6. Write encrypted file
      CipherInputStream cis = new CipherInputStream(new FileInputStream(plaintextPath), cipher);
      byte[] bytes = new byte[256];
      int numBytes;
      while ((numBytes = cis.read(bytes)) != -1) {
        fos.write(bytes, 0, numBytes);
      }
      cis.close();
      fos.close();

      // 7. Encrypted keystore file will replace the plaintext file
      if (isKeyStoreFile) {
        File plainKeyStoreFile = new File(plaintextPath);
        plainKeyStoreFile.delete();
        File cipherKeyStoreFile = new File(ciphertextPath);
        cipherKeyStoreFile.renameTo(plainKeyStoreFile);
      } else {
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
    decrypt the encrypted file with password-base key and write into a "output.txt" file
   */
  public String decryptFile(String ciphertextPath, String passphrase, String haha) {
    String decryptedFilePath = null;
    try {
      decryptedFilePath = getOutputFilePath(ciphertextPath, "dec");
      FileInputStream fis = new FileInputStream(ciphertextPath);

      // 1. Read method
      byte[] methodBytes = new byte[6];
      fis.read(methodBytes);
      String method = new String(methodBytes);
      if (method.charAt(3) != 'e') {
        method = method.substring(0, 3);
      }

      // 2. Read salt
      byte[] salt = new byte[SALT_LENGTH];
      fis.read(salt);

      // 3. Read IV
      int ivLength = getIVLength(method);
      byte[] iv = new byte[ivLength];
      fis.read(iv);

      // 4. Initialize cipher
      Cipher cipher = getCipherInstance(passphrase, method, salt, iv);

      // 5. Write decrypted file
      CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(decryptedFilePath), cipher);
      byte[] bytes = new byte[256];
      int numBytes;
      while ((numBytes = fis.read(bytes)) != -1) {
        cos.write(bytes, 0, numBytes);
      }
      fis.close();
      cos.flush();
      cos.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return decryptedFilePath;
    }
  }

  private Cipher getCipherInstance(String passphrase, String method, byte[] salt, byte[] iv) {
    Cipher cipher = null;
    try {
      SecretKey key = generateKeyFromPassphrase(passphrase, method, salt);
      String algorithm = getCipherAlgorithm(method);
      cipher = Cipher.getInstance(algorithm);
      if (iv == null) {
        cipher.init(Cipher.ENCRYPT_MODE, key);
      } else {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return cipher;
    }
  }

  public void generateDigitalSignature(String dataPath, Key priv, String filePath, String algorithm) {
    //if (algorithm.equals("SHA1")) {
      algorithm = "SHA1withDSA";
    //}
    try {
      Signature signature = Signature.getInstance(algorithm, "SUN");
      signature.initSign((PrivateKey) priv);
      // sign data file
      FileInputStream fis = new FileInputStream(dataPath);
      BufferedInputStream bufin = new BufferedInputStream(fis);
      byte[] buffer = new byte[1024];
      int len;
      while ((len = bufin.read(buffer)) >= 0) {
        signature.update(buffer, 0, len);
      };
      bufin.close();

      byte[] realSig = signature.sign();
      FileOutputStream sigfos = new FileOutputStream(filePath);
      System.out.println(filePath);
      sigfos.write(realSig);
      sigfos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean verifyDigitalSignature(String keyName, String fileName, String dataPath){
    try {
      PublicKey pub= App.akms.getPublicKey(keyName);
      Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
      sig.initVerify(pub);
      FileInputStream datafis = new FileInputStream(dataPath);
      BufferedInputStream bufin = new BufferedInputStream(datafis);

      byte[] buffer = new byte[1024];
      int len;
      while (bufin.available() != 0) {
        len = bufin.read(buffer);
        sig.update(buffer, 0, len);
      };
      bufin.close();

      // read signature file
      FileInputStream sigfis = new FileInputStream(fileName);
      byte[] sigToVerify = new byte[sigfis.available()];
      sigfis.read(sigToVerify);
      sigfis.close();

      return sig.verify(sigToVerify);

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    }
    return false;
  }

  private String getOutputFilePath(String inputFilePath, String concatedStr) {
    return inputFilePath + "." + concatedStr;
  }

  private String getCipherAlgorithm(String method) {
    String algorithm = method + "/" + PADDING_MODE;
    return algorithm;
  }

  private byte[] generateSalt() {
    Random r = new SecureRandom();
    byte[] salt = new byte[SALT_LENGTH];
    r.nextBytes(salt);
    return salt;
  }

  private int getIVLength(String method) {
    if (method.equals("DES")) {
      return 8;
    } else if (method.equals("AES")) {
      return 16;
    } else if (method.equals("DESede")) {
      return 8;
    } else {
      return 0;
    }
  }

  /*
    Generate a symmetric key from passphrase with a random salt
   */
  private SecretKey generateKeyFromPassphrase(String passphrase, String method, byte[] salt) {
    String algorithm = null;
    PBEKeySpec pbeKeySpec = null;

    if (method.equals("DES")) {
      algorithm = "PBEWithMD5andDES";
      pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT);
      // 65536:key derivation iteration count, 256: the key size
    } else if (method.equals("AES")) {
      algorithm = "PBKDF2WithHmacSHA1";
      pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, 128);
    } else if (method.equals("DESede")) {
      algorithm = "PBKDF2WithHmacSHA256";
      pbeKeySpec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, 192);
    }

    SecretKey key = null;
    try {
      SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);
      SecretKey tmp = kf.generateSecret(pbeKeySpec);
      key = new SecretKeySpec(tmp.getEncoded(), method);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return key;
  }
}
