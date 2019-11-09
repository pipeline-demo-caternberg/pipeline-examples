pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate.yml'
        }
    }
    stages {
        stage('init-stage') {
            steps {
                echo "This is the init stage"
            }
        }
        stage('run-parallel-branches') {
            steps {
                parallel(
                        a: {
                            container("curl"){
                                echo "This is branch a"
                            }
                        },
                        b: {
                            container("curl"){
                                echo "This is branch b"
                            }
                        }
                )
            }
        }
        stage('last-stage') {
            steps {
                echo "This is the init stage"
            }
        }
    }
}