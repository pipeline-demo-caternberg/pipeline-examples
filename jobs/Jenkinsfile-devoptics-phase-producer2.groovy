pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate.yml'
        }
    }
    stages {
        stage('Preparation') { // for display purposes
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/jglick/simple-maven-project-with-tests.git'
                // Get the Maven tool.
                // ** NOTE: This 'M3' Maven tool must be configured
                // **       in the global configuration.
                //  mvnHome = tool 'M3'
            }
        }
        stage('Build') { // for display purposes
            steps {
                // Run the maven build
                container("maven") {
                    sh "mvn -Dmaven.test.failure.ignore clean package"
                }
            }
        }
        stage('Results') { // for display purposes
            steps {
                junit '**/target/surefire-reports/TEST-*.xml'
                archiveArtifacts 'target/*.jar'
                //TODO: push to artifactory
            }
        }
        stage('Produce') { // for display purposes
            steps {
                // Notify DevOptics that this run produced plugin-a.txt.
                //   gateProducesArtifact file: 'target/*.jar'
                gateProducesArtifact file: 'pom.xml'
                publishEvent event: jsonEvent('{"event":"com.example:supply-chain-webapp:1.2-SNAPSHOT:war","mavenArtifact":{"groupId":"com.example", "artifactId":"cloudbees-jar", "version":"0.5-SNAPSHOT"}} '), verbose: true

            }
        }
    }
}