pipeline {
    agent { node { label 'master' } }
    stages {
        stage('hello World') {
            steps {
                echo "Hello World"
                sh 'ls -lR . && ls -ltr /tmp'
            }
        }
    }
}
