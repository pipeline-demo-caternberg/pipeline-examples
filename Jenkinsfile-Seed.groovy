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
                    withCredentials([string(credentialsId: 'githubaccesstoken', variable: 'GH_ACCESS_TOKEN')]) {

                        jobDsl targets: ['Seed.groovy'].join('\n'),
                                removedJobAction: 'DELETE',
                                removedViewAction: 'DELETE',
                                lookupStrategy: 'SEED_JOB',
                                additionalClasspath: ['lib/*.jar'].join('\n'),
                                additionalParameters: [message: 'Hello from pipeline', credentials: 'githubaccesstoken']
                    }
                    // point to exact source file
                 /*
                    script {
                        withCredentials([string(credentialsId: 'githubaccesstoken', variable: 'GH_ACCESS_TOKEN')]) {
                            apiKey = "\nAPI key: ${GH_ACCESS_TOKEN}\n"
                            println apiKey
                            echo env.GH_ACCESS_TOKEN
                            def rootDir = pwd()
                            def seed = load "${rootDir}/Seed.groovy"
                            seed.createPipelineJobs(env.GH_ACCESS_TOKEN)
                        }
                    }
                    */
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
