package enc;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

public class KeyStore {
  private File file;
  private FileInputStream fis;
  private FileOutputStream fos;
  private KeyRing keyRing;

  public KeyStore(File file, String passphrase, String length) {
    try {
      this.file = file;
      this.file.createNewFile();

      this.fis = new FileInputStream(this.file);
      this.fos = new FileOutputStream(this.file);

      fos.write(length.getBytes());
      fos.write(passphrase.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    keyRing = new KeyRing();
  }

  public void close() {

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