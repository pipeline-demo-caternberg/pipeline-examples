pipeline {
    agent any

    environment {
        A_VALUE = 'Some Value'
    }

    stages {
        stage('Build') {
            steps {
                echo "${A_VALUE}"
                echo "${env.BUILD_ID}"
                echo sh(script: 'env|sort', returnStdout: true)
            }
        }
    }
}
