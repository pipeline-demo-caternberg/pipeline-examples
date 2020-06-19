pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-dind.yaml'
        }
    }
    stages {
        stage('Say Hello') {
            steps {
                container('dind') {
                     git 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
                     sh 'docker build -t caternberg/dindtest -f $(pwd)/resources/dockerfiles/Dockerfile-custom-jnlp-agent  .'
                }
            }
        }
    }
}
