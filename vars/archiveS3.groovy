def call() {
        def eventCause = currentBuild.getBuildCauses("com.cloudbees.jenkins.plugins.pipeline.events.EventTriggerCause")
        echo "$eventCause"
        def url = eventCause[0].event.url
        def gitcommit =  eventCause[0].event.gitcommit
        def buildtag =  eventCause[0].event.buildtag
        echo "URL: $url"
       // def scriptS3 = libraryResource 'scripts/curlBuildLog.sh'
        //copy the logs and files to be archieved
        sh(script: """
        
                  mkdir -p TARFOLDER
                  cd TARFOLDER
                  curl   -u  admin:admin $url/consoleText -o build.log
                  curl -O   -u  admin:admin $url/archive/test.zip 
                  ls -ltr
                  cat build.log
                  #TODO: copy all wanted fies to here 
         """)
        //verify
        //TODO: add your verification impl

        //archive
        sh(script: """
                 cd $WORKSPACE
                 tar -cvzf archive-$buildtag-$gitcommit-\$(date +%s%3N).tar.gz TARFOLDER/*
               
         """)

        //upload to s6
        //TODO: implement s3 upload by aws-cli or rest
        echo "upload to S3 using aws cli "

}