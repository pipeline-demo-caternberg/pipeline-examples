pipeline {
    agent none
    stages {
        stage('Stage Stash') {
            agent { label 'cloudbees-core'}
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
            agent { label 'cloudbees-core'}
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