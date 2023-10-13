/*
We import the JsonSlurper to simulate some workload on the Controller.
JsonSlurper is NOT recommended to be used in production Pipelines. It can slow down the performance!
*/
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: shell
                    image: busybox
                    command:
                    - sleep
                    args:
                    - infinity
                '''
            defaultContainer 'shell'
        }
    }
    stages {
        stage('One') {
            steps {
                /*
                Groovy inside scripts tag will be executed on the Controller!, anyhow, it is inside an agent context
                */
                script {
                // Avoid this! see https://docs.cloudbees.com/docs/cloudbees-ci/latest/pipelines/pipeline-best-practices#_reduce_the_amount_of_groovy_code_executed_by_pipelines
                    def jsonSlurper = new JsonSlurper()
                    def objectJson = jsonSlurper.parseText('{ "myList": [4, 8, 15, 16, 23, 42] }')
                    print objectJson
                    /*This requires 2 script approvals under https://CONTROLLER_URL/anage/scriptApproval/
                    * method java.net.InetAddress getCanonicalHostName
                    * staticMethod java.net.InetAddress getLocalHost
                    */
                    println("Running on Controller Host : " + InetAddress.localHost.canonicalHostName)
                }
                echo "Tip run: watch kubectl top pod"
                sleep 1000
                sh 'echo Running on AGENT-HOST: $(hostname)'
            }
        }
        stage('two') {
            steps {
                sh 'echo Running on AGENT-HOST: $(hostname)'
            }
        }
    }
}
