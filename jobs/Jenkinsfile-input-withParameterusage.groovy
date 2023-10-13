pipeline {
    agent none
    options {
        timeout(time: 2, unit: 'MINUTES')
    }
    stages {
        stage('Say Hello') {
            steps {
                script {
                    //If agent other than "any" the ìput`should take place outside a agent definition!!!
                    returnValue = input message: 'Need some input',
                            parameters: [string(defaultValue: '',
                                    description: '',
                                    name: 'Give me a value')]
                }
                echo "${returnValue}"
            }
        }

        stage('Say Hello2') {
            agent {
                kubernetes {
                    yamlFile 'resources/yaml/podTemplate-tools-os.yml'
                }
            }
            steps {
                container("curl") {
                    echo "Input is not blocking the agent: Returnvalue is:${returnValue}"
                }
            }
        }
    }
}