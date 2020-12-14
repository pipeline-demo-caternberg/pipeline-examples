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
                     withKubeConfig([credentialsId: 'kubeconf']) {
                        // sh 'kubectl apply -f my-kubernetes-directory'
                         sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                    }
                }
            }
        }
    }
}
