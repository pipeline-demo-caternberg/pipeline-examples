pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-customagent.yml'
        }
    }
    parameters {
        string(name: 'kubectl_command', defaultValue: 'cluster-info',
                description: 'put a kubectl command')
    }
    stages {
        stage('Say Hello') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    sh "kubectl version"
                    sh "kubectl ${params.kubectl_command}"
                }
            }
        }
    }
}
