

def script
node('cloudbees-core') {
    stage("listCreds"){
        git 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
        script = load 'resources/groovy/crdeenetials-list.groovy'
        script.credentialsList()
    }

}



