pipeline {
    agent {
        kubernetes {
            yamlFile 'yanl/podTemplate.yml'
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
