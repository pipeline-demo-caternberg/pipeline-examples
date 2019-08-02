pipeline {
    /*comment to execute on master(f.e if curl is not available on agents).
    master executors has to be increased from default `0` to ```to get this executed ton master
    agent { label 'master'}
    */
    agent { label "cloudbees-core"}
    triggers {
        eventTrigger jmespathQuery("contains(event,'com.example:' && '-SNAPSHOT')")
    }
    stages {
        stage ("ConsumeArtifact"){
            steps {
                gateConsumesArtifact id: "pom.xml", type: 'xml'
            }
        }
        stage('meta-info') {
            steps {
                //see ttps://cb-technologists.github.io/posts/cloudbees-cross-team-and-dev-sec-ops/

                script {
                    println currentBuild.getBuildCauses()[0].event.toString()
                }
                container ("curl") {
                    sh "wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 && chmod +x ./jq"
                    sh "curl -u admin:11af9308704300b89422c24e34b408332d --silent ${BUILD_URL}/api/json | ./jq '.actions[0].causes[0].event'"
                    // echo sh(script: 'env|sort', returnStdout: true)
                }
            }   //  sh "env"
        }
    }
}