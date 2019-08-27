library '_github.com_pipeline-demo-caternberg_workflowLibs' _
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
                container("curl") {
                    withCredentials([string(credentialsId: 'jenkinsuserandtoken', variable: 'ADMINTOKEN')]) {
                        curlEventCause "$ADMINTOKEN"
                    }
                }
            }   //  sh "env"
        }
    }
}