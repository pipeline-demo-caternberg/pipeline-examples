library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
def mavenPod = libraryResource 'podtemplates/podTemplate-maven.yaml'
// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent none
/*    triggers {
        eventTrigger simpleMatch('helloWorld')
    }*/

    stages {

        stage('Hello') {
            agent {
                kubernetes {
                    //#deprectatedlabel 'mavenPod'
                    defaultContainer "maven"
                    yaml mavenPod
                }
            }
            steps {
                sh 'hostname'
                helloWorld "Andreas"
            }
        }
    }
    post {
        always {
            echo "${BUILD_URL}"
            publishEvent event: jsonEvent("{eventName: 'archive', url: \"$BUILD_URL\"}"), verbose: true

        }
    }
}
