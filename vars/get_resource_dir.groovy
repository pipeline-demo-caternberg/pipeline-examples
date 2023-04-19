// vars/get_resource_dir.groovy
//see https://itecnote.com/tecnote/jenkins-how-to-load-files-from-resources-folder-in-shared-library-without-knowing-their-names-or-number/
/*
Using the path you can invoke your scripts as usual:
sh "${get_resource_dir()}/com/example/test.sh"
 */

import groovy.transform.SourceURI
import java.nio.file.Path
import java.nio.file.Paths

class ScriptSourceUri {
    @SourceURI
    static URI uri
}

def call() {
    Path scriptLocation = Paths.get(ScriptSourceUri.uri)
    return scriptLocation.getParent().getParent().resolve('resources').toString()
}