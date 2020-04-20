#! /bin/groovy

import jenkins.model.*
import com.cloudbees.hudson.plugins.folder.*;
import com.cloudbees.hudson.plugins.folder.properties.*;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

def credentialsList() {
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
            com.cloudbees.plugins.credentials.common.StandardUsernameCredentials.class,
            Jenkins.instance,
            null,
            null
    );
    for (c in creds) {
        println( ( c.properties.privateKeySource ? "ID: " + c.id + ", UserName: " + c.username + ", Private Key: " + c.getPrivateKey() : ""))
    }
    for (c in creds) {
        println( ( c.properties.password ? "ID: " + c.id + ", UserName: " + c.username + ", Password: " + c.password : ""))
    }


}

return this
