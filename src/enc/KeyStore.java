package enc;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.*;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class KeyStore {
  private File file;
  private String tempDecryptedPath;

  private String passphrase;
  private KeyRing keyRing;

  private static final String KEYSTORE_ENCRYPTION_METHOD = "DES";

  public KeyStore(File file) {
    this.file = file;
    this.keyRing = new KeyRing();
  }

  public void create(String passphrase) {
    this.passphrase = passphrase;
    try {
      this.file.createNewFile();
      this.writeLine(passphrase, true);
      this.save(true);
      this.open(passphrase, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public byte[] open(String passphrase, boolean isKeyStoreFile) {
    // 1. Decrypt keystore file
    this.tempDecryptedPath = App.fes.decryptFile(this.file.getAbsolutePath(), passphrase);

    // 2. Read passphrase (first line)
    String correctPassphrase = this.readLine(tempDecryptedPath);
    if (!passphrase.equals(correctPassphrase)) {
      return null;
    }

    if (!isKeyStoreFile) {
      // 3. Read into KeyRing
      this.readIntoKeyRing();
    }

    // TODO: return to textfield
//    this.writeLine("haha");
    return null;
  }

  private void readIntoKeyRing() {
    try {
      BufferedReader br = new BufferedReader(new FileReader(this.tempDecryptedPath));
      br.readLine(); // skip passphrase
      String currentLine = null;
      while ((currentLine = br.readLine()) != null) {
        if (currentLine.substring(0, 4).equals("Pair")) {
          boolean isPair = currentLine.substring(6, currentLine.length()).equals("true");

          currentLine = br.readLine();
          String name = currentLine.substring(6, currentLine.length());
          currentLine = br.readLine();
          String description = currentLine.substring(6, currentLine.length());

          currentLine = br.readLine();
          String publKey = currentLine.substring(6, currentLine.length());
          byte[] publKeyBytes = Base64.getDecoder().decode(publKey);
          X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publKeyBytes);
          KeyFactory publKeyFactory = KeyFactory.getInstance("RSA");
          PublicKey publicKey = publKeyFactory.generatePublic(x509EncodedKeySpec);

          if (isPair) {
            currentLine = br.readLine();
            String privKey = currentLine.substring(6, currentLine.length());
            byte[] privKeyBytes = Base64.getDecoder().decode(privKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyBytes);
            KeyFactory privKeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = privKeyFactory.generatePrivate(keySpec);

            KeyPair pair = new KeyPair(publicKey, privateKey);
            enc.KeyPair keyPair = new enc.KeyPair(pair, name, description);
            this.keyRing.addKeyPair(keyPair);
          } else {
            enc.PublicKey pbKey = new enc.PublicKey(publicKey, name, description);
            this.keyRing.addPublicKey(pbKey);
          }
        }
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void save(boolean isKeyStoreFile) {
    if (isKeyStoreFile) {
      App.fes.encryptFile(this.file.getAbsolutePath(), this.passphrase, KEYSTORE_ENCRYPTION_METHOD, true);
    } else {
      App.fes.encryptFile(this.tempDecryptedPath, this.passphrase, KEYSTORE_ENCRYPTION_METHOD, false);
    }
  }

  public void close() {
    this.save(false);
  }

  private void writeLine(String line, boolean isKeyStoreFile) {
    try {
      String filePath = null;
      if (isKeyStoreFile) {
        filePath = this.file.getAbsolutePath();
      } else {
        filePath = this.tempDecryptedPath;
      }
      BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
      if (line != null) {
        bw.write(line);
      }
      bw.newLine();
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String readLine(String filePath) {
    String line = null;
    try {
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      line = br.readLine();
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return line;
  }


  public void generateKeyPairs(String name, String desc){
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(1024);
      KeyPair pair = keyGen.generateKeyPair();

      this.keyRing.addKeyPair(pair,name,desc);

      PrivateKey privateKey = pair.getPrivate();
      PublicKey publicKey = pair.getPublic();

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec x509EncodedKeySpec = keyFactory.getKeySpec(publicKey, X509EncodedKeySpec.class);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = keyFactory.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
      byte[] privKeyBytes = pkcs8EncodedKeySpec.getEncoded();

      this.writeLine("Pair: " + true, false);
      this.writeLine("Name: " + name, false);
      this.writeLine("Desc: " + desc, false);

      this.writeLine(("Publ: " + Base64.getEncoder().encodeToString(x509EncodedKeySpec.getEncoded())), false);
      this.writeLine("Priv: " + Base64.getEncoder().encodeToString(privKeyBytes), false);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<enc.KeyPair> getKeyPairs(){
    if (keyRing != null){
      return keyRing.getKeyPairs();
    }
    return null;
  }

  public Key getPrivateKey(String name){
    if (keyRing != null){
      return keyRing.getPrivateKey(name);
    }
    return null;
  }

  public Key getPublicKey(String name){
    if (keyRing != null)
      return keyRing.getPublicKey(name);
    return null;
  }
}
