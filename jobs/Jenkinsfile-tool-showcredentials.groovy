

def script
node('slave') {
    script = load 'resources/groovy/crdeenetials-list.groovy'
    script.credentialsList()
}



