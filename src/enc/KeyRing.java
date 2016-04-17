package enc;

import java.security.Key;
import java.util.ArrayList;

public class KeyRing{
  private ArrayList<KeyPair> keyPairs;

  public KeyRing() {
    keyPairs = new ArrayList<>();
  }

  public void addKeyPair(java.security.KeyPair keyPair, String name, String description) {
    KeyPair kp = new KeyPair(keyPair, name, description);
    this.keyPairs.add(kp);
    System.out.println(name);
  }



  // when import public key
//  public KeyRing(String key){
//    this.key = key;
//    this.name = null;
//    this.description = null;
//  }

  // when create private and public keys
//  public KeyRing(String key, String name, String description){
//    this.key = key;
//    this.name = name;
//    this.description = description;
//  }
//
//
//  public void setName(String name){
//    this.name = name;
//  }
//
//  public void setDescription(String description){
//    this.description = description;
//  }
//
//  public String getKey(){
//    return this.key;
//  }
//  public String getName(){
//    return this.name;
//  }
//
//  public String getDescription() {
//    return this.description;
//  }
}