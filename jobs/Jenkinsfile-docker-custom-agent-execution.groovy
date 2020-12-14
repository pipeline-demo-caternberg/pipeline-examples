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
                     withKubeConfig([credentialsId: '74d29bc5-b2ca-4253-b639-141e6549245a']) {
                        // sh 'kubectl apply -f my-kubernetes-directory'
                         sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                    }
                }
            }
        }
    }
}
