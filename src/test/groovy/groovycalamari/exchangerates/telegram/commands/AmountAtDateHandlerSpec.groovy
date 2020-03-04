package groovycalamari.exchangerates.telegram.commands

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class AmountAtDateHandlerSpec extends Specification {

    @Subject
    @Shared
    AmountAtDateHandler handler = new AmountAtDateHandler(null, null, null)

    void "matches 2020-01-02 1000"() {
        expect:
        handler.matches('2020-01-02 1000')

        and:
        !handler.matches('2020-01-02 foo')
    }

    void "parseAmount 2020-01-02 4750"() {
        expect:
        handler.parseAmount("2020-01-02 4750").get() == 4750.0
    }

    void "parseDtsr 2020-01-02 4750"() {
        expect:
        handler.parseDateStr("2020-01-02 4750").get() == '2020-01-02'
    }
}
