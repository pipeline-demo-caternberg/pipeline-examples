pipeline {
  agent none
  stages {
    stage('Build') {
      agent any
      steps {
        echo "prep"
      }
    }
    stage('Select_Images') {
      agent none
      steps {
        script {
          IMAGES = input message: 'Deploy?', parameters: [extendedChoice(description: '', multiSelectDelimiter: ',', name: 'images', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_MULTI_SELECT', value: 'image1,image2,image3', visibleItemCount: 10)]
        }
      }
    }
    stage('Tag on Docker Hub') {
      agent any   
      steps {
        echo "${IMAGES}"
      }
    }
  }
}
