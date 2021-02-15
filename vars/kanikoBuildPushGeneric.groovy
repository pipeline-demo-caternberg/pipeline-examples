// vars/kanikoBuildPush.groovy
def call(String imageName, String imageTag = env.BUILD_NUMBER, String gcpProject = "core-workshop", String target = ".", String dockerFile="Dockerfile.multistagebuild-kaniko-example", Closure body) {
  def dockerReg = "gcr.io/${gcpProject}"
  def label = "kaniko-${UUID.randomUUID().toString()}"
  def podYaml = libraryResource 'podtemplates/dockerBuildPush.yml'
  podTemplate(name: 'kaniko', label: label, yaml: podYaml, nodeSelector: 'type=agent') {
    node(label) {
      body()
      imageNameTag()
      gitShortCommit()
      container(name: 'kaniko', shell: '/busybox/sh') {
        withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
          sh """#!/busybox/sh
            /kaniko/executor -f ${pwd()}/${dockerFile} -c ${pwd()} --build-arg buildNumber=${BUILD_NUMBER} --build-arg shortCommit=${env.SHORT_COMMIT} --build-arg commitAuthor=${env.COMMIT_AUTHOR} -d ${dockerReg}/${imageName}:${imageTag}
          """
        }
      }
    }
  }
}
