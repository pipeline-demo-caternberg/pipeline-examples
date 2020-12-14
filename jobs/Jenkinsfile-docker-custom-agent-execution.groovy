def apiScript='resources/scripts/serviceAccountAPItest.sh'
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
        stage('ClusterAdmin') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    withKubeConfig(credentialsId: 'BearerClusterAdmin', namespace: 'cloudbees-core', serverUrl: 'https://35.196.164.234/') {
                        sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                        timeout(time: 3, unit: 'MINUTES') {
                            retry(5) {
                                //call external shell script
                                sh "${apiScript}"
                            }
                        }
                    }
                }
            }
        }
        stage('SAJenkins') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    withKubeConfig(credentialsId: 'BearerToken', namespace: 'cloudbees-core', serverUrl: 'https://35.196.164.234/') {
                        sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                    }
                }
            }
        }
    }
}
