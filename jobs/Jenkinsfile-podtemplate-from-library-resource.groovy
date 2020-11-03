library '_github_com_pipeline-templates-apps_pipeline-library' _
def maven = libraryResource 'podtemplates/podTemplate-maven.yaml'
pipeline {
    agent {
        kubernetes {
            label 'mymavenlabel'
            yaml maven
        }
    }
    stages {
        stage('Say Hello') {
            steps {
                container('maven') {
                    echo 'Hello World!'
                }
            }
        }
    }
}
