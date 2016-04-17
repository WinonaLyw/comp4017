package enc;

/**
 * Created by xiaoyufan on 18/4/2016.
 */
public class PublicKey {
  private java.security.PublicKey publicKey;
  private String name;
  private String description;

  public PublicKey(java.security.PublicKey publicKey, String name, String description) {
    this.publicKey = publicKey;
    this.name = name;
    this.description = description;
  }
}
