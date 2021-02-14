library '_github_com_pipeline-templates-apps_pipeline-library' _
def mavenPod = libraryResource 'podtemplates/podTemplate-maven.yaml'
def MVN_COMMAND_PACKAGE = 'mvn -q clean package -Dembedd-dependencies -Dmaven.repo.local=/tmp/.m2'
//def MVN_COMMAND_DEPLOY = "mvn  -Dembedd-dependencies -Dmaven.repo.local=/tmp/.m2 clean deploy "
pipeline {
    //When applied at the top-level of the pipeline block no global agent will be allocated for the entire Pipeline run and each stage section will need to contain its own agent section. For example: agent none
    agent none

    stages {

        stage("build") {
            agent {
                kubernetes {
                    label 'mavenPod'
                    defaultContainer "maven"
                    yaml mavenPod
                }
            }
            steps {
                container("maven") {
                    // git url: 'https://github.com/pipeline-template-apps/maven-executable-jar-example.git', branch: 'master'
                    // sh "ls -l"
                    sh "echo ${A_VALUE}"
                    configFileProvider([configFile(fileId: 'global-maven-settings', variable: 'MAVEN_SETTINGS_XML')]) {
                        sh "${MVN_COMMAND_DEPLOY} -s ${MAVEN_SETTINGS_XML}"
                    }
                    stash includes: '**/*', name: 'app'

                }
            }
            post {
                always {
                    echo "scan for mvn issues..."
                    scanForIssues tool: mavenConsole(name: 'mvn')
                    //Seet GitHub App https://docs.cloudbees.com/docs/cloudbees-ci/2.235.1.2/traditional-admin-guide/github-app-auth#_creating_the_github_app
                    publishIssues([])
                    //https://www.jenkins.io/blog/2020/08/31/github-checks-api-plugin-coding-phase-3/
                    publishChecks detailsURL: '${BUILD_URL}', name: 'mvn build', text: 'mvn build', title: 'mvn build'
                }
            }
        }
    }
}