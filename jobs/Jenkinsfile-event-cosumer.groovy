library '_github.com_pipeline-demo-caternberg_workflowLibs' _
pipeline {
    /*comment to execute on master(f.e if curl is not available on agents).
    master executors has to be increased from default `0` to ```to get this executed ton master
    agent { label 'master'}
    */
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate.yml'
        }
    }
    triggers {
        eventTrigger jmespathQuery("contains(event,'com.example:' && '-SNAPSHOT')")
    }

    stages {
        stage('Example') {
            steps {
                container("curl") {
                    withCredentials([string(credentialsId: 'jenkinstoken', variable: 'ADMINTOKEN')]) {
                        echo "TOKEN: $ADMINTOKEN"
                        curlEventCause "admin" "$ADMINTOKEN"
                    }
                }
            }
        }
    }
}