package groovycalamari.exchangeratesbot

import io.micronaut.bots.core.FileCommandHandler
import io.micronaut.inject.qualifiers.Qualifiers

class AboutCommandSpec extends ApplicationContextSpecification {

    void "about command defined"() {
        expect:
        applicationContext.containsBean(FileCommandHandler, Qualifiers.byName("about"))
    }
}
