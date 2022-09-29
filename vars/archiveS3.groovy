def call() {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        echo "URL: $url"
       // def scriptS3 = libraryResource 'scripts/curlBuildLog.sh'
        //copy the logs and files to be archieved
        sh(script: """
        
                  mkdir -p TARFOLDER
                  cd TARFOLDER
                  curl -v  -u  admin:admin $url/consoleText -0
                  ls -ltr
                  #TODO: copy all wanted fies to here 
         """)
        //verify
        //TODO: add your verification impl

        //packacge
        sh(script: """
                 cd $WORKSPACE
                 #TODO get BUILD_TAG, GIT_COMMIT  from json event data payload
                tar -cvzf archive-\$(date +%s%3N).tar.gz TARFOLDER/*
               
         """)

        //upload to s6
        //
        sh "echo up0load to S3 using aws cli "

}