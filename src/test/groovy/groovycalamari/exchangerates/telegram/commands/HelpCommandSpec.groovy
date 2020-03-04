package groovycalamari.exchangerates.telegram.commands

import io.micronaut.bots.telegram.dispatcher.FileCommandHandler
import io.micronaut.inject.qualifiers.Qualifiers

class HelpCommandSpec extends ApplicationContextSpecification {

    void "help command defined"() {
        expect:
        applicationContext.containsBean(FileCommandHandler, Qualifiers.byName("help"))
    }
}
