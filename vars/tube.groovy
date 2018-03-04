/*
 * tube.groovy
 * Tube
 *
 * Shared library function which runs a new tube build.
 * See https://jenkins.io/blog/2017/06/27/speaker-blog-SAS-jenkins-world/
 */

import at.phatbl.tube.Tube

/**
 * Runs a tube-style pipeline build using the default options.
 * The call(body) method in any file in /vars is exposed as a method with the same name as the file.
 *
 * @param body Closure
 */
void call(body) {
    // Wiring up config to be populated with key-value pairs from user-supplied body.
    Map configMap = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = configMap
    body()

    Tube tube = new Tube(script: this, configMap: configMap)
    tube.run()
}
