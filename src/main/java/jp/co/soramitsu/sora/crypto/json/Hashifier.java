package jp.co.soramitsu.sora.crypto.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import jp.co.soramitsu.sora.crypto.common.Hash;
import jp.co.soramitsu.sora.crypto.common.SecurityProvider;
import jp.co.soramitsu.sora.crypto.type.DigestTypeEnum;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.val;

@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class Hashifier {

  private MessageDigest digest;

  private ObjectMapper mapper;

  private JSONCanonizer canonizer;

  public Hashifier() {
    this(new SecurityProvider().getMessageDigest(DigestTypeEnum.SHA3_256));
  }

  public Hashifier(@NonNull MessageDigest digest) {
    this(digest, new ObjectMapper(), new JSONCanonizerWithOneCoding());
  }

  /**
   * Hashes top-level key-value pairs from input JSON. Number of output hashes equals to number of
   * top-level keys in input JSON.
   *
   * @param root JSON Object
   * @return list of hashes. Hash is specified by {@link #digest}
   * @throws NotJsonObjectException when input json is not an object
   */
  public List<Hash> hashify(ObjectNode root) throws IOException {
    List<Hash> hashes = new ArrayList<>(root.size());

    for (Iterator<Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
      val field = it.next();

      // create new object, one per key-value
      ObjectNode out = mapper.createObjectNode();
      out.set(field.getKey(), field.getValue());

      byte[] serialized = canonizer.canonize(out);
      byte[] hash = digest.digest(serialized);
      hashes.add(
          new Hash(hash)
      );
    }

    return hashes;
  }
}
