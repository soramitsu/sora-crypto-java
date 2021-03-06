package jp.co.soramitsu.sora.sdk.crypto.json;

import com.fasterxml.jackson.databind.JsonNode;

public class NotJsonObjectException extends RuntimeException {

  public NotJsonObjectException(JsonNode root) {
    super("Not a JSON object :" + root.toString());
  }

}
