/*
 * Jenkinsfile
 * Tube
 */

@Library('Tube') _

simpleBuild {

    machine = "node1"

    env = [
        FOO : 42,
        BAR : "YASS"
    ]

    before_script = "echo before"
    script = 'echo after $FOO'
    after_script = 'echo done now'

}
