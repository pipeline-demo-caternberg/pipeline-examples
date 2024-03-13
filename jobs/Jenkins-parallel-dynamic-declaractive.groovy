
// Example function that would return an array of regions to deploy
def getRegionsToDeploy() {
  return ["One","Two","Three"]
}

// Convert array of regions to map of stages
def getDeployStages() {
  return getRegionsToDeploy().collectEntries { region ->
    [
      (region): {
        stage("Deploy region ${region}") {
          echo  "sample command ${region}"
        }
      }
    ]
  }
}

pipeline {
  agent any
  stages {
    // other stages...
    stage('Deploy') {
      steps {
        script {
          // Directly pass the map of stages to `parallel`
          parallel getDeployStages()
        }
      }
    }
  }
}
