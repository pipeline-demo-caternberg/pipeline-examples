// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent {
        kubernetes {
            yaml '''
        ---
        apiVersion: v1
        kind: Pod
        metadata:
          name: test-agent-node-pool
          labels:
            node: test-node-agent-pool
        spec:
          nodeSelector:
            cloudbees.custom.agent: agent
          affinity:
            podAntiAffinity:
              preferredDuringSchedulingIgnoredDuringExecution:
                - podAffinityTerm:
                    labelSelector:
                      matchExpressions:
                        - key: "com.cloudbees.cje.type"
                          operator: "In"
                          values:
                            - "master"
                            - "cjoc"
                    topologyKey: "kubernetes.io/hostname"
                  weight: 1
            nodeAffinity:
              requiredDuringSchedulingIgnoredDuringExecution:
                nodeSelectorTerms:
                  - matchExpressions:
                      - key: cloudbees.custom.agent
                        operator: In
                        values:
                          - "agent"
          containers:
            - name: shell
              image: ubuntu
              command:
                - sleep
              args:
                - infinity

        '''
            defaultContainer 'shell'
            retries 2
        }
    }
    stages {
        stage('Main') {
            steps {
                sh 'hostname'
            }
        }
    }
}