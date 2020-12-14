pipeline {
    agent {
        kubernetes {
            yamlFile 'resources/yaml/podTemplate-customagent.yml'
        }
    }
    parameters {
        string(name: 'kubectl_command', defaultValue: 'cluster-info',
                description: 'put a kubectl command')
    }
    stages {
        stage('Say Hello') {
            steps {
                container('custom-agent') {
                    echo 'Hello World!'
                    withKubeConfig(caCertificate: '''-----BEGIN CERTIFICATE-----
MIIDDDCCAfSgAwIBAgIRANowLMC5X57ejgRWp7BbQcAwDQYJKoZIhvcNAQELBQAw
LzEtMCsGA1UEAxMkZjk2YmUxZWEtZmJiYS00NzdhLWI5NjktMGUyYWZkZjViMGY1
MB4XDTIwMDMwMzEzMjA1NVoXDTI1MDMwMjE0MjA1NVowLzEtMCsGA1UEAxMkZjk2
YmUxZWEtZmJiYS00NzdhLWI5NjktMGUyYWZkZjViMGY1MIIBIjANBgkqhkiG9w0B
AQEFAAOCAQ8AMIIBCgKCAQEAwRj4zUngC2pfv2NIg/5e97b4VCpJhWwAMWURaxpu
oLhhz+cF2RIJQgeE6Bojgli69RJ4cRnIFXfFdetIeFRuxf76vzveYuQO174ALHPk
RLZLCsUEJNUvslWtklEazhsbxUlY1dtiz00C+jvxYDZL5R0yy87Q6DDuR7HPVaGh
9fyG9UNn81LnmJlRanR27ve7xWIn4FxBvc/ENcXXKZgltWQ6vOkqryWV/70reajz
A2ZW+/d4+P0eKMUEcPbR2hMH8cEGzpkeuTj58ClEKMxse2r2mcy50F/2IuvC1qV3
cLIMDu0VLx/EJJQkqE38ADa44LA3GSYTtkWrQJHnJiqNTwIDAQABoyMwITAOBgNV
HQ8BAf8EBAMCAgQwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEA
m9qkoGvhl+pegpYnwCfDXJP+DXdaSFNy9xwPBaGc4n6nsFhG1jsh6J+ia9/eKIRi
lNHlHjorsQkUDIWHapBBDXIMZe/fDuSToi/Ou7C535YZmkVfq+VxEBDxlKp4BeFZ
NstmwtVKGnTx1MlGMpoYFYtpJjlGMLmlmd7MRU3+n5v9S7hAkY8o6fh17qf0SOZ5
sWr6nsqo76/HROmw51QkJVQTF+xoeICzMilRFCt9yfmdzN11MXHlIWqWhCZFm9Qk
bHyaHE9+so4SMKuVPwe196AGTMowk3WoXZzLMUUwKUunBAclqqoNwKJ6oEe/bDNU
x/J0bqmAhM9cv+kEPIWPEA==
-----END CERTIFICATE-----''', clusterName: '', contextName: '', credentialsId: 'BearerToken', namespace: 'cloudbees-core', serverUrl: 'https://35.196.164.234/') {
                        // some block

                         sh "kubectl version"
                        sh "kubectl ${params.kubectl_command}"
                    }
                }
            }
        }
    }
}
