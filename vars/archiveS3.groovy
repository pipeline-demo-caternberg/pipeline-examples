def call() {
    script {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        echo "URL: $url"
        def log = sh(script: """
             curl -v  -u  ${jenkinsUserAndToken}  --silent  $url/consoletext'
          """, returnStdout: true)
        echo LOG: "$log"
    }
}