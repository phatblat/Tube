/*
 * Jenkinsfile
 * Tube
 */

@Library('Tube') _

// Register webhooks
// https://issues.jenkins-ci.org/browse/JENKINS-35132#commentauthor_294758_verbose
properties([pipelineTriggers([githubPush()])])

simpleBuild {

    machine = "node1"

    env = [
        FOO : 42,
        BAR : "YASS"
    ]

    git_repo = "https://github.com/cloudbeers/PR-demo"

    before_script = "echo before"
    script = 'echo after $FOO'
    after_script = 'echo done now'

    notifications = [
        email : "ben@octop.ad"
    ]

}
