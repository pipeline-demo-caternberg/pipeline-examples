pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-maven.yml'
        }
    }
    stages {
        stage('Say Hello') {
            steps {
                echo 'Hello World!'
                sleep 10
                checkpoint 'Hello'
            }
        }
        stage('Say Good By') {
            steps {
                echo 'Say Good By'
                checkpoint 'ByBy'
            }
        }
    }
}