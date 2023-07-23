pipeline {
    agent any
    stages {
        stage('Vault') {
            steps {
                withVault(configuration: [timeout: 60, vaultCredentialId: 'vault1', vaultUrl: 'http://192.168.0.168:8200'], vaultSecrets: [[path: 'kv/hello', secretValues: [[isRequired: false, vaultKey: 'foo']]]]) {
                        // some block
                        sh'echo valure retrived '
                        sh 'echo "$foo" >> file.txt'
                }
            }
        }

            stage('Vault Next') {
                steps {
                    sh 'echo $foo'
                    sh 'echo "$foo" >> file.txt'
                    sh "cat file.txt"
                }
            }
                }
    }

