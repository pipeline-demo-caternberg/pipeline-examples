// Uses Declarative syntax to run commands inside a container.
//This Pipeline shows how to clone a submodule repo via ssh
pipeline {
    environment{
        GIT_DISCOVERY_ACROSS_FILESYSTEM=1
    }
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: custom-agent
      image: caternberg/jenkins-agent-customized:latest
      runAsUser: 1000
      command:
        - cat
      tty: true
      workingDir: "/home/jenkins/agent"
      securityContext:
        runAsUser: 1000
'''
         defaultContainer 'custom-agent'
        }
    }
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                //see https://issues.jenkins.io/browse/JENKINS-60529
                //a submodule repo needs to be prepared first, see https://git-scm.com/book/en/v2/Git-Tools-Submodules and git@github.com:org-caternberg/submoduletest.git'
                checkout([$class: 'GitSCM', branches: [[name: '*/main']],
                              extensions: [
                                      [$class: 'SubmoduleOption',
                                       disableSubmodules: false,
                                       parentCredentials: true,
                                       recursiveSubmodules: true,
                                       shallow: true,
                                       trackingSubmodules: false]
                              ],
                              submoduleCfg: [],
                              userRemoteConfigs: [
                                      [credentialsId: 'github-user-ssh',
                                       url: 'git@github.com:org-caternberg/submoduletest.git']
                              ]
                    ]
                    )

            }
        }
    }
}
