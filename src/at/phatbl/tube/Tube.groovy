package at.phatbl.tube

/**
 * Entry point for tube-style pipeline build.
 */
class Tube implements Serializable {
    def script
    Map config

    Tube(def script, Map config) {
        this.script = script
        this.config = config
    }

    void run() {
        runPipeline(config)
    }

    /**
     * Runs the standard tube stages.
     * @param config
     */
    void runPipeline(Map config) {
        Closure script = {
            // Wire up groovy delegate to script so that same Jenkinsfile syntax can be used
            delegate = script
            resolveStrategy = Closure.DELEGATE_FIRST

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
        script()
    }
}
