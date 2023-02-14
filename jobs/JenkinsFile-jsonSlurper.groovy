library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
import groovy.json.JsonSlurper;

def pipelineProperties = libraryResource 'json/pipelineProperties.json'
//print pipelineProperties

//HashMaps requires Jenkins Script Approval!
LinkedHashMap repoMap = new LinkedHashMap();

//Strings doesnt require Jenkins Script Approval!, so this should be preferred
String repo = "";
String id = "";
String key = "";
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
        //HashMaps requires Jenkins Script Approval!
        repoMap << it
    }
}
println repoMap

pipeline {
    agent {
        kubernetes {
            // Rather than inline YAML, in a multibranch Pipeline you could use: yamlFile 'jenkins-pod.yaml'
            // Or, to avoid YAML:
            // containerTemplate {
            //     name 'shell'
            //     image 'ubuntu'
            //     command 'sleep'
            //     args 'infinity'
            // }
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
            // Can also wrap individual steps:
            // container('shell') {
            //     sh 'hostname'
            // }
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
                echo "${repoMap.repo}"
            }
        }
    }
}
