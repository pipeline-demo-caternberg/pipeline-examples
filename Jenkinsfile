pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-gradle.yml'
        }
    }
    stages {
        stage('SeedDSL') {
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
                }
            }
        }
    }
}
