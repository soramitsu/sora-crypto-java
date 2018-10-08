package jp.co.soramitsu.sora.sdk.did.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jp.co.soramitsu.sora.sdk.did.model.Visitable;
import jp.co.soramitsu.sora.sdk.did.model.dto.authentication.AuthenticationVisitor;
import jp.co.soramitsu.sora.sdk.did.model.dto.authentication.Ed25519Sha3Authentication;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Ed25519Sha3Authentication.class, name = "Ed25519Sha3Authentication")
})
@EqualsAndHashCode
@ToString
public abstract class Authentication implements Visitable<AuthenticationVisitor> {

  public abstract DID getPublicKey();
}
