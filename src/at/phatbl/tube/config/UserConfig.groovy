package at.phatbl.tube.config

/**
 * Configuration parameters passed into the tube.
 */
class UserConfig implements Config {
    String label

    /**
     * Custom Map constructor which merges the given map with defaultMap before assigning to fields.
     * @param configMap
     */
    UserConfig(Map configMap) {
        if (!configMap) {
            configMap = [:]
        }
        Map newConfig = defaultMap.clone() as Map
        // Merge maps (+ not working)
        configMap.each { key, value ->
            newConfig."$key" = value
        }
        // Set fields from map
        newConfig.each { key, value ->
            this."$key" = value
        }
    }
}
