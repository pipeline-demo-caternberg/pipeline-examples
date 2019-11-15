#! /bin/groovy

import jenkins.model.*
import com.cloudbees.hudson.plugins.folder.*;
import com.cloudbees.hudson.plugins.folder.properties.*;
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

def credentialsAddToFolder(String folderName) {
    jenkins = Jenkins.instance
    String id = java.util.UUID.randomUUID().toString()
    Credentials c = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, id, "description:"+id, "user", "password")

    for (folder in jenkins.getAllItems(Folder.class)) {
        if(folder.name.equals("${folderName}") ){
            AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
            FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class)
            if(property) {
                println "Add credentials in global store"
                property.getStore().addCredentials(Domain.global(), c)
            } else {
                println "Initialize Folder Credentials store and add credentials in global store"
                property = new FolderCredentialsProperty([c])
                folderAbs.addProperty(property)
            }
            println property.getCredentials().toString()
        }
    }
}

return this