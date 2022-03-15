pipeline {
    agent any
    stages {
        stage('SeedDSL') {
            steps {
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
