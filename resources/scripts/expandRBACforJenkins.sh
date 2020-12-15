#! /bin/bash
#see https://github.com/jenkinsci/kubernetes-cli-plugin#generating-kubernetes-credentials



NS=cloudbees-core

cat << EOF > /tmp/clusterRole.yml
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: jenkins-robot
rules:
  - apiGroups: [""]
    resources: ["*", "*"]
    verbs: ["get", "watch", "list"]
EOF
kubectl apply -f /tmp/clusterRole.yml


# Get the name of the token that was automatically generated for the ServiceAccount `jenkins-robot`.
TOKEN=$(kubectl -n $NS  get serviceaccount jenkins -o go-template --template='{{range .secrets}}{{.name}}{{"\n"}}{{end}}')
echo $TOKEN
#jenkins-robot-token-d6d8z

# Retrieve the token and decode it using base64.
kubectl -n $NS  get secrets $TOKEN -o go-template --template '{{index .data "token"}}' | base64 -d
#eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2V[...]



kubectl create cluserrolebinding jenkins-robot --clusterrole jenkins-robot --serviceaccount cloudbees-core:jenkins
