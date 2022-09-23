def call() {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        echo "URL: $url"
       // def scriptS3 = libraryResource 'scripts/curlBuildLog.sh'
        def curlPod = libraryResource 'podtemplates/podTemplate-curl.yaml'
        def label = "curl"
        podTemplate(name: 'mycurl', label: label, yaml: curlPod) {
                node(label) {
                        sh(script: """
                                    curl -v  -u  admin:admin $url/consoleText
                                  """)
                }
        }
}