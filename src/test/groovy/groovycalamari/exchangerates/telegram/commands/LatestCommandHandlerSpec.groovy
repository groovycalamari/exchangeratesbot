package groovycalamari.exchangerates.telegram.commands


import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class LatestCommandHandlerSpec extends Specification {
    @Shared
    @Subject
    LatestCommandHandler handler = new LatestCommandHandler()

    void "regex matches USD EUR"() {
        given:
        String text = "USD EUR"

        expect:
        handler.matches(text)
        handler.parseUri(text).get() == "https://api.exchangeratesapi.io/latest?base=USD&symbols=EUR"

        !handler.matches("2020-02-19 USD EUR")
    }
}
