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
                //sh "${get_resource_dir()}/scripts/parametrizedscript.sh"
                // get shell from libraryResource, and then make a file on workspace
                writeFile file:"${WORKSPACE}/parametrizedscript.sh", text:libraryResource("scripts/parametrizedscript.sh")
                // add execute permission
                sh "chmod +x *.sh && ls -l"
                // run shell with params
                sh "./parametrizedscript.sh -b test1 -c test2"
            }
        }
          stage('Stage2') {
              steps {
                  //sh "${get_resource_dir()}/scripts/parametrizedscript.sh"
                  sh libraryResource("util/shell/test.sh") "MYTESTPARAM"
              }
          }

    }
}