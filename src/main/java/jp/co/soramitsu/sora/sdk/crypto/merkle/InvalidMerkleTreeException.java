package jp.co.soramitsu.sora.sdk.crypto.merkle;

public class InvalidMerkleTreeException extends RuntimeException {

  public InvalidMerkleTreeException(int pos) {
    super("No element at position: " + pos);
  }
}
