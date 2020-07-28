pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-maven.yml'
        }
    }
    stages {
        stage('Say Hello') {
            steps {
                container('maven') {
                    echo 'Hello World!'
                }
            }
        }
    }
}
