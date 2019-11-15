

def script
node('cloudbees-core') {
    script = load 'resources/groovy/crdeenetials-list.groovy'
    script.credentialsList()
}



