node {
    properties([
            parameters([
                    [$class: 'ChoiceParameter',
                     choiceType: 'PT_SINGLE_SELECT',
                     description: 'The names',
                     filterLength: 1,
                     filterable: true,
                     name: 'Name',
                     randomName: 'choice-parameter-5631314439613971',
                     script: [
                             $class: 'GroovyScript',
                             script: [
                                     classpath: [],
                                     sandbox: false,
                                     script: '''
                            println LOADVALUES
                            def url = 'https://raw.githubusercontent.com/cb-ci-templates/ci-shared-library/main/resources/json/test.json'
                            def response = new URL(url).getText()
                            def json = new groovy.json.JsonSlurper().parseText(response)
                            def valueList = []
                            json.each{key,value ->  valueList.add(value) }
                            println valueList
                            return valueList
                        '''
                             ]
                     ]
                    ],
            ])
    ])
}
pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: shell
                    image: ubuntu
                    command:
                    - sleep
                    args:
                    - infinity
                '''
            defaultContainer 'shell'
        }
    }

    stages {
        stage('Main') {
            steps {
                sh 'hostname'
            }
        }
    }
}
