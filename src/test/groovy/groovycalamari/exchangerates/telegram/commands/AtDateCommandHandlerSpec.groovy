package groovycalamari.exchangerates.telegram.commands


import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class AtDateCommandHandlerSpec extends Specification {
    @Shared
    @Subject
    AtDateCommandHandler handler = new AtDateCommandHandler()

    void "regex matches 2020-02-19 USD EUR"() {
        given:
        String text = "2020-02-19 USD EUR"

        expect:
        handler.matches(text)
        handler.parseUri(text).get() == "https://api.exchangeratesapi.io/2020-02-19?base=USD&symbols=EUR"
    }
}
