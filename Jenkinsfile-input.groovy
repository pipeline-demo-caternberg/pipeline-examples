pipeline {
    agent none
    stages {
        stage('Say Hello') {
            steps {
                script {
                    //If agent other than "any" the ìput`should take place outside a agent definition!!!
                   returnValue = input message: 'your input',
                            parameters: [string(defaultValue: 'myvalue',
                                    description: '',
                                    trim: true)],
                            submitter: 'admin'
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