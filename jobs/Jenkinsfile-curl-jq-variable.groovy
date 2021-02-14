pipeline {
    agent { node { label 'master' } }
    stages {
        stage('hello World') {
            steps {
                script {
                    DEPLOYMENT_ALLOWED = sh(script: """
echo '{"result":{"deploymentAllowed":"false","caseType":"not found"}}' | jq -r .result.deploymentAllowed
          """, returnStdout: true)
                }
                environment {
                    DEPLOYMENT_ALLOWED = ${DEPLOYMENT_ALLOWED}
                }
                echo sh(script: 'env|sort', returnStdout: true)
                echo "DEPLOYMENT_ALLOWED: ${DEPLOYMENT_ALLOWED}"
            }
        }
    }
}

