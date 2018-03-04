package at.phatbl.tube.config

import spock.lang.Specification

class ConfigSpec extends Specification {
    void "can be merged with defaults"() {
        setup:
        Config config = new UserConfig()

        expect:
        config.label == "mini"
    }

    void "can override defaults"() {
        setup:
        Config config = new UserConfig(label: "secret_agent")

        expect:
        config.label == "secret_agent"
    }
}