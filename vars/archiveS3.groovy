def call() {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        echo "URL: $url"
        echo "curl -v  -u  admin:admin   $url/consoleText"
        def log = sh(script: """
             curl -v  -u  admin:admin   $url/consoleText
          """, returnStdout: true)
        echo LOG: "$log"
}