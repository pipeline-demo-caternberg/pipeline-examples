pipeline {
    agent none
    stages {
        stage('Stage Stash') {
            agent {
                kubernetes {
                    yamlFile 'resources/yaml/podTemplate-tools-os.yml'
                }
            }
            steps {
                container('curl'){
                    // checkout scm
                    // sh 'make'
                    sh 'echo Some value from Stage1: $BUILD_NUMBER > buildnumber.txt'
                    stash includes: '**/buildnumber.txt', name: 'buildnumber'
                }
            }
        }
        stage('Stage Unstash') {
            agent {
                kubernetes {
                    yamlFile 'resources/yaml/podTemplate-maven.yml'
                }
            }
            steps {
                container('maven'){
                    unstash 'buildnumber'
                    sh 'ls -l'
                    sh 'cat buildnumber.txt'
                }
            }
        }
    }
}