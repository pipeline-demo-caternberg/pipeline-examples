library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
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
