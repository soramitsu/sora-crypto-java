package jp.co.soramitsu.sora.sdk.crypto.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import jp.co.soramitsu.sora.sdk.crypto.json.flattener.Flattener
import spock.genesis.Gen
import spock.lang.Specification

class FlattenerTest extends Specification {
    static class FlattenerImpl extends Flattener {
        /* to test protected methods */
    }

    def mapper = new ObjectMapper()
    def flattener = new FlattenerImpl()

    def initial = mapper.readTree(this.getClass().getResourceAsStream('/json/_initial.json'))
    def flattened = mapper.readTree(this.getClass().getResourceAsStream('/json/flattened.json'))

    def "isFlattened works"() {
        given:
        initial
        flattened

        when:
        def f1 = flattener.isFlattened(initial)
        def f2 = flattener.isFlattened(flattened)

        then:
        !f1
        f2
    }

    def "flatten works"() {
        given:
        initial
        flattened

        when:
        def flatActual = flattener.flatten(initial)

        then:
        flatActual == flattened
    }

    ObjectNode deepgen(int level, it) {
        if (level <= 0) {
            return null
        }

        ObjectNode last = mapper.createObjectNode() as ObjectNode
        last.set(it.next(), deepgen(level - 1, it))
        return last
    }

    def "deep jsons are handled properly"() {
        given:
        def deep = deepgen(levels, it)

        when:
        def flat = flattener.flatten(deep)

        then:
        flattener.isFlattened(flat)
        flat.size() == 1

        where:
        // maximum nesting levels, iterator
        levels | it
        100    | Gen.string(100).iterator()  // autogenerated key
        100    | Gen.string(~/same/).iterator()         // same key

    }

}
