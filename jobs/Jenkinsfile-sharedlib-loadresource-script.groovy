library '_github.com_pipeline-demo-caternberg_pipeline_examples' _
def ostools = libraryResource 'podtemplates/podTemplate-os-tools.yaml'
pipeline {
    agent {
        kubernetes {
            yaml ostools
        }
    }
      stages {
        stage('Stage1') {
            steps {
                sh "${get_resource_dir()}/scripts/parametrizedscript.sh"
            }
        }
    }
}