def call() {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        echo "URL: $url"
        sh "curl -v  -u  admin:admin   $url/consoleText "
        sh """
             curl -v  -u  admin:admin   $url/consoleText
          """

}