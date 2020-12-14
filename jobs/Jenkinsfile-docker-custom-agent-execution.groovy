pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-customagent.yml'
        }
    }
    stages {
        stage('Say Hello') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    sh "kubectl version"
                }
            }
        }
    }
}
