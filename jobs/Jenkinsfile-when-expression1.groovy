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
            when { expression { return Boolean.valueOf("${DEPLOYMENT_ALLOWED}") } }
            steps {
                sh  "echo Hello, bitwiseman! ${DEPLOYMENT_ALLOWED}"
            }
        }
    }
}


