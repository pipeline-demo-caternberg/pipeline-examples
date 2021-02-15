// vars/kubeDeploy.groovy
def call(imageName, imageTag, githubCredentialId, repoOwner) {
    def label = "kubectl"
    def podYaml = libraryResource 'podtemplates/kubeDeploy.yml'
    def deployYaml = libraryResource 'k8s/basicDeploy.yml'
    def repoName = env.IMAGE_REPO.toLowerCase()
    def envStagingRepo = "environment_staging"
    
    podTemplate(name: 'kubectl', label: label, yaml: podYaml) {
      node(label) {
        //create environment repo for prod if it doesn't already exist
        echo githubCredentialId
        withCredentials([usernamePassword(credentialsId: githubCredentialId, usernameVariable: 'USERNAME', passwordVariable: 'ACCESS_TOKEN')]) {
          echo repoOwner
          echo envStagingRepo

        sh(script: """
            curl --silent -H "Authorization: token $ACCESS_TOKEN" --data '{"name":"${envStagingRepo}"}' https://api.github.com/orgs/${repoOwner}/repos
          """)
        }
        withCredentials([usernamePassword(credentialsId: githubCredentialId, usernameVariable: 'USERNAME', passwordVariable: 'ACCESS_TOKEN')]) {
          sh """
            git init
            git config user.email "deployBot@cb-sa.io"
            git config user.name "${USERNAME}"
            git remote add origin https://${USERNAME}:${ACCESS_TOKEN}@github.com/${repoOwner}/${envStagingRepo}.git
          """
          
          try {
            sh 'git pull origin master'
          } catch(e) {
            //nothing to do, just means remote hasn't been initialized yet
          }
          writeFile file: "deploy.yml", text: deployYaml

          sh("sed -i.bak 's#REPLACE_IMAGE_TAG#gcr.io/core-workshop/helloworld-nodejs:${repoName}-${BUILD_NUMBER}#' deploy.yml")
          sh("sed -i.bak 's#REPLACE_SERVICE_NAME#${repoName}#' deploy.yml")
          sh """
            git add *
            git commit -a -m 'updating ${envStagingRepo} deployment with version ${repoName}-${BUILD_NUMBER}'
            git push -u origin master
          """
        }
        container("kubectl") {
          sh "kubectl apply -f deploy.yml"
          sh "echo 'deployed to http://staging.cb-sa.io/${repoName}/'"
        }
      }
    }
}
