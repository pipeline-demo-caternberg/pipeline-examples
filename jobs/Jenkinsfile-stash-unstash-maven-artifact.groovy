pipeline {
    agent none

    stages {
        stage('Stash') {
            agent {
                kubernetes {
                    yamlFile 'yaml/podTemplate.yml'
                }
            }
            steps {
                container('maven') {
                    git credentialsId: 'githubuserssh', url: 'git@github.com:pipeline-demo-caternberg/maven-project.git'
                    sh 'mvn clean package'
                    stash includes: '**/target/*.war', name: 'myapp'
                    //sh 'ls -l'
                    //sh 'echo Some value from Stage1: $BUILD_NUMBER > buildnumber.txt'
                    // stash includes: '**/buildnumber.txt', name: 'myapp'
                }
            }
        }
        stage('Unstash') {
            agent {
                kubernetes {
                    yamlFile 'yaml/podTemplate.yml'
                }
            }
            steps {
                container('curl') {
                    unstash 'myapp'
                    sh 'ls -lR'
                    //sh 'cat buildnumber.txt'
                }
            }
        }
    }
}
