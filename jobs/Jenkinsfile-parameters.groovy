pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-tools-os.yml'
        }
    }

    parameters {
        string(name: 'Greeting', defaultValue: 'Hello',
                description: 'How should I greet the world?')
    }

    stages {
        stage('Example') {
            steps {
                echo "${params.Greeting} World!"
                script {
                    echo params.Greeting
                }

            }
        }
    }
}
