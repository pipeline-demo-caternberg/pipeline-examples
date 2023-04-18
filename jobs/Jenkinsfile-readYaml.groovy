//see https://stackoverflow.com/questions/57261787/use-object-returned-by-readyaml-from-file-in-declarative-jenkinsfile
//see https://stackoverflow.com/questions/71980010/what-is-the-proper-way-to-use-readyaml-in-jenkins-declarative-pipeline
//see https://roytuts.com/how-to-read-yaml-file-into-jenkinsfile/
def loadValuesYaml(x){
    def valuesYaml = readYaml (file: './chart/values.yaml')
    return valuesYaml[x];
}

pipeline {
    agent {
        label "jenkins-maven"
    }
    environment {
        APP=loadValuesYaml('appName')
        REPLICACOUNT=loadValuesYaml('replicaCount')
    }
    stages {
        stage('CICD Initialize') {
            steps {
                script{
                    println APP
                    println REPLICACOUNT
                }
            }
        }
    }
}