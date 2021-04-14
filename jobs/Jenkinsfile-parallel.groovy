pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-tools-os.yml'
        }
    }
    stages {
        stage('init-stage') {
            steps {
                ansiColor('css') {
                    echo "This is the init stage"
                }
            }
        }
        stage('run-parallel') {
            steps {
                parallel(
                        a: {
                            container("curl") {
                                ansiColor('vga') {
                                    echo '\033[42m\033[97mThread A green background\033[0m'
                                }
                                //echo "This is branch a"
                            }
                        },
                        b: {
                            container("curl") {
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