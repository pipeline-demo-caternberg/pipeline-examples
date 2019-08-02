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
        stage('Example') {

            steps {
                container ("curl"){
                    script {
                        println currentBuild.getBuildCauses()[0].event.toString()
                    }
                    //https://cb-technologists.github.io/posts/cloudbees-cross-team-and-dev-sec-ops/
                    sh "wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 && chmod +x ./jq"
                    sh "curl -u admin:11af9308704300b89422c24e34b408332d --silent ${BUILD_URL}/api/json | ./jq '.actions[0].causes[0].event'"
                    sh 'echo received helloWorld  '
                    echo "BUILDINFO"
                    // echo sh(script: 'env|sort', returnStdout: true)
                }

            }
        }

    }
}