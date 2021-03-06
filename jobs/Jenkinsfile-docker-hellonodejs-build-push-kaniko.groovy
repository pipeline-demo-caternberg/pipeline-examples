def label = "kaniko-${UUID.randomUUID().toString()}"
podTemplate(name: 'kaniko', label: label, yaml: """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: jenkins-docker-cfg
        mountPath: /kaniko/.docker
  volumes:
  - name: jenkins-docker-cfg
    projected:
      sources:
      - secret:
          name: docker-credentials
          items:
            - key: .dockerconfigjson
              path: config.json
""") {
    node(label) {
        stage('Build with Kaniko') {
            container(name: 'kaniko', shell: '/busybox/sh') {
                sh 'ls -lR'
                git 'https://github.com/pipeline-demo-caternberg/pipeline-examples.git'
                withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
                    sh '''#!/busybox/sh
                /kaniko/executor  --dockerfile $(pwd)/resources/dockerfiles/Dockerfile-kaniko-example --insecure --skip-tls-verify --cache=false  --context $(pwd) --destination caternberg/hellokaniko:BUILD_NUMBER-${BUILD_NUMBER}
            '''
                }
            }
        }
    }
}
