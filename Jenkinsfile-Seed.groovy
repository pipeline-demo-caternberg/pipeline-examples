pipeline {
    agent { node { label 'cloudbees-core' } }
    stages {
        stage('buildLib') {
            steps {
                container('gradle') {
                    script {
                        println System.getProperty("java.ext.dirs")
                    }
                    echo "Hello World"
                    sh 'gradle clean lib'
                    sh 'mkdir -p  ~/.groovy/grapes/  &&  cp -f lib/*.jar  ~/.groovy/grapes/'
                    // point to exact source file
                    withCredentials([string(credentialsId: 'githubaccesstoken', variable: 'GH_ACCESS_TOKEN')]) {
                       script {
                           apiKey = "\nAPI key: ${GH_ACCESS_TOKEN}\n"
                           println apiKey
                           def rootDir = pwd()
                           def seed = load "${rootDir}/Seed.groovy"
                           seed.createPipelineJobs(${GH_ACCESS_TOKEN})
                        }
                    }
                    println apiKey
                }
            }
        }
        stage('SetClasspath') {
            steps {
                echo "addToClasspath"
                // sh 'addToClasspath lib/*.jar'
            }
        }
    }
}
