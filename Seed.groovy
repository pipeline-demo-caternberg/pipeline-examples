import org.kohsuke.github.*
import hudson.*


//def createPipelineJobs(String ghAccessToken) {
//   println("createPipelineJobs with ghAccessToken: ${ghAccessToken}")
// Redefine these variables for your installation
String folder = '.'                                               // folder to put your jobs into
String githubOrganization = 'pipeline-demo-caternberg'                     // github organization
String gitHubUrl = 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
String includes = '*'                                                // What branches to include
String excludes = ''                                                // What branches to exclude
String numToKeep = '5'
// Number of recent builds to keep. -1 for all of them
String daysToKeep = '10'


//GitHub github = GitHub.connectUsingPassword(githubLogin, githubPassword)
pribtln "TOKEN $GH_ACCESS_TOKEN"
GitHub github = GitHub.connectUsingOAuth(${env.GH_ACCESS_TOKEN})
rateLimitBefore = github.getRateLimit().remaining
echo "API requests before: ${rateLimitBefore}"

// you can say that using .each({ repo -> .... }) would make sense
// I would say that too.
// But Jenkins does not agree with us
// so @see: https://issues.jenkins-ci.org/browse/JENKINS-26481
GHOrganization ghOrganization = github.getOrganization(githubOrganization)
List repositories = ghOrganization.listRepositories(100).asList()

for (int i = 0; i < repositories.size(); i++) {
    GHRepository repo = (GHRepository) repositories.get(i)
    println repo.getName()
}

GHRepository ghRepository = ghOrganization.getRepository("pipeline-examples")
List<GHContent> ghContentList = ghRepository.getDirectoryContent(".")
for (ghContent in ghContentList) {
    if (ghContent.isFile() && ghContent.getName().startsWith("Jenkinsfle-")) {
        pipelineJob(ghContent.getName()) {
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
                    scriptPath(ghContent.getName())
                    lightweight(true)
                }
            }
        }
    }
}
//}

//return this