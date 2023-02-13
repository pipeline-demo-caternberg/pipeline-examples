library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
import groovy.json.JsonSlurper;
//def response = new URL('https://NXUS/service/rest/v1/search?repository=shared-demos&group=org.springframework.samples&name=spring-petclinic').text;
def pipelineProperties = libraryResource 'json/pipelineProperties.json'
//print pipelineProperties

//Datatype needs to be serializable!
String repo = "";
String id = "";
String key = "";
new JsonSlurper().parseText(pipelineProperties).each {
    print it.repo
    if (it.repo == "repo1") {
        repo = it.repo
        id = it.id
        key = it.key1
    }
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
                echo "${repo}"
                echo "${id}"
                echo "${key}"
            }
        }
    }
}
