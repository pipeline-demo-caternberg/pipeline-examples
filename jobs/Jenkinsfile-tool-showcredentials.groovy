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