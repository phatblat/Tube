/*
 * Jenkinsfile
 * pipeline
 */

@Library('pipeline') _

node {
    checkout scm
    sh "ls -l"
    gradle "projects"
}
