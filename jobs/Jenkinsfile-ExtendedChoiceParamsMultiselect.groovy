import com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition

//see https://support.cloudbees.com/hc/en-us/articles/115003891391-Pipeline-Snippet-Generator-return-the-name-of-the-object-instead-of-the-object-with-the-parameters
//see https://support.cloudbees.com/hc/en-us/articles/115003895271-How-to-do-a-multiselect-input-in-a-pipeline
def checkBox(String name, String values, String defaultValue, String propertyFile, String propertyKey,
             int visibleItemCnt = 0, String description = '', String delimiter = ',') {

    // default same as number of values
    visibleItemCnt = visibleItemCnt ?: values.split(',').size()
    return new ExtendedChoiceParameterDefinition(
            name, //name,
            "PT_CHECKBOX", //type
            values, //value
            "", //projectName
            propertyFile, //propertyFile
            "", //groovyScript
            "", //groovyScriptFile
            "", //bindings
            "", //groovyClasspath
            propertyKey, //propertyKey
            defaultValue, //defaultValue
            "", //defaultPropertyFile
            "", //defaultGroovyScript
            "", //defaultGroovyScriptFile
            "", //defaultBindings
            "", //defaultGroovyClasspath
            "", //defaultPropertyKey
            "", //descriptionPropertyValue
            "", //descriptionPropertyFile
            "", //descriptionGroovyScript
            "", //descriptionGroovyScriptFile
            "", //descriptionBindings
            "", //descriptionGroovyClasspath
            "", //descriptionPropertyKey
            "", //javascriptFile
            "", //javascript
            false, //saveJSONParameterToFile
            false, //quoteValue
            visibleItemCnt, //visibleItemCount
            description, //description
            delimiter //multiSelectDelimiter
    )
}

def testParam = checkBox("images", // name
        "", // values
        "", //default value
        "resources/properties/extended_choice_params.properties",//Absolute Path or Network URL to propertyFile, http://..../property.properties
        "images",//propertyKey
        10, //visible item cnt
        "Multi-select", // description
)

properties(
        [parameters([testParam])]
)
node {
    echo "${params.images}"
}
pipeline {
    agent none
    stages {
        stage("params") {
            steps {
                echo "${params.images}"
            }

        }
    }

}