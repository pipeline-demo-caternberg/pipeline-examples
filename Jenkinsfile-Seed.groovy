pipeline {
    agent { node { label 'cloudbees-core' } }
    stages {
        stage('buildLib') {
            steps {
                container ('gradle'){
                    echo "Hello World"
                    sh 'gradle clean lib'
                }
            }
        }
        stage('SetClasspath') {
            steps {
                echo "addToClasspath"
                sh 'addToClasspath lib/*.jar'
            }
        }
    }
}
