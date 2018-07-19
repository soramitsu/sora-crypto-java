package jp.co.soramitsu.sora.crypto;

import lombok.Value;

@Value
public class MerkleTree {

  // [root] [intermediate nodes] [leafs]
  byte[][] tree;

  /**
   * Getter for the tree. Tree is stored "by levels", so first level is root and stored
   * as tree[0], next level (2 items) is stored in tree[1] and tree[2], and so on.
   * @return tree represented as [root] [intermediate nodes] [leafs]
   */
  public byte[][] getTree() {
    return tree;
  }

  /**
   * @return merkle root hash
   */
  public byte[] root() {
    return tree[0];
  }
}

