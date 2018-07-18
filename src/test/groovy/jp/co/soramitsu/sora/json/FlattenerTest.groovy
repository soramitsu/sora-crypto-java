package jp.co.soramitsu.sora.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import spock.genesis.Gen
import spock.genesis.generators.InfiniteIterator
import spock.genesis.transform.Iterations
import spock.lang.Specification

class FlattenerTest extends Specification {
    static class FlattenerImpl extends Flattener {
        /* to test protected methods */
    }

    def mapper = new ObjectMapper()
    def flattener = new FlattenerImpl()

    def initial = mapper.readTree(this.getClass().getResourceAsStream('/json/_initial.json'))
    def flattened = mapper.readTree(this.getClass().getResourceAsStream('/json/flattened.json'))

    @Iterations(500)
    def "sanitize and desanitize are opposite"() {
        given:
        expected

        when:
        def sanitized = flattener.sanitize(expected)
        def actual = flattener.desanitize(sanitized)

        then:
        actual == expected
        noExceptionThrown()

        where:
        expected << Gen.string(~/[a-z\\\/_0-9]+/)
    }

    def "sanitize handles UTF-8"() {
        given:
        byte[] b = str.getBytes("utf-8") as byte[]
        def bytes = new String(b, "utf-8") as String

        when:
        def sanitized = flattener.sanitize(bytes)
        def actual = flattener.desanitize(sanitized)

        then:
        actual == str
        noExceptionThrown()

        where:
        str = "привет, мир"

    }

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

    ObjectNode deepgen(int level, InfiniteIterator<String> it) {
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
        levels = 1000    // maximum nesting levels
        maxLength = 100  // maximum key length
        it = Gen.string(maxLength).iterator()
    }

}
