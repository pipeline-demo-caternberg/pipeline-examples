pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-customagent.yml'
        }
    }
    stages {
        stage('hello World') {
            steps {
                echo "Hello World"
                sh 'ls -lR . && ls -ltr /tmp'
            }
        }
    }
}
