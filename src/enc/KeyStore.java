package enc;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.security.*;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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
    this.tempDecryptedPath = App.fes.decryptFile(this.file.getAbsolutePath(), passphrase, KEYSTORE_ENCRYPTION_METHOD);

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
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while ((currentLine = br.readLine()) != null) {
        if (currentLine.substring(0, 4).equals("Pair")) {
          boolean isPair = currentLine.substring(6, currentLine.length()).equals("true");

          currentLine = br.readLine();
          String name = currentLine.substring(6, currentLine.length());
          currentLine = br.readLine();
          String description = currentLine.substring(6, currentLine.length());
          System.out.println(name);
          System.out.println(description);

//          String publicKey = currentLine.substring(0, currentLine.length());
//          byte[] publicKeyBytes = (new BASE64Decoder()).decodeBuffer(publicKey);
//          System.out.println(publicKey);
//          System.out.println(publicKeyBytes);
//          X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey.getBytes());
//          KeyFactory pubKeyFactory = KeyFactory.getInstance("RSA");
//          java.security.PublicKey pubKey = pubKeyFactory.generatePublic(pubKeySpec);
//
          if (isPair) {
//            currentLine = br.readLine();
//            String privateKey = currentLine.substring(6, currentLine.length());
//            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKey.getBytes());
//            KeyFactory privKeyFactory = KeyFactory.getInstance("RSA");
//            java.security.PrivateKey privKey = privKeyFactory.generatePrivate(privKeySpec);

//            KeyPair pair = new KeyPair(pubKey, privKey);
//            enc.KeyPair keyPair = new enc.KeyPair(pair, name, description);
          }
        } else {
          baos.write(currentLine.getBytes());
        }

      }
      byte[] bytes = baos.toByteArray();

      //byte[] publicKeyBytes = (new BASE64Decoder()).decodeBuffer(baos.toByteArray());
      X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(bytes);
      KeyFactory pubKeyFactory = KeyFactory.getInstance("RSA");
      java.security.PublicKey pubKey = pubKeyFactory.generatePublic(pubKeySpec);

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

      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
              publicKey.getEncoded());
      byte[] pubKeyBytes = x509EncodedKeySpec.getEncoded();

      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
              privateKey.getEncoded());
      byte[] privKeyBytes = pkcs8EncodedKeySpec.getEncoded();

      this.writeLine("Pair: " + true, false);
      this.writeLine("Name: " + name, false);
      this.writeLine("Desc: " + desc, false);

      BufferedWriter bw = new BufferedWriter(new FileWriter(this.tempDecryptedPath, true));
      BASE64Encoder b64 = new BASE64Encoder();
      bw.write(b64.encode(publicKey.getEncoded()));
      bw.close();

//      this.writeLine(pubKeyBytes.toString(), false);
//      this.writeLine(privKeyBytes.toString(), false);

//      this.writeLine("Publ: " + privateKey.toString(), false);
//      this.writeLine("Priv: " + publicKey.toString(), false);

//      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//      X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
//              pubKeyBytes);
//      PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
//      System.out.println(pubKey.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

//  public void loadFromFile(String fileName) {
//    try {
//      BufferedReader br = new BufferedReader(new FileReader(fileName));
//      String line = br.readLine();
//      while (line != null) {
//        String[] contents = line.split(" ");
//        KeyRing keyRing = new KeyRing(contents[0],contents[1],contents[2]);
//        keyRings.add(keyRing);
//        line = br.readLine();
//      }
//      br.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

//  public void writeIntoFile(String fileName) {
//    try {
//      BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
//      for(KeyRing keyRing : keyRings){
//        bw.write(keyRing.getKey() + " " + keyRing.getName() + " " + keyRing.getDescription());
//        bw.newLine();
//      }
//      bw.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//  }

  //public Key exportPublicKey(String name){
  //}

  public void importPublicKey(KeyRing piblicKeyRing){

  }
}
