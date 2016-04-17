package enc;

import java.io.*;
import java.security.*;
import java.security.KeyPair;
import java.util.ArrayList;

public class KeyStore {
  private File file;
  private String tempDecryptedPath;

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
      while ((currentLine = br.readLine()) != null) {
        System.out.println();
      }
      br.close();
    } catch (IOException e) {
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

      byte[] privateKey = pair.getPrivate().getEncoded();
      byte[] publicKey = pair.getPublic().getEncoded();

      this.writeLine("Pair: " + true, false);
      this.writeLine("Name: " + name, false);
      this.writeLine("Desc: " + desc, false);
      this.writeLine("Publ: " + privateKey.toString(), false);
      this.writeLine("Priv: " + publicKey.toString(), false);
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
}