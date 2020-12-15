def apiScript='resources/scripts/serviceAccountAPItest.sh'
def credentialID="a5e189cf-d372-4b05-9f39-8c24952850e2"
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

        stage('SAJenkinsK8sAccount') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    withKubeConfig(credentialsId: "${credebdialID}" ,namespace: 'cloudbees-core', serverUrl: 'https://35.196.164.234/') {
                        sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                    }
                }
            }
        }
        stage('SAJenkins') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    withKubeConfig(credentialsId: 'a5e189cf-d372-4b05-9f39-8c24952850e2', namespace: 'cloudbees-masters', serverUrl: 'https://35.196.164.234/') {
                        sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                    }
                }
            }
        }
       /* stage('ClusterAdmin') {
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
        }*/

    }
}
