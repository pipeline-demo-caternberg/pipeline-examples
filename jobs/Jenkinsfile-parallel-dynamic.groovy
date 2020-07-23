node() {
    stage("prepare") {
        ["1", "2", "3"].each {
            println "Item: $it"
            // Write a text file there.
            dir("output") {
                writeFile file: "${it}-test.txt", text: "$it", encoding: "UTF-8"

            }
        }
        stash name: "myTestFiles", includes: "output/*.*"
    }

    stage('run-parallel') {
        unstash "myTestFiles"
        def files = findFiles glob: '**/*-test.txt'
        files.each { file -> println file }
        def parallelBranches = files.collectEntries { n ->
            [(n): {
                node('cloudbees-core') {
                    sh "sleep 10"
                    echo "Done"
                }
            }]
        }

        parallel parallelBranches

    }
}