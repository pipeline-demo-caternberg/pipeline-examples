


def adminToken = "admin:11d2b842e59277c949c1cd2e2aa1497c6a"
pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                // withCredentials([string(credentialsId: 'jenkinsAdminAndToken', variable: 'ADMINTOKEN')]) {
                echo "ADMINTOKEN: ${adminToken}"
                sh "curl  -o ./jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 && chmod +x ./jq"
                script {
                    DEPLOYMENT_ALLOWED = sh(script: """
                          echo true
            """, returnStdout: true).trim()
                }
                sh "echo ${DEPLOYMENT_ALLOWED}"

                script {
                      EVENT_CAUSE_JSON = sh(script: """
                            curl -v  -u  ${adminToken}  --silent  ${BUILD_URL}/api/json | ./jq '.actions[0].causes[0].event'
            """, returnStdout: true).trim()
                }
             //   environment {
           //         EVENT_CAUSE_JSON = ${EVENT_CAUSE_JSON}
             //   }
                echo sh(script: 'env|sort', returnStdout: true)
                echo "EVENT_CAUSE_JSON: ${EVENT_CAUSE_JSON}"


                // return ${EVENT_CAUSE_JSON}
            }
        }
        //}
    }
}



