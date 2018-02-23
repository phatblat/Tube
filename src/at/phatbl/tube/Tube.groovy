package at.phatbl.tube

/**
 * Entry point for tube-style pipeline build.
 */
class Tube implements Serializable {
    def script
    Map config

    /**
     * Runs the pipeline.
     */
    void run() {
        runPipeline(buildPipeline(config))
    }

    /**
     * Creates a Jenkinsfile script closure with the standard tube stages.
     * @param config Map of project ata passed to tube.
     * @return Closure of all the pipeline build steps.
     */
    Closure buildPipeline(Map config) {
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
                        echo "🛒 Checkout stage"
                        step([$class: 'WsCleanup'])
                        checkout scm
                        sh "echo workspace after checkout: && ls -ah"
                    }
                    stage('🏗 Assemble') {
                        echo "🏗 Assemble stage"
                    }
                    stage('✅ Test') {
                        echo "✅ Test stage"
                    }
                    stage('🔎 Code Quality') {
                        echo "🔎 Code Quality stage"
                    }
                    stage('🔖 Release') {
                        echo "🔖 Release stage"
                    }
                    stage('🚀 Deploy') {
                        echo "🚀 Deploy stage"
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
            failureDetail = failureDetail(rethrow)
            println """\
                FAILURE: '${script.env.JOB_NAME} (${script.env.BUILD_NUMBER})
                
                $failureDetail""".stripIndent()
            throw rethrow
        }
    }

    /**
     * Read the detail from the exception to be used in the failure message
     * https://issues.jenkins-ci.org/browse/JENKINS-28119 will give better options.
     */
    static String failureDetail(exception) {
        /* not allowed to access StringWriter
        def w = new StringWriter()
        exception.printStackTrace(new PrintWriter(w))
        return w.toString();
        */
        return exception.toString()
    }
}
