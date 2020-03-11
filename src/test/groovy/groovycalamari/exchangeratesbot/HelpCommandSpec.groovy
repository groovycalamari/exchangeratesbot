package groovycalamari.exchangeratesbot

import io.micronaut.bots.core.FileCommandHandler
import io.micronaut.inject.qualifiers.Qualifiers

class HelpCommandSpec extends ApplicationContextSpecification {

    void "help command defined"() {
        expect:
        applicationContext.containsBean(FileCommandHandler, Qualifiers.byName("help"))
    }
}
