pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-maven.yml'
        }
    }
    stages {

        stage("notify") {
            steps {
                //  publishEvent event:jsonEvent('{"eventName":"helloWorld"}'), verbose: true

                publishEvent event: jsonEvent('{"event":"com.example:supply-chain-webapp:1.2-SNAPSHOT:war","mavenArtifact":{"groupId":"com.example", "artifactId":"cloudbees-jar", "version":"0.5-SNAPSHOT"}} '), verbose: true

            }
        }
    }
}

