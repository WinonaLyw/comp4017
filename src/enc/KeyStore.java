package enc;

import java.util.ArrayList;

public class KeyStore {
  private ArrayList<KeyRing> keyRings;
  // set with key, name, description
  public void addKeyRing(){
    keyRings.add(new KeyRing());
  }

  public void generateKeyPairs(){

  }

  public KeyRing exportPublicKey(){
    return new KeyRing();
  }

  public void importPublicKey(KeyRing piblicKeyRing){

  }
}