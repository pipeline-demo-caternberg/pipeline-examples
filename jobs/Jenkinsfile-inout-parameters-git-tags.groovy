pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-tools-os.yml'
        }
    }
    stages {
        stage('My Stage') {
            steps {
                script {
                    def GIT_TAGS = sh (script: 'git tag -l', returnStdout:true).trim()
                    inputResult = input(
                        message: "Select a git tag",
                        parameters: [choice(name: "git_tag", choices: "${GIT_TAGS}", description: "Git tag")]
                    )
                }
            }
        }
        stage('My other Stage'){
            steps{
                echo "The selected tag is: ${inputResult}"
            }
        }
    }
}
