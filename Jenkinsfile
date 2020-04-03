pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-gradle.yml'
        }
    }
    stages {
        stage('buildLib') {
            steps {
                container('gradle') {
                    withCredentials([string(credentialsId: 'githubaccesstoken', variable: 'GH_ACCESS_TOKEN')]) {
                        echo sh(script: 'env|sort', returnStdout: true)
                        jobDsl targets: ['resources/groovy/Seed.groovy'].join('\n'),
                                removedJobAction: 'DELETE',
                                removedViewAction: 'DELETE',
                                lookupStrategy: 'SEED_JOB',
                                additionalParameters: [credentials: "${GH_ACCESS_TOKEN}"]
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

                }
            }
        }
    }
}
