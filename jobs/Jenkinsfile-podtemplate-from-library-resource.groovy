library '_github_com_pipeline-demo-caternberg_pipeline-library' _
def testPodYaml = libraryResource 'podtemplates/nodejs-app/web-test-pod.yml'
pipeline {
    agent {
        kubernetes {
            label 'nodejs-testcafe'
            yaml testPodYaml
        }
    }
    stages {
        stage('Say Hello') {
            steps {
                container('nodejs-testcafe') {
                    echo 'Hello World!'
                }
            }
        }
    }
}
