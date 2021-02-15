def call(Map pipelineParams) {
    // evaluate the body block, and collect configuration into the object
    //def pipelineParams= [:]
   // body.resolveStrategy = Closure.DELEGATE_FIRST
   // body.delegate = pipelineParams
    //body()
node ("docker"){

        def app

        stage('Clone repository') {
            echo "checkout from ${pipelineParams.branch}"
            echo "SIZE:" + pipelineParams.lenght
            checkout scm
        }

        stage('Build image') {
            /* This builds the actual image; synonymous to
             * docker build on the command line */

            app = docker.build("caternberg/hellonode")
        }

        stage('Test image') {
            app.inside {
                sh 'echo "Tests passed"'
            }
        }


        stage('Push image') {
            /* Finally, we'll push the image with two tags:
             * First, the incremental build number from Jenkins
             * Second, the 'latest' tag.
             * Pushing multiple tags is cheap, as all the layers are reused. */
            docker.withRegistry('https://registry.hub.docker.com', 'docker-credentials') {
                app.push("${env.BUILD_NUMBER}")
                app.push("latest")
            }
        }
    }

}
