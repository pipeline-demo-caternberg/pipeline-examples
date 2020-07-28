pipeline {
    agent none

    stages {
        stage("build and deploy on Windows and Linux") {
            parallel {
                stage("threadA") {
                    agent {
                        kubernetes {
                            yamlFile 'resources/yaml/podTemplate-maven.yml'
                        }
                    }
                    stages {
                        stage("build") {
                            steps {
                                sh "echo build threadA"
                            }
                        }
                        stage("deploy") {
                            steps {
                                sh "echo deploy threadA"
                            }
                        }
                    }
                }

                stage("threadB") {
                    agent {
                        kubernetes {
                            yamlFile 'resources/yaml/podTemplate-maven.yml'
                        }
                    }
                    stages {
                        stage("build") {
                            steps {
                                sh "echo build threadB"
                            }
                        }
                        stage("deploy") {
                            steps {
                                sh "echo deploy threadB"
                            }
                        }
                    }
                }
            }
        }
    }
}
