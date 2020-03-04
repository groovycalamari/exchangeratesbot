package groovycalamari.exchangerates.telegram.commands

import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Subject

@Ignore
class ExchangeRatesUpdateDispatcherSpec extends ApplicationContextSpecification {

    @Shared
    @Subject
    ExchangeRatesUpdateDispatcher exchangeRatesUpdateDispatcher = applicationContext.getBean(ExchangeRatesUpdateDispatcher)

    void "parse command"() {
        expect:
        exchangeRatesUpdateDispatcher.parseCommand("/help").isPresent()
        exchangeRatesUpdateDispatcher.parseCommand("/help").get() == 'help'
    }
}
