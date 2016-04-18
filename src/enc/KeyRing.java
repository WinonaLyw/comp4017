package enc;

import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;

public class KeyRing{
  private ArrayList<enc.KeyPair> keyPairs;
  private ArrayList<enc.PublicKey> publicKeys;

  public KeyRing() {
    keyPairs = new ArrayList<>();
  }

  public void addKeyPair(java.security.KeyPair keyPair, String name, String description) {
    enc.KeyPair kp = new enc.KeyPair(keyPair, name, description);
    this.keyPairs.add(kp);
  }

  public void addKeyPair(KeyPair keyPair) {
    this.keyPairs.add(keyPair);
  }

  public ArrayList<KeyPair> getKeyPairs(){
    return this.keyPairs;
  }

  public Key getPrivateKey(String name){
    for (KeyPair kp: this.keyPairs){
      if (kp.getName().equals(name)){
        return kp.getPrivateKey();
      }
    }
    return null;
  }

  public void addPublicKey(PublicKey publicKey, String name, String description) {
    enc.PublicKey pk = new enc.PublicKey(publicKey, name, description);
    this.publicKeys.add(pk);
  }

  public void addPublicKey(enc.PublicKey publicKey) {
    this.publicKeys.add(publicKey);
  }

  public Key getPublicKey(String name){
    for(KeyPair kp: this.keyPairs) {
      if(name.equals(kp.getName())){
        return kp.getPublicKey();
      }
    }
    return null;
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

//  public String getDescription() {
//    return this.description;
//  }
}