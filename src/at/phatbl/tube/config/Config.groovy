package at.phatbl.tube.config

/**
 * Interface defining all the possible configuration parameters for a tube.
 */
interface Config {
    /** Jenkins agent label to run the tube. */
    String label

    /** Default values to be used when project doesn't specify value for a key. */
    static Map defaults = [
        label: "mini"
    ]
}
