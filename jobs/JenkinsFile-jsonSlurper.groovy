library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
import groovy.json.JsonSlurper;

def pipelineProperties = libraryResource 'json/pipelineProperties.json'
//print pipelineProperties

//HashMaps requires Jenkins Script Approval!
//LinkedHashMap repoMap = new LinkedHashMap();

//Strings doesnt require Jenkins Script Approval!, so this should be preferred
String repo = "";
String id = "";
String key = "";
String buildTimeout = "";
// ‘checkout scm’ is only available when using “Multibranch Pipeline” or “Pipeline script from SCM”
//For Git
def repositoryUrl = scm.userRemoteConfigs[0].url
//For SVN
//def repositoryUrl = scm.locations[0].remote
print repositoryUrl

new JsonSlurper().parseText(pipelineProperties).each {

    //if (it.repo == "repo1") {
    if (it.repo == repositoryUrl) {
        println it
        repo = it.repo
        id = it.id
        key = it.key1
        buildTimeout = it.timeout
        //HashMaps requires Jenkins Script Approval!
        //repoMap << it
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
    options {
        //Requires plugin https://github.com/jenkinsci/build-timeout-plugin
        timeout(time: buildTimeout, unit: 'SECONDS')
    }
    stages {
        stage('Main') {


            steps {
                sh 'hostname'
                echo "${repo}"
                echo "${id}"
                echo "${key}"
                echo "${buildTimeout}"
                //echo "${repoMap.repo}"
            }
        }
    }
}
