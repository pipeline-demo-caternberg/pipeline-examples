pipeline {
    agent none
    stages {
        stage('Say Hello') {
            steps {
                script {
                    //If agent other than "any" the ìput`should take place outside a agent definition!!!
                   returnValue = input id: 'Inpu1', message: 'your input',
                            parameters: [string(defaultValue: 'myvalue',
                                    description: '',
                                    name: 'TEXTINPUT',
                                    trim: true)],
                            submitter: 'admin',
                            submitterParameter: 'INPUTUSER'
                }
                echo "entered value: ${returnValue} by user: ${INPUTUSER}"
            }
        }

        stage('Say Hello2') {
            agent { label "cloudbees-core" }
            steps {
            container("curl") {
                    echo "Input is not blocking the agent: Returnvalue is:${returnValue}"
            }
           }
        }
    }
}