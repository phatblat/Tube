/*
 * Jenkinsfile
 * Tube
 */

@Library('Tube') _

// Register webhooks
// https://issues.jenkins-ci.org/browse/JENKINS-35132#commentauthor_294758_verbose
properties([pipelineTriggers([githubPush()])])

node {
    stage("Checkout") {
        checkout scm
        sh "ls -l"
        gradle "projects"
    }
}
