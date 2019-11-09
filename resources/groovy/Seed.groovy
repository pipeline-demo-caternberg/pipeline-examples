import org.kohsuke.github.*
import hudson.*
import org.apache.commons.io.FilenameUtils;

// println("createPipelineJobs with ghAccessToken: ${ghAccessToken}")
// Redefine these variables for your installation
String genFolder = 'pipeline-examples-gen'                                               // folder to put your jobs into
String githubOrganization = 'pipeline-demo-caternberg'                     // github organization
String gitHubUrl = 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
String includes = '*'                                                // What branches to include
String excludes = ''                                                // What branches to exclude
String numToKeep = '5'
// Number of recent builds to keep. -1 for all of them
String daysToKeep = '10'


//GitHub github = GitHub.connectUsingPassword(githubLogin, githubPassword)
println "TOKEN ${credentials}"
GitHub github = GitHub.connectUsingOAuth("${credentials}")
rateLimitBefore = github.getRateLimit().remaining
println "API requests before: ${rateLimitBefore}"
GHOrganization ghOrganization = github.getOrganization(githubOrganization)

/**
 List repositories = ghOrganization.listRepositories(100).asList()

 for (int i = 0; i < repositories.size(); i++) {GHRepository repo = (GHRepository) repositories.get(i)
 println repo.getName()}*/


/**
 * creates a gen folder
 *
 */
folder("${genFolder}") {
    description("Folder containing all generated pipelones from $gitHubUrl")
}

/**
 *
 * creates a pipelinejob for each Jenjinsfile-*
 */

GHRepository ghRepository = ghOrganization.getRepository("pipeline-examples")
List<GHContent> ghContentList = ghRepository.getDirectoryContent("jobs")
for (ghContent in ghContentList) {
    if (ghContent.isFile() && ghContent.getName().startsWith("Jenkinsfile")) {
        String fileNameWithOutExt = FilenameUtils.removeExtension(ghContent.name)
        println("generate ${fileNameWithOutExt}")
        pipelineJob("${genFolder}/${fileNameWithOutExt}") {
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
                    scriptPath("jobs/" + ghContent.getName())
                    lightweight(true)
                }
            }
        }
    }
}
