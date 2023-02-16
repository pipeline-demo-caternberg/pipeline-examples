/**
 * See https://plugins.jenkins.io/ansible-tower/#plugin-content-pipeline-returns
 * https://www.jenkins.io/doc/pipeline/steps/ansible-tower/
 * Ansible Tower Plugin must be installed and configured : manage jenkins -> configure system -> ansible tower setup
 */
pipeline {
    agent any
    stages {
        stage('Tower') {
            steps {
                script {
                    results = ansibleTower(
                            credential: '<CREDENTIAL_ID>',
                            extraVars: '''{                        
                                  extra_vars: {                        
                                    KEY1: "VAL1",                        
                                    KEY2: "enabled",                        
                                    KEY3: "VAL3"                        
                                  }                        
                                }''',
                            inventory: 'localhost',
                            jobTemplate: '146',
                            jobType: 'run',
                            throwExceptionWhenFail: false,
                            towerCredentialsId: '<TOWER_CREDENTIAL>',
                            towerLogLevel: 'false',
                            towerServer: '<ANSIBLE_TOWER_NAME>' //references manage jenkins -> configure system -> ansible tower setup
                     )
                   // println(results.JOB_ID)
                }
            }
        }
    }
}