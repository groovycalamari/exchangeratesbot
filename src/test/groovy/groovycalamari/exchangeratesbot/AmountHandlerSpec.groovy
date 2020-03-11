package groovycalamari.exchangeratesbot

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class AmountHandlerSpec extends Specification {
    @Subject
    @Shared
    AmountHandler handler = new AmountHandler(null, null, null, null)

    void "matches 2020-01-02 1000"() {
        expect:
        handler.matches('50.50')
    }
}
