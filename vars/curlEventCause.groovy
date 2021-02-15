/**
 * get the eventCause as JSON  for a triggerEvent from the related  ${env.BUILD_URLa}
 * Pre-requirement: secrettext as credential  "jenkinsuserandtoken"  fomat: <user>:<token>
 * @author: acaternberg@cloudbees.com
 * @param adminToken
 * @return export ${env.EVENT_CAUSE_JSON}
 */
def call(String jenkinsUserAndToken) {
  //  withCredentials([string(credentialsId: 'jenminsUserAndToken', variable: 'ADMINTOKEN')]) {
        echo "ADMINTOKEN: ${jenkinsUserAndToken}"
        sh "wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 && chmod +x ./jq"
        script {
            EVENT_CAUSE_JSON = sh(script: """
             curl -v  -u  ${jenkinsUserAndToken}  --silent  ${BUILD_URL}/api/json | ./jq '.actions[0].causes[0].event'
          """, returnStdout: true)
        }
        environment  {
            EVENT_CAUSE_JSON = ${EVENT_CAUSE_JSON}
        }
        echo sh(script: 'env|sort', returnStdout: true)
        echo "EVENT_CAUSE_JSON: ${EVENT_CAUSE_JSON}"

       // return ${EVENT_CAUSE_JSON}
  //  }
}


