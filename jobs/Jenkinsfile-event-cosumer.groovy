library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
pipeline {
    /*comment to execute on master(f.e if curl is not available on agents).
    master executors has to be increased from default `0` to ```to get this executed ton master
    agent { label 'master'}
    */
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-tools-os.yml'
        }
    }
    triggers {
        eventTrigger jmespathQuery("contains(event,'com.example:' && '-SNAPSHOT')")
    }

    stages {
        stage('Example') {
            steps {
                container("curl") {
                    echo "called"
                }
            }
        }
    }
}
