
def scan() {
    Jenkins.instance.getAllItems(AbstractItem.class).sort().each {it ->
        try {
            println it.lastBuild.timestamp.get(Calendar.YEAR)+"-"+(it.lastBuild.timestamp.get(Calendar.MONTH)+1)+"-"+it.lastBuild.timestamp.get(Calendar.DATE) + " " + it.fullName + " " + it.class.name;
            //I know, this is very ugly to catch NPE, sry
        }catch (Exception e){
            println "Error catched: " +  e.message
        }
    }
}

return this