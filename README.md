This repo contains sample Jenkins Pipelines, Shared Libraries and Pipeline Template catalogs.

Directories
* jobs: contains sample Pipelines
* templates: contains sample [Pipeline Template catalogs](https://docs.cloudbees.com/docs/admin-resources/latest/pipeline-templates-user-guide/)
* resources: contains sample Shared Library resources
* vars: contains sample Shared Library custom steps
* src: contains a sample Shared Library Helper class

Files:
* catalog.yaml: Pipeline Template catalog descriptor
* Jenkinsfile: generate all jobs from jobs dir by Jobs DSL (optional)

# Optional steps for generating the sample jobs (inside jobs dir) by JOB-DSL 
For CloudBees, Job DSL is not recommended because we are using [Configuration as Code/CasC](https://docs.cloudbees.com/docs/cloudbees-ci/latest/casc-controller/items)

1. Install the  [JobDSL](https://wiki.jenkins.io/display/JENKINS/Job+DSL+Plugin) Plugin

2. Create and run a new Pipeline job that points to `Jenkinsfile`  
   (http://github-api.kohsuke.org/ is used to scan the GH organization, see `resources/groovy/Seed.groovy` to adjust the values if required)

3. Jobs are generated in the pipeline-example folder


if you try to run the Jenkinsfiles standalone, you have to fix the path to the yamlFile of some `jobs/Jenkinsfile-*` in this area to your belongings
```
agent {
    kubernetes {
        yamlFile 'yaml/podTemplate.yml'
    }
}
```



## Pre-requirerments: Set up credentials

Setup the following credentials:  (used by some pipelines)

* githubuserssh =GH User and SSH key (Type SSH user and private key)
* githubaccesstoken=GH Access token (Type secret text)
* as well as the dockerhub  credentials for the kaniko  docker build/push job: (see  jobs/Jenkinsfile-docker-build-kaniko.groovy)
for how to add the k8s secret for dockerhub. *Note#: GCR is not implemented yet, docker hub is used in the example pipeline  See instructions below to set up 

## A simple-docker-kaniko-pipeline-example
A simple Dockerfile to build with kaniko
see https://docs.cloudbees.com/docs/cloudbees-core/latest/cloud-admin-guide/using-kaniko#_create_a_new_kubernetes_secret   for further details
### Configure

### rename kubctl-create-secret.sh.default
```
cp -f scripts/kubctl-create-secret.sh.default kubctl-create-secret.sh
```
#### adjust your docker registry values
NORTE: Special characters in password must escape!
```
kubectl create secret docker-registry docker-credentials \
    --docker-username=><USER>  \
    --docker-password=<PASSWORD> \
    --docker-email=<EMAIL>
```
#### create the scercet
```
./kubctl-create-secret.sh
```
#### docker push manually
```
docker login
sudo docker build -t caternberg/hellonode:1.1 .
docker push caternberg/hellonode:1.1
```

