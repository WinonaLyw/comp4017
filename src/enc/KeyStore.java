package enc;

import java.io.*;
import java.security.*;
import java.security.KeyPair;
import java.util.Random;

public class KeyStore {
  private File file;

  private String passphrase;
  private byte[] decryptedContent;
  private KeyRing keyRing;

  private static final String KEYSTORE_ENCRYPTION_METHOD = "DES";

  public KeyStore(File file) {
    this.file = file;
  }

 public void create(String passphrase, String length) {
  //  this.passphrase = passphrase;
  //  this.keyRing = new KeyRing();
  //
  //  try {
  //    this.file.createNewFile();
  //
  //    ByteArrayOutputStream baos = new ByteArrayOutputStream();
  //    baos.write(length.getBytes());
  //    baos.write(passphrase.getBytes());
  //    this.decryptedContent = baos.toByteArray();
  //    baos.close();
  //
  //    // TODO
  //    this.store();
  //  } catch (IOException e) {
  //    e.printStackTrace();
  //  }
  }
  //
  // Store decryptedContent to keystore file
  private void store() {
  //  // 1. Encrypt decryptedContent
  //  byte[] encryptedContent = App.fes.encryptBytes(this.decryptedContent, this.passphrase,
  //          KEYSTORE_ENCRYPTION_METHOD);
  //
  //  // 2. Write encrypted content
  //  try {
  //    FileOutputStream fos = new FileOutputStream(this.file);
  //    fos.write(encryptedContent);
  //    fos.close();
  //  } catch (IOException e) {
  //    e.printStackTrace();
  //  }
  }

  public byte[] open(String passphrase) {
  //  // 1. Read keystore file into byte[] buffer
  //  try {
  //    FileInputStream fis = new FileInputStream(this.file);
  //  } catch (FileNotFoundException e) {
  //    e.printStackTrace();
  //  }
  //
  //  // 2. Decrypt buffer
  //
  //  // 3. Read passphrase and check
  //
  //  // 3. TODO: Return decrypted content without passphrase?
  //  byte[] decryptedContent = App.fes.decryptBytes(null);
  //  this.decryptedContent = decryptedContent;
    return decryptedContent;
  }

  public void close() {
    this.store();
  }

  public byte[] getDecryptedContent() {
    return this.decryptedContent;
  }

  public void generateKeyPairs(){
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(1024);
      KeyPair pair = keyGen.generateKeyPair();
      PrivateKey priv = pair.getPrivate();
      PublicKey pub = pair.getPublic();
//      this.addKeyRing(Base64.getEncoder().encodeToString(priv.getEncoded()));
//      this.addKeyRing(Base64.getEncoder().encodeToString(pub.getEncoded()));
    } catch (NoSuchAlgorithmException e) {
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