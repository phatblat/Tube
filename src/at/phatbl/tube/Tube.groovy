package at.phatbl.tube

/**
 * Entry point for tube-style pipeline bulid.
 */
class Tube {
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
        script.node {
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
