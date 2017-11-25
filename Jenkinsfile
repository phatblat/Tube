/*
 * Jenkinsfile
 * Tube
 */

@Library('Tube') _

node {
    stage("Checkout") {
        checkout scm
        sh "ls -l"
        gradle "projects"
    }
}
