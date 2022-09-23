def call() {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        echo "URL: $url"
        echo "TEST TESTE"
        def scriptS3 = libraryResource 'scripts/curlBuildLog.sh'
       //echo "${scriptS3}"
        //echo "curl -v  -u  admin:admin   $url/consoleText"
        sh "pwd"
   /*     timeout(time: 10, unit: 'SECONDS') {
                sh "${scriptS3} ${url}"
        }
*/


}