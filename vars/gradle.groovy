/*
 * gradle.groovy
 * pipeline
 *
 * https://jenkins.io/blog/2017/06/27/speaker-blog-SAS-jenkins-world/
 */

def call(args) {
    sh "./gradlew ${args}"
}
