pipeline {
    agent any

    parameters {
        // Define an Active Choices Parameter
        activeChoice(
                name: 'GIT_BRANCH',
                description: 'Select Git branch',
                script: [
                        // Define Groovy script to fetch Git branch names dynamically
                        groovyScript: [
                                // Define the Groovy script to fetch Git branch names
                                classpath: [],
                                sandbox: false,
                                script: '''
                        // Import necessary classes
                        import hudson.model.*
                        import jenkins.model.*
                        import groovy.json.JsonSlurper

                        // Fetch Git branches dynamically
                        def gitBranches = []

                        // Get Jenkins instance
                        def jenkins = Jenkins.instance

                        // Get the project (assuming the pipeline is configured at the project level)
                        def project = jenkins.getItemByFullName(env.JOB_NAME)

                        // Get the Git SCM information from the project
                        def scm = project.scm
                        
                        // Check if the SCM is Git
                        if (scm instanceof hudson.plugins.git.GitSCM) {
                            // Iterate over the branches defined in the Git SCM configuration
                            scm.branches.each { branchSpec ->
                                // Extract branch name
                                def branchName = branchSpec.name
                                // Add branch name to the list
                                gitBranches.add(branchName)
                            }
                        }

                        return gitBranches
                    '''
                        ]
                ]
        )
    }

    stages {
        stage('Example Stage') {
            steps {
                // Example stage steps
                echo "Selected Git branch: ${params.GIT_BRANCH}"
            }
        }
    }
}
