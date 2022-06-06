pipeline {
    agent {
        kubernetes {
            yaml """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: golang
    image: golang:1.12
    command:
    - cat
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: jenkins-docker-cfg
        mountPath: /kaniko/.docker
  volumes:
  - name: jenkins-docker-cfg
    projected:
      sources:
      - secret:
          name: docker-credentials
          items:
            - key: .dockerconfigjson
              path: config.json
"""
        }
    }
    stages {
        stage('Build') {
            steps {
                container('golang') {
                    sh 'ls -ltr && chmod +x *.sh  && ./build-go-bin.sh'
                }
            }
        }
        stage('Make Image') {
            environment {
                PATH        = "/busybox:$PATH"
                REGISTRY    = 'index.docker.io' // Configure your own registry
                REPOSITORY  = 'caternberg'
                IMAGE       = "helloworldgolang:${GIT_COMMIT}"
            }
            steps {
                container(name: 'kaniko', shell: '/busybox/sh') {
                    sh '''#!/busybox/sh
                    /kaniko/executor -f `pwd`/Dockerfile.run -c `pwd` --cache=true --destination=${REGISTRY}/${REPOSITORY}/${IMAGE}
                    '''
                }
            }
        }
    }
}