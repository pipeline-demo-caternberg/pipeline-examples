import org.kohsuke.github.*
import hudson.*
import org.apache.commons.io.FilenameUtils;



//def createPipelineJobs(String ghAccessToken) {
//   println("createPipelineJobs with ghAccessToken: ${ghAccessToken}")
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




folder("${genFolder}") {
    description("Folder containing all generated pipelones from $gitHubUrl")
}

GHRepository ghRepository = ghOrganization.getRepository("pipeline-examples")
List<GHContent> ghContentList = ghRepository.getDirectoryContent("/jobs")
//List<GHContent> ghContentList = ghRepository.getFileContent '/jobs/'
for (ghContent in ghContentList) {
    println(ghContent.name)
    if (ghContent.isFile() && ghContent.getName().startsWith("Jenkinsfile-")) {
        println("generate ${ghContent.name}" )
        String fileNameWithOutExt = FilenameUtils.removeExtension(ghContent.name)
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
                    scriptPath(ghContent.getName())
                    lightweight(true)
                }
            }
        }
    }
}