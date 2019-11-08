pipeline {
    agent {
        kubernetes {
            yamlFile 'yaml/podTemplate.yml'
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
