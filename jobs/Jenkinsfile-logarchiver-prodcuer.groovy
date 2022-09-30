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
                sh "touch test.zip"
                archiveArtifacts allowEmptyArchive: true, artifacts: '*', followSymlinks: false
            }
        }
    }
    post {
        always {
            echo "${BUILD_URL}"
            //enable for real commits
            //publishEvent event: jsonEvent("{eventName: 'archive', url: \"$BUILD_URL\",build_tag: \"$BUILD_TAG\", git_commit: \"$GIT_COMMIT\"}"), verbose: true

            //fake git commit
            publishEvent event: jsonEvent("{eventName: 'archive', url: \"$BUILD_URL\",buildtag: \"$BUILD_TAG\", gitcommit: '1234567'}"), verbose: true

        }
    }
}
