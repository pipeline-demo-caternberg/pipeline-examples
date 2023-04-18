import de.caternberg.pipeline.helper.GlobalVars
def call(Map pipelineParams) {
    // evaluate the body block, and collect configuration into the object
    //def pipelineParams= [:]
    // body.resolveStrategy = Closure.DELEGATE_FIRST
    // body.delegate = pipelineParams
    //body()
    pipeline {
        agent {
            kubernetes {
                yamlFile 'resources/podtemplates/podTemplate-curl.yaml'
            }
        }
        stages {
            stage('Say Hello') {
                steps {
                    echo 'Hello World!'
                    echo "${GlobalVars.foo}"
                    sh "echo ${pipelineParams.param1}"
                    sh "echo ${pipelineParams.param2}"
                }
            }
        }
    }

}
