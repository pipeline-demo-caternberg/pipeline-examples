// Build a Maven project using the standard image and Scripted syntax.
// Rather than inline YAML, you could use: yaml: readTrusted('jenkins-pod.yaml')
// Or, to avoid YAML: containers: [containerTemplate(name: 'maven', image: 'maven:3.6.3-jdk-8', command: 'sleep', args: 'infinity')]
podTemplate(yaml: '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: dind
    image: benhall/dind-jenkins-agent
    command:
    - sleep
    args:
    - infinity
''') {
    node(POD_LABEL) {
        container('dind') {
            sh 'docker build -t caternberg/dindtest -f resources/dockerfiles/Dockerfile-simple .'
        }      
    }
}
