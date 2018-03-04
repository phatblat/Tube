package at.phatbl.tube

import at.phatbl.tube.config.Config
import at.phatbl.tube.config.UserConfig

/**
 * Entry point for tube-style pipeline build.
 */
class Tube implements Serializable {
    /** Script object passed from Tubefile */
    def script

    /** Map of project configuration. */
    Map configMap

    /** Computed property for convenient access to environment map. */
    Map getEnv() {
        return script.env.getEnvironment()
    }

    /**
     * Runs the pipeline.
     */
    void run() {
        Config config = new UserConfig(configMap)
        runPipeline(buildPipeline(config))
    }

    /**
     * Creates a Jenkinsfile script closure with the standard tube stages.
     * @param config Map of project ata passed to tube.
     * @return Closure of all the pipeline build steps.
     */
    Closure buildPipeline(Config config) {
        return {
            // Wire up groovy delegate to script so that same Jenkinsfile syntax can be used
            resolveStrategy = Closure.DELEGATE_FIRST
            delegate = script

            /*
             - Only keep the 100 most recent builds.
             */
            properties([
                buildDiscarder(logRotator(numToKeepStr: '100')),
/*
                pipelineTriggers([
                    // This works to add the "GitHub hook trigger for GITScm polling" trigger
                    // in multibranch and GH org jobs. However, it **REMOVES** all triggers
                    // when used on the older standalone PRB jobs.
                    githubPush(),
                    // cron('H 1 * * *'),
                ]),
*/
            ])

            // Abort if build takes over 1 hour
            timeout(time: 1, unit: 'HOURS') {
                node {
                    stage('🛒 Checkout') {
                        ansiColor('xterm') {
                            echo "script: $script"
                            echo "script.env: $script.env"
                            echo "env.BRANCH_NAME: $env.BRANCH_NAME"
                            echo "script.params: $script.params"

                            // FIXME: cleanWs step broken?
                            // https://jenkins.io/doc/pipeline/steps/ws-cleanup/#cleanws-delete-workspace-when-build-is-done
                            // groovy.lang.MissingPropertyException: No such property: cleanWs for class: tube
                            // Workspace Cleanup Plugin loaded http://wiki.jenkins-ci.org/display/JENKINS/Workspace+Cleanup+Plugin
                            step([$class: 'WsCleanup'])
                            checkout scm
                            sh "echo workspace after checkout: && ls -ah"
                            gradle "clean"
                            sh "echo workspace after gradle clean: && ls -ah"
                        }
                    }
                    stage('🏗 Assemble') {
                        gradle "assemble"
                    }
                    stage('✅ Test') {
                        gradle "test"
                    }
                    stage('🔎 Code Quality') {
                        gradle "codeQuality"
                    }
                    if (!isReleaseBuild()) {
                        return
                    }
                    stage('🔖 Release') {
                        gradle "release"
                    }
                    stage('🚀 Deploy') {
                        gradle "deploy"
                    }
                }
            }
        }
    }

    /**
     * Invokes the given closure within a try/catch block.
     * @param pipeline Closure containing pipeline build steps.
     */
    void runPipeline(Closure pipeline) {
        try {
            pipeline()
        } catch (Exception rethrow) {
            String failureDetail = failureDetail(rethrow)
            script.echo("""\
                FAILURE: '${script.env.JOB_NAME} (${script.env.BUILD_NUMBER})
                
                $failureDetail""").stripIndent()
            throw rethrow
        }
    }

    /**
     * Determines whether this build should continue to release.
     * @return
     */
    Boolean isReleaseBuild() {
        if (isPullRequestBuild()) {
            return false
        }
        return false
    }

    /**
     * Determines whether this is a pull request build.
     * @return
     */
    Boolean isPullRequestBuild() {
        if (script.params.sha1 != null) {
            // Standalone PRB populates the 'sha1' parameter with the commit hash to build.
            // Parameter is coerced into a Boolean using Groovy "truthy" logic.
            return script.params.sha1?.trim()
        }
        else if (env.BRANCH_NAME.contains("PR-")) {
            // Multibranch jobs populate BRANCH_NAME with "PR-123"
            return true
        }
        return false
    }

    /**
     * Read the detail from the exception to be used in the failure message
     * https://issues.jenkins-ci.org/browse/JENKINS-28119 will give better options.
     */
    static String failureDetail(Exception exception) {
        /* not allowed to access StringWriter
        def w = new StringWriter()
        exception.printStackTrace(new PrintWriter(w))
        return w.toString();
        */
        return exception.toString()
    }
}
