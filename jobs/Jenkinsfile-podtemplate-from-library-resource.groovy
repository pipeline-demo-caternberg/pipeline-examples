library '_git@github.com:pipeline-demo-caternberg/pipeline-library.git_'
def testPodYaml = libraryResource 'podtemplates/nodejs-app/web-test-pod.yml'
pipeline {
     agent {
        kubernetes {
          label 'nodejs-testcafe'
          yaml web-test-pod.yml
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
