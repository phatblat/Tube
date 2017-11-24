/*
 * Jenkinsfile
 * pipeline
 */

@Library('pipeline') _

node {
    stage("Checkout") {
        checkout scm
        sh "ls -l"
        gradle "projects"
    }
}
