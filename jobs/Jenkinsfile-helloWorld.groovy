pipeline {
    agent any
    stages {
        stage('hello World') {
            steps {
                echo "Hello World"
                sh 'ls -lR . && ls -ltr /tmp'
            }
        }
    }
}
