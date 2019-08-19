//Dmaven.repo.local=/cache  must point to the container's volume mountpath mountPath: /cache .  (see below)
//A local node volume or a PVC must be configured in the podtemplate
def MVN_COMMAND = 'mvn -X  -Dmaven.repo.local=/cache clean'
pipeline {
    agent {
        kubernetes {
            label 'cache_test'
            defaultContainer 'jnlp'
            yaml """
    apiVersion: v1
    kind: Pod
    metadata:
      labels:
        some-label: some-label-value
    spec:
      containers:
      - name: maven-one
        image: maven:3.3.9-jdk-8-alpine
        command:
        - cat
        tty: true
        volumeMounts:
        # directory location in container
        - mountPath: /cache
          name: maven-cache
      - name: maven-two
        image: maven:3.3.9-jdk-8-alpine
        command:
        - cat
        tty: true
        volumeMounts:
         # directory location in container
        - mountPath: /cache
          name: maven-repo
      volumes:
      - name: maven-cache
        persistentVolumeClaim:
          - mountPath: '/tmp/cache'
            claimName: 'maven-repo'
          """
        }
    }

    stages {
        stage('Clean Cache') {
            steps {
                container('maven-one') {
                    //UNCOMENT TO ERASE ALL LOCAL ARTIFACTS FROM LOCAL CACHE
                    //sh "rm -Rf -v /cache/org"
                    sh "ls -la /cache"
                }
            }
        }

        stage('Run maven') {
            steps {
                container('maven-one') {
                    //just test if we can receive an artifact with curl.
                    sh 'curl http://repo1.maven.org/maven2/org/apache/maven/plugins/maven-clean-plugin/2.5/maven-clean-plugin-2.5.jar -o /cache/test.jar'
                    sh "ls -la /cache"
                    git 'https://github.com/cccaternberg/example-maven-api.git'
                    sh 'ls -ltr'
                    sh "${MVN_COMMAND}"
                }
            }
        }
        stage('Run another maven') {
            steps {
                container('maven-two') {
                    git 'https://github.com/cccaternberg/example-maven-api.git'
                    sh "ls -la /cache"
                    sh "${MVN_COMMAND}"
                }
            }
        }
    }

}


