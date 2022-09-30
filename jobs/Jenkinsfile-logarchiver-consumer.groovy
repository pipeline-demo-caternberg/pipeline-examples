library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
def pod = libraryResource 'podtemplates/podTemplate-curl.yaml'
// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent {
        kubernetes {
            defaultContainer "custom-agent"
            yaml pod
        }
    }
    triggers {
        eventTrigger jmespathQuery("eventName=='archive'")
    }
    stages {
        stage('archive') {
            //see https://stackoverflow.com/questions/56534579/how-to-pass-aws-secret-key-and-id-in-jenkins-build-pipeline-script
           /*
            environment {
                AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
            }
            */
            steps {
                echo "AWS_KEY ${AWS_ACCESS_KEY_ID}"
                archiveS3 ()
            }
        }

    }
}