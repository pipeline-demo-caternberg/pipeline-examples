#! /bin/bash


SERVICE_ACCOUNT=jenkins #DEFAULT BY CLOUDBEES
NAMESPACE=cloudbees-masters #$1

# Get the ServiceAccount's token Secret's name
export SECRET=$(kubectl -n $NAMESPACE get serviceaccount ${SERVICE_ACCOUNT} -o json | jq -Mr '.secrets[].name | select(contains("token"))')
#echo $SECRET
# Extract the Bearer token from the Secret and decode
export TOKEN=$(kubectl -n $NAMESPACE get secret ${SECRET} -o json | jq -Mr '.data.token' | base64 -d)
echo "Bearer $TOKEN"

# Extract, decode and write the ca.crt to a temporary location
kubectl -n $NAMESPACE get secret ${SECRET} -o json | jq -Mr '.data["ca.crt"]' | base64 -d > /tmp/ca.crt
# Get the API Server location
APISERVER=https://$(kubectl -n default get endpoints kubernetes --no-headers | awk '{ print $2 }')
echo "APISERVER: $APISERVER"

#curl -s $APISERVER/openapi/v2  --header "Authorization: Bearer $TOKEN" --cacert /tmp/ca.crt | less
#curl -s https://35.196.164.234/api/v1/namespaces/cloudbees-core/pods --header "Authorization: Bearer $TOKEN" --cacert /tmp/ca.crt | less
curl -s $APISERVER/api/v1/namespaces/$NAMESPACE/pods --header "Authorization: Bearer $TOKEN" --cacert /tmp/ca.crt | less