pipeline {
    agent any

    environment {
        A_VALUE = 'Some Value'
    }

    stages {
        stage('Build') {
            steps {
                echo "THE CURRENT VALUE  IS:${A_VALUE}"
                echo "${env.BUILD_ID}"
                echo "##################################"
                echo "ALL ENV VARIABLES:"
                echo sh(script: 'env|sort', returnStdout: true)
            }
        }
    }
}
