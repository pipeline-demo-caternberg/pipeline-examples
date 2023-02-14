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
Integer timeout = 0;
// ‘checkout scm’ is only available when using “Multibranch Pipeline” or “Pipeline script from SCM”
//For Git
def repositoryUrl = scm.userRemoteConfigs[0].url
print repositoryUrl
//For SVN
//def repositoryUrl = scm.locations[0].remote
new JsonSlurper().parseText(pipelineProperties).each {
    print it.repo
    //if (it.repo == "repo1") {
    if (it.repo == repositoryUrl) {
        repo = it.repo
        id = it.id
        key = it.key1
        timeout = it.tinmeout
        //HashMaps requires Jenkins Script Approval!
        //repoMap << it
    }
}
println repoMap

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
    script {
        options {
            // Timeout counter starts AFTER agent is allocated
            timeout(time: "${timeouit}", unit: 'SECONDS')
        }ad
    }

    stages {
        stage('Main') {
            steps {
                sh 'hostname'
                echo "${repo}"
                echo "${id}"
                echo "${key}"
                echo "${timeout}"
                //echo "${repoMap.repo}"
            }
        }
    }
}
