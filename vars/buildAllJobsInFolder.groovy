def call() {
    // evaluate the body block, and collect configuration into the object
    //def pipelineParams= [:]
   // body.resolveStrategy = Closure.DELEGATE_FIRST
   // body.delegate = pipelineParams
    //body()
    def names = jobNames()
    for (i = 0; i < names.size(); i++) {
       // build job: names[i], wait: false
        echo job: names[i]
    }
}

@NonCPS
def jobNames() {
    def project = Jenkins.instance.getItemByFullName(currentBuild.fullProjectName)
    def childItems = project.parent.items
    def targets = []
    for (i = 0; i < childItems.size(); i++) {
        def childItem = childItems[i]
        if (!childItem instanceof AbstractProject) continue;
        if (childItem.fullName == project.fullName) continue;
        targets.add(childItem.fullName)
    }
    return targets
}