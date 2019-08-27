pipeline {
    agent { label 'cloudbees-core' }
    stages {
        stage('Say Hello') {
            steps {
                container('maven') {
                    echo 'Hello World!'
                }
            }
        }
    }
}
