package enc;

import java.security.Key;

/**
 * Created by xiaoyufan on 16/4/2016.
 */
public class KeyPair {
  private java.security.KeyPair keyPair;
  private String name;
  private String description;

  public KeyPair(java.security.KeyPair keyPair, String name, String description) {
    this.keyPair = keyPair;
    this.name = name;
    this.description = description;
  }

  public String getName(){
    return this.name;
  }

  public Key getPrivateKey(){
    return this.keyPair.getPrivate();
  }

  public Key getPublicKey(){
    return this.keyPair.getPublic();
  }
}
