library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
def buildPod = libraryResource 'podtemplates/podTemplate-maven-sonar.yaml'
def MVN_COMMAND_DEPLOY = "mvn -e -Djansi.force=true -Dstyle.color=always  -Dembedd-dependencies -Dmaven.repo.local=/tmp/.m2  -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true clean install"

pipeline {
    //When applied at the top-level of the pipeline block no global agent will be allocated for the entire Pipeline run and each stage section will need to contain its own agent section. For example: agent none
    agent {
        kubernetes {
            yaml buildPod
        }
    }
    stages {
        stage("build") {
            steps {
                container("maven") {
                    sh "${MVN_COMMAND_DEPLOY}"
                }
            }
        }
    }
    stage('Sonar') {
        options {
            skipDefaultCheckout(true)
        }
        steps {
            container('sonarscanner') {
                environment {
                    SONAR_HOST_URL="${sonar_host}"
                    SONAR_TOKEN="${sonar_token}"
                    //SONAR_SCANNER_OPTS="${sonar_scanner_options}"
                }
                withSonarQubeEnv("sonarqube") {
                    sh "sonar-scanner -Dproject.settings=./sonar-project.properties"
                }
            }
        }
    }
}
