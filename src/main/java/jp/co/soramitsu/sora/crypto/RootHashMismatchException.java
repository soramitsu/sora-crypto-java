package jp.co.soramitsu.sora.crypto;

import javax.xml.bind.DatatypeConverter;
import lombok.Getter;

public class RootHashMismatchException extends Exception {

  @Getter
  private final Hash expectedRoot;

  @Getter
  private final Hash actualRoot;

  public RootHashMismatchException(Hash expectedRoot, Hash actualRoot) {
    super(String.format("Merkle tree is invalid. Expected root %s, actual %s",
        DatatypeConverter.printHexBinary(expectedRoot.getData()),
        DatatypeConverter.printHexBinary(actualRoot.getData())
    ));

    this.expectedRoot = expectedRoot;
    this.actualRoot = actualRoot;
  }
}
