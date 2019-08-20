import org.kohsuke.github.*

// Redefine these variables for your installation
String folder = '.'                                               // folder to put your jobs into
String githubLogin = 'acaternberg@cloudbees.com'                                   // github user login
String githubPassword = 'ab673d6cf65a305c092d61edcb7625e072ab4162'                      // github user personal access token
String githubOrganization = 'pipeline-demo-caternberg'                     // github organization
String gitHubUrl = 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
String scanCredentials = 'scan-github-access'                       // credentials for scanning repository
String checkoutCredentials = 'checkout-github-access'               // credentials for repository checkout
String includes = '*'                                                // What branches to include
String excludes = ''                                                // What branches to exclude
String numToKeep = '5'                                              // Number of recent builds to keep. -1 for all of them
String daysToKeep = '10'


GitHub github = GitHub.connectUsingPassword(githubLogin, githubPassword)
rateLimitBefore = github.getRateLimit().remaining
echo "API requests before: ${rateLimitBefore}"

// you can say that using .each({ repo -> .... }) would make sense
// I would say that too.
// But Jenkins does not agree with us
// so @see: https://issues.jenkins-ci.org/browse/JENKINS-26481
List repositories = github.getOrganization(githubOrganization).listRepositories(100).asList()

for (int i = 0; i < repositories.size(); i++) {

    println $i
}

pipelineJob('my-actual-pipeline') {
    definition {
        cpsScmFlowDefinition {
            scm {
                gitSCM {
                    userRemoteConfigs {
                        userRemoteConfig {
                            credentialsId('')
                            name('')
                            refspec('')
                            url("${gitHubUrl}")
                        }
                    }
                    branches {
                        branchSpec {
                            name('*/master')
                        }
                    }
                    browser {
                        gitWeb {
                            repoUrl('')
                        }
                    }
                    gitTool('')
                    doGenerateSubmoduleConfigurations(false)
                }
            }
            scriptPath('Jenkinsfile')
            lightweight(true)
        }
    }
}
