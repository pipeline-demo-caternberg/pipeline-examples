import com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition

node {
    def multiSelect = new ExtendedChoiceParameterDefinition("name",
            "PT_MULTI_SELECT",
            "blue,green,yellow,blue",
            "project name",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "blue,green,yellow,blue",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            false,
            3,
            "multiselect",
            ",")

    def userInput = input id: 'customID', message: 'Let\'s promote?', ok: 'Release!', parameters: [multiSelect]


    echo "Hello: " + userInput
}

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

