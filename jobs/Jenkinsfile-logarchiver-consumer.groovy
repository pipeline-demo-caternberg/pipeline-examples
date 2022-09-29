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
            steps {
                archiveS3 ()
                 //sh " curl  -k   -u  admin:admin https://sda.acaternberg.flow-training.beescloud.com/controller0/job/folder-one/job/Logarchiver-prodcuer/112/consoleText"
            }
        }

    }
}