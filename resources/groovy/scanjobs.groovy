
def scan() {
Jenkins.instance.getAllItems(AbstractItem.class).sort().each {it ->
    println  it.fullName + " " + it.class.name ;
    try {
        if ( null != it.lastBuild && null != it.lastBuild.timestamp )
        {
            println it.lastBuild.timestamp
        }
        if ( null !=  it.class && ! it.class.name.equals("org.jenkinsci.plugins.workflow.job.WorkflowJob") )
        {
            println it.lastBuiltOn.timestamp
        }
        //Iknow, this is very ugly to catch NPE . , sry
    }catch (Exception e){
        println "Error catched: " +  e.message
    }

}


}

return this