pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-tools-os.yml'
        }
    }
    stages {
        stage('Build') {
            steps {
                container('git') {
                    //the ssh private key must be configured as secret text in jenkins credentials store
                    git branch: 'master',
                            credentialsId: 'githubuserssh',
                            url: 'git@github.com:pipeline-demo-caternberg/maven-executable-jar-example.git'
                    withCredentials([sshUserPrivateKey(credentialsId: 'githubuserssh', keyFileVariable: 'CERT')]) {
                        sh 'mkdir -p ~/.ssh && chmod 700 ~/.ssh &&  cp -prf $CERT ~/.ssh/id_rsa && chmod 600 ~/.ssh/id_rsa'
                        sh "git config --global user.email \"user@cloudbees.com\""
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
