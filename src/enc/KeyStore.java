package enc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

public class KeyStore {
  private ArrayList<KeyRing> keyRings = new ArrayList<>();

  // set with key, name, description
  public void addKeyRing(String key){
    keyRings.add(new KeyRing(key));
  }

  public void loadFromFile(String fileName) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line = br.readLine();
      while (line != null) {
        String[] contents = line.split(" ");
        KeyRing keyRing = new KeyRing(contents[0],contents[1],contents[2]);
        keyRings.add(keyRing);
        line = br.readLine();
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeIntoFile(String fileName) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
      for(KeyRing keyRing : keyRings){
        bw.write(keyRing.getKey() + " " + keyRing.getName() + " " + keyRing.getDescription());
        bw.newLine();
      }
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  public void generateKeyPairs(){
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(1024);
      KeyPair pair = keyGen.generateKeyPair();
      PrivateKey priv = pair.getPrivate();
      PublicKey pub = pair.getPublic();
      this.addKeyRing(Base64.getEncoder().encodeToString(priv.getEncoded()));
      this.addKeyRing(Base64.getEncoder().encodeToString(pub.getEncoded()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  //public Key exportPublicKey(String name){
  //}

  public void importPublicKey(KeyRing piblicKeyRing){

  }
}