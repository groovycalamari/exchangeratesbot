package groovycalamari.exchangerates.telegram.commands

import io.micronaut.bots.telegram.dispatcher.FileCommandHandler
import io.micronaut.inject.qualifiers.Qualifiers

class AboutCommandSpec extends ApplicationContextSpecification {

    void "about command defined"() {
        expect:
        applicationContext.containsBean(FileCommandHandler, Qualifiers.byName("about"))
    }
}
