run `gradle clean lib``to get  the GitHub libs and its depnedencies 
http://github-api.kohsuke.org/

1. Create your GH token at https://github.com/settings/tokens 
2. Create a SecretText credential with id 'githubaccesstoken' and add your GH token from step 1 
3. Create a OipelineJob with Jenkinsfile-Seed.groovy  
4. Start the job