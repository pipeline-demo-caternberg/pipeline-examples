#! /bin/bash
#see https://github.com/jenkinsci/kubernetes-cli-plugin#generating-kubernetes-credentials
NS=cloudbees-core
# Create a ServiceAccount named `jenkins-robot` in a given namespace.
kubectl -n $NS delete serviceaccount jenkins-robot
kubectl -n $NS create serviceaccount jenkins-robot

# The next line gives `jenkins-robot` administator permissions for this namespace.
# * You can make it an admin over all namespaces by creating a `ClusterRoleBinding` instead of a `RoleBinding`.
# * You can also give it different permissions by binding it to a different `(Cluster)Role`.
kubectl -n $NS  delete rolebinding jenkins-robot-binding
kubectl -n $NS  create rolebinding jenkins-robot-binding --clusterrole=cluster-admin --serviceaccount=$NS:jenkins-robot

# Get the name of the token that was automatically generated for the ServiceAccount `jenkins-robot`.
TOKEN=$(kubectl -n $NS  get serviceaccount jenkins-robot -o go-template --template='{{range .secrets}}{{.name}}{{"\n"}}{{end}}')
echo $TOKEN
#jenkins-robot-token-d6d8z

# Retrieve the token and decode it using base64.
kubectl -n $NS  get secrets $TOKEN -o go-template --template '{{index .data "token"}}' | base64 -d
#eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2V[...]

#serviceaccount "jenkins-robot" deleted
#serviceaccount/jenkins-robot created
#rolebinding.rbac.authorization.k8s.io "jenkins-robot-binding" deleted
#rolebinding.rbac.authorization.k8s.io/jenkins-robot-binding created
#jenkins-robot-token-xmvw2
#eyJhbGciOiJSUzI1NiIsImtpZCI6InpfaFVNVHBDbDVuNGFNQzl3NHFvRVlNYnM3eHdEOFMtaERRVEJUTDIzWWMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJjbG91ZGJlZXMtY29yZSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqZW5raW5zLXJvYm90LXRva2VuLXhtdncyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImplbmtpbnMtcm9ib3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI1NjkyNDJlYy05YzllLTRlNGYtYjhmMy1hZTUzMDU3YjYzMmEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6Y2xvdWRiZWVzLWNvcmU6amVua2lucy1yb2JvdCJ9.ohnyBdbVbP1p5uwxdKf6Lsh5ReAI7uNgTg_5Oc0eHyAZKb60DHLczwAwZgX0Py4Q6FpqonWnKLADDTSTZ2K5YpxBhs25Bps-xTU6KmTB36jDemwuWOWvduTUnWiU-qossgJ9ZiTE7SUVpEv3FsJqXwL4sn8901Akk5yZCbh7rLjBt4eqW2bOv4EDVqd_Kvd3paqfF6X0Nao5q15uv5vCTFSot_mmXSFebnhlWPTVBq9WqaQPrfoH4V86OrqX-NtbL6h-0aU2OOfS5eMHry3Gv7bZPc-Zg7bRF-TT05HfifGr1gptlEjmmvio5HUBKlTK8-uy8BuXKSVKWI4wUj9VKg%
eyJhbGciOiJSUzI1NiIsImtpZCI6InpfaFVNVHBDbDVuNGFNQzl3NHFvRVlNYnM3eHdEOFMtaERRVEJUTDIzWWMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJjbG91ZGJlZXMtY29yZSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJqZW5raW5zLXJvYm90LXRva2VuLXhtdncyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImplbmtpbnMtcm9ib3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI1NjkyNDJlYy05YzllLTRlNGYtYjhmMy1hZTUzMDU3YjYzMmEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6Y2xvdWRiZWVzLWNvcmU6amVua2lucy1yb2JvdCJ9.ohnyBdbVbP1p5uwxdKf6Lsh5ReAI7uNgTg_5Oc0eHyAZKb60DHLczwAwZgX0Py4Q6FpqonWnKLADDTSTZ2K5YpxBhs25Bps-xTU6KmTB36jDemwuWOWvduTUnWiU-qossgJ9ZiTE7SUVpEv3FsJqXwL4sn8901Akk5yZCbh7rLjBt4eqW2bOv4EDVqd_Kvd3paqfF6X0Nao5q15uv5vCTFSot_mmXSFebnhlWPTVBq9WqaQPrfoH4V86OrqX-NtbL6h-0aU2OOfS5eMHry3Gv7bZPc-Zg7bRF-TT05HfifGr1gptlEjmmvio5HUBKlTK8-uy8BuXKSVKWI4wUj9VKg


kubectl create rolebinding jenkins-robot --clusterrole jenkins-robotr --serviceaccount cloudbees-core:jenkins
