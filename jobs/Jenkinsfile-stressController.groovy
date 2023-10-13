//To simulate some stress on the controller withgout using sress-ng
//The Controller requires to have at least 1 executor assigend
pipeline {
    agent any
    stages {
        stage('Generate Load') {
            steps {
                // Start the 'yes' command in the background to generate load.
                sh '''
                        yes > /dev/null &
                        echo $! > yes.pid
                    '''
                // stress cpu see https://www.baeldung.com/linux/cpu-spike-bash
                sh '''
                      dd if=/dev/zero of=/dev/null |
                      dd if=/dev/zero of=/dev/null |
                      dd if=/dev/zero of=/dev/null |
                      dd if=/dev/zero of=/dev/null &
                    '''
                echo "Tip run now: watch kubectl top pod"
                // Sleep for a specified duration, e.g., 1 hour.
                //sleep time: 1, unit: 'HOURS'
                input id: 'Input', message: 'abort', ok: 'continue'
                sh '''
                        kill $(cat yes.pid)
                        rm -f yes.pid
                        killall dd
                    '''
            }
        }
    }
}