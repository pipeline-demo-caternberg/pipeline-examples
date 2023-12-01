pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                    - name: maven
                      image: maven:3.9.5-eclipse-temurin-17-alpine
                      command:
                        - sleep
                      args:
                        - infinity
                      containers:
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
                 '''
            defaultContainer 'maven'
        }
    }
    stages {
        stage('Build') {
            steps {
                git "https://github.com/org-folderscan-example/spring-boot-demo.git"
                //see https://plugins.jenkins.io/pipeline-maven/
                withMaven(
                        // Use `$WORKSPACE/.repository` for local repository folder to avoid shared repositories
                        mavenLocalRepo: '.repository',
                        mavenSettingsConfig: 'global-maven-settings'
                ) {
                    // Run the maven build
                    sh "mvn  clean install"
                }
                archiveArtifacts "target/*.war"
            }
        }

        stage("Docker") {
            options {
                skipDefaultCheckout(true)
            }
            steps {
                container(name: 'kaniko', shell: '/busybox/sh') {
                    withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
                        sh '''#!/busybox/sh
                              /kaniko/executor  --dockerfile $(pwd)/Dockerfile --insecure --skip-tls-verify --cache=false  --context $(pwd) --destination caternberg/spring-boot-demo:${GIT_COMMIT_SHORT}
                          '''
                    }
                }
            }
            post {
                success {
                    echo "Docker Build Successfully"
                    //Slack notification....
                    //Jira update
                }
                failure {
                    echo "Docker Build Failed"
                    //Slack notification....
                    //Jira update
                }
            }
        }
    }
}



