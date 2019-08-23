import org.kohsuke.github.*
import hudson.*


def createPipelineJobs() {
    println("createPipelineJobs")
// Redefine these variables for your installation
    String folder = '.'                                               // folder to put your jobs into
    String githubLogin = 'cccaternberg'                                   // github user login
    String githubPassword = '3b2ffac6accd9cea58a7a347c1e15a4ee68e7ded'
    // github user personal access token
    String githubOrganization = 'pipeline-demo-caternberg'                     // github organization
    String gitHubUrl = 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
    String scanCredentials = '12193af6ef620119665a8c10c6394dd4e2ee4b56'
    // credentials for scanning repository
    String checkoutCredentials = 'checkout-github-access'               // credentials for repository checkout
    String includes = '*'                                                // What branches to include
    String excludes = ''                                                // What branches to exclude
    String numToKeep = '5'
    // Number of recent builds to keep. -1 for all of them
    String daysToKeep = '10'


//GitHub github = GitHub.connectUsingPassword(githubLogin, githubPassword)
    GitHub github = GitHub.connectUsingOAuth(githubPassword)
    rateLimitBefore = github.getRateLimit().remaining
    echo "API requests before: ${rateLimitBefore}"

// you can say that using .each({ repo -> .... }) would make sense
// I would say that too.
// But Jenkins does not agree with us
// so @see: https://issues.jenkins-ci.org/browse/JENKINS-26481
    List repositories = github.getOrganization(githubOrganization).listRepositories(100).asList()

    for (int i = 0; i < repositories.size(); i++) {

        println i
    }

    pipelineJob('Pipeline') {
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
                // scriptPath('Jenkinsfile')
                lightweight(true)
            }
        }
    }

}

return this