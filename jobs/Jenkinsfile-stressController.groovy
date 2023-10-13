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
                    echo "Tip run: watch kubectl top pod"
                    // Sleep for a specified duration, e.g., 1 hour.
                    sleep time: 1, unit: 'HOURS'
                    // Kill the 'yes' command after the duration.
                    sh '''
                        kill $(cat yes.pid)
                        rm -f yes.pid
                    '''
            }
        }
    }
}