package jp.co.soramitsu.sora.sdk.crypto.common;

public interface SaltGenerator {

  /**
   * Get next salt represented as string.
   *
   * @return salt
   */
  String next();
}
