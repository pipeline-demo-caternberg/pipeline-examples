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
                  // get shell from libraryResource, and then make a file on /dev/shm/myfiles
                  writeFile file:"/tmp/parametrizedscript.sh", text:libraryResource("scripts/parametrizedscript.sh")
                  // add execute permission
                  sh "chmod +x /tmp/*.sh && ls -l /tmp/"
                  // run shell with params
                  sh "/tmp/parametrizedscript.sh -b test1 -c test2"
              }
          }
          stage('Stage3') {
              environment{
                  MYTESTPARAM="MYTESTPARAMVALUE"
              }
              steps {
                  //sh "${get_resource_dir()}/scripts/parametrizedscript.sh"
                  sh libraryResource("scripts/parametrizedscript.sh")
              }
          }

    }
}