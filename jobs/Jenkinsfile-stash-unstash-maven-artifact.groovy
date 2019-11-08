pipeline {
    agent none

    stages {
        stage('Stash') {
            agent {
                kubernetes {
                    defaultContainer 'stash'
                    yaml """
            apiVersion: v1
            kind: Pod
            metadata:
              labels:
                some-label: mypodtemplate1
            spec:
              containers:
              - name: stash
                image: maven:3.3.9-jdk-8-alpine
                runAsUser: 1000
                command:
                - cat
                tty: true
            """
                }
            }
            steps {
                container('stash') {
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
                    defaultContainer 'unstash'
                    yaml """
            apiVersion: v1
            kind: Pod
            metadata:
              labels:
                some-label: mypodtemplate2
            spec:
              containers:
              - name: unstash
                image: appropriate/curl
                runAsUser: 1000
                command:
                - cat
                tty: true
                workingDir: "/home/jenkins/agent"
            """
                }
            }
            steps {
                container('unstash') {
                    unstash 'myapp'
                    sh 'ls -lR'
                    //sh 'cat buildnumber.txt'
                }
            }
        }
    }
}
