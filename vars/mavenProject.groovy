import de.caternberg.pipeline.helper.GlobalVars

def call(Map pipelineParams) {
    //evaluate the body block, and collect configuration into the object
    //def config = [:]
    //body.resolveStrategy = Closure.DELEGATE_FIRST
    //body.delegate = config
    //body()
    pipeline {
        agent {
            kubernetes {
                yamlFile 'resources/yaml/podTemplate-maven.yml'
            }
        }

        options {
            buildDiscarder(logRotator(numToKeepStr: '5'))
        }

        stages {
            stage('Checkout') {
                steps {
                    git credentialsId: "${pipelineParams.credentialId}",
                            url: "${pipelineParams.gitRepo}",
                            branch: "${pipelineParams.branch}"
                    sh "echo S{GlobalVars.foo}"
                    }
                }
            stage('Build') {
                steps {
                    container('maven') {
                        sh 'mvn clean package'
                    }
                }
            }
            stage('Test') {
                steps {
                    echo 'Test our code'
                }
            }
            stage('Archive') {
                steps {
                    echo 'Archive our artifacts'
                }
            }
            stage('Deploy') {
                steps {
                    echo 'Deploy our application'
                }
            }
        }
    }
}
