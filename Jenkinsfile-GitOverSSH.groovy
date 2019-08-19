pipeline {
    agent {
        kubernetes {
            label 'git_test'
            defaultContainer 'git'
            yaml """
    apiVersion: v1
    kind: Pod
    metadata:
      labels:
        some-label: some-label-value
    spec:
      containers:
      - name: git
        image: bravissimolabs/alpine-git
        command:
        - cat
        tty: true
             """
        }
    }

    stages {

        stage('Build') {
            steps {
                container('git') {
                    //the ssh private key must be configured as secret text in jenkins credentials store
                    git branch: 'master',
                            credentialsId: 'ssh-key-git',
                            url: 'git@github.com:pipeline-demo-caternberg/example-maven-api-k8s.git'
                    withCredentials([sshUserPrivateKey(credentialsId: 'ssh-key-git', keyFileVariable: 'CERT')]) {
                        sh 'mkdir -p ~/.ssh && chmod 700 ~/.ssh &&  cp -prf $CERT ~/.ssh/id_rsa && chmod 600 ~/.ssh/id_rsa'
                        sh "git config --global user.email \"acaternberg@cloudbees.com\""
                        sh "git config --global user.name \"cccaternberg\""
                        sh "eval `ssh-agent -s`  && ssh-add ~/.ssh/id_rsa"
                        sh "ssh-keyscan -H github.com >> ~/.ssh/known_hosts"
                        sh "echo \" \" >> README.md"
                        sh "git add README.md"
                        sh "git commit -m \"add line\""
                        sh 'git push origin master'

                    }


                }
            }
        }
    }
}