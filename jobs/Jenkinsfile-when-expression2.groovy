pipeline {
    agent { node { label 'master' } }
    stages {
        stage('hello World') {
            steps {
                script {
                    DEPLOYMENT_ALLOWED = sh(script: """
                        echo true
          """, returnStdout: true).trim()
                }
                sh "echo ${DEPLOYMENT_ALLOWED}"
            }
        }

        stage ("next") {
          /*  environment  {
                DEPLOYMENT_ALLOWED = "${DEPLOYMENT_ALLOWED}"
            }
            */
            when { expression { return Boolean.valueOf("${DEPLOYMENT_ALLOWED}") } }
            steps {
             //   echo sh(script: 'env|sort', returnStdout: true)
                sh  "echo Hello, bitwiseman! ${DEPLOYMENT_ALLOWED}"
            }
        }
    }
}


