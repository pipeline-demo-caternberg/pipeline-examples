import com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition


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
pipeline {
    agent any
    stages {
        stage("input") {
            steps {


            }

        }

    }
}
