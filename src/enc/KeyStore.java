package enc;

import java.io.*;
import java.security.*;
import java.security.KeyPair;

public class KeyStore {
  private File file;
  private String decryptedPath;

  private String passphrase;
  private KeyRing keyRing;

  private static final String KEYSTORE_ENCRYPTION_METHOD = "DES";

  public KeyStore(File file) {
    this.file = file;
  }

  public void create(String passphrase) {
    this.passphrase = passphrase;
    this.keyRing = new KeyRing();

    try {
      this.file.createNewFile();
      this.writeLine(passphrase);
      this.save();
      this.open(passphrase);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public byte[] open(String passphrase) {
    // 1. Decrypt keystore file
    this.decryptedPath = App.fes.decryptFile(this.file.getAbsolutePath(), passphrase, KEYSTORE_ENCRYPTION_METHOD, true);

    // 2. Read passphrase (first line)
    String correctPassphrase = this.readLine(decryptedPath);
    if (!passphrase.equals(correctPassphrase)) {
      return null;
    }

    this.writeLine("haha");
    return null;
  }

  private void save() {
    App.fes.encryptFile(this.file.getAbsolutePath(), this.passphrase, KEYSTORE_ENCRYPTION_METHOD, true);
  }

  public void close() {
    this.save();
  }

  private void writeLine(String line) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(this.decryptedPath, true));
      bw.write(line);
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