package at.phatbl.tube

import spock.lang.Specification

class TubeSpec extends Specification {
    void "failure detail extracts exception message"() {
        setup:
        Exception exception = new Exception("this is the message")

        when:
        String message = Tube.failureDetail(exception)

        then:
        message == "java.lang.Exception: " + "this is the message"
    }

    void "runs a closure"() {
        setup:
        Boolean closureCalled = false
        Closure closure = { closureCalled = true }
        Tube tube = new Tube()

        when:
        tube.runPipeline(closure)

        then:
        closureCalled
    }
}
