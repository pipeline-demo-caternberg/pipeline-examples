pipeline {
    agent none
    stages {
        stage('Stage Stash') {
            agent {
                kubernetes {
                    yamlFile 'resources/podtemplates/podTemplate-maven.yaml'
                }
            }
            steps {
                container('maven') {
                    // checkout scm
                    // sh 'make'
                    //sh 'echo Some value from Stage1: $BUILD_NUMBER > buildnumber.txt'
                   //sh "for i in {1..50}; do touch  testfile-$i ; done"
                   //stash includes: '**/buildnumber.txt', name: 'testfile'
                   stash includes: '**/*', name: 'testfiles'
                }
            }
        }
        stage('Stage Unstash') {
            agent {
                kubernetes {
                    yamlFile 'resources/podtemplates/podTemplate-maven.yaml'
                }
            }
            steps {
                container('maven') {
                    unstash 'testfiles'
                    sh 'ls -l'
                    //sh 'cat buildnumber.txt'
                }
            }
        }
    }
}
