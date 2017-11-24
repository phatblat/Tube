/*
 * Jenkinsfile
 * pipeline
 */

@Library('pipeline') _

node {
    scm checkout
    sh "ls -l"
    gradle "projects"
}
