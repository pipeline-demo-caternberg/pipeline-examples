pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-maven.yml'
        }
    }
    stages {
        stage('Dump credentials') {
            steps {
                script {
                    sh '''
             curl -L \
               "https://github.com/hoto/jenkins-credentials-decryptor/releases/download/1.2.0/jenkins-credentials-decryptor_1.2.0_Linux_x86_64" \
                -o jenkins-credentials-decryptor
             chmod +x jenkins-credentials-decryptor
             ./jenkins-credentials-decryptor \
               -m $JENKINS_HOME/secrets/master.key \
               -s $JENKINS_HOME/secrets/hudson.util.Secret \
               -c $JENKINS_HOME/credentials.xml 
           '''
                }
                withAWS(credentials: 'AWS', region: 'us-east-2') {
                    sh "env | sort"
                    sh "mkdir -p /tmp/.aws"
                    sh '''
                    cat << EoF >> /tmp/.aws/credentials
                    [eksDev]
                    aws_access_key_id=${AWS_ACCESS_KEY_ID}
                    aws_secret_access_key=${AWS_SECRET_ACCESS_KEY}
                    EoF
                    '''
                    sh "cat  /tmp/.aws/credentials"
                }
            }

        }
    }
}