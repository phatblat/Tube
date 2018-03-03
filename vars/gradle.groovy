/*
 * gradle.groovy
 * Tube
 *
 * https://jenkins.io/blog/2017/06/27/speaker-blog-SAS-jenkins-world/
 */

/**
 * Invokes the Gradle wrapper at the root of the repo with standard options, appending any given args
 *
 * @param args Space-separated string of additional arguments to pass to Gradle.
 */
void call(args) {
    sh "./gradlew --info --no-daemon --stacktrace $args"
}
