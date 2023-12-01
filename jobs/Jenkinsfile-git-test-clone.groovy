def repo= "https://github.com/pipeline-demo-caternberg/pipeline-examples.git"
def branch="master"
pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: shell
    image: caternberg/ci-toolbox:latest
    command:
    - sleep
    args:
    - infinity
'''
            defaultContainer 'shell'
        }
    }
    options {
        skipDefaultCheckout()
    }
    stages {
        stage('Main') {
            steps {
                parallel(
                        checkoutOption: {
                            dir("checkout-option") {
                                checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: branch]], extensions: [], userRemoteConfigs: [[url: repo]]]
                            }
                        },
                        gitOption: {
                            dir("git-option") {
                                git branch: branch, changelog: false,  poll: false, url: repo
                            }
                        },
                        manualGitCliOption: {
                            dir ("manual-GitCliOption"){
                                customCheckout()
                            }
                        },
                )
            }
        }
    }
}

def customCheckout() {
    // withCredentials([usernamePassword( credentialsId: '<git-creds>', passwordVariable: 'GIT_PASS', usernameVariable: 'GIT_USER')] ) {
    //       #echo "machine github.com\nlogin ${GIT_USER}\npassword ${GIT_PASS}\n" > "${HOME}/.netrc"
    /*
       mkdir -p "${WORKSPACE}/manual-git-cli-option/"
        cd "${WORKSPACE}/manual-git-cli-option/"
                git init "${WORKSPACE}/manual-git-cli-option/"
     */
    sh '''     
       git clone https://github.com/pipeline-demo-caternberg/pipeline-examples.git
        git fetch --no-tags --force -- https://github.com/pipeline-demo-caternberg/pipeline-examples.git"+refs/heads/${branch}:refs/remotes/origin/${branch}"
        git config remote.origin.url https://github.com/pipeline-demo-caternberg/pipeline-examples.git
        git config --add remote.origin.fetch "+refs/heads/master:refs/remotes/origin/master"
        git checkout -f "8a781356dca101cede4db53ab6e39053a004dd7d"
        git rev-list --no-walk "8a781356dca101cede4db53ab6e39053a004dd7d"
        '''
    // }
}
