// Build a Maven project using the standard image and Scripted syntax.
// Rather than inline YAML, you could use: yaml: readTrusted('jenkins-pod.yaml')
// Or, to avoid YAML: containers: [containerTemplate(name: 'maven', image: 'maven:3.6.3-jdk-8', command: 'sleep', args: 'infinity')]
podTemplate(yaml: '''
apiVersion: v1
kind: Pod
metadata:
  name: test-dind
spec:
  containers:
  - name: dind
    image: benhall/dind-jenkins-agent
    command:
    - sleep
    args:
    - infinity
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: myvolume
  volumes:
  - name: myvolume
    hostPath:
      path: /var/run/docker.sock
      type: FileOrCreate    
''') {
    node(POD_LABEL) {
        container('dind') {
          git 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
            sh 'docker build -t caternberg/dindtest -f $(pwd)/resources/dockerfiles/Dockerfile-custom-jnlp-agent  .'
        }      
    }
}
