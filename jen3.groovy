pipeline {
    agent any
    stages {
        stage('Vault') {
            steps {
                withVault(configuration: [timeout: 60, vaultCredentialId: 'vault1', vaultUrl: 'http://192.168.0.168:8200'], vaultSecrets: [[path: 'secret/app1', secretValues: [[isRequired: false, vaultKey: 'name']]]]) {
                    // some block
                    sh 'echo $name'
                    sh 'echo "$name" >> file.txt'
                    sh 'cat file.txt'
                    script {
                        // Define a variable
                        def myVariable,myVariabl2
                        def var1
                        var1 = name

                        // Execute a shell command and capture the output
                        myVariable = sh(returnStdout: true, script: 'echo "Hello, world!"').trim()
                        myVariable2 = sh(returnStdout: true, script: 'echo $var1').trim()
                        // Print the value of the variable
                        echo "Output: ${myVariable}"
                        echo "Output: ${myVariable2}"
                    }
                }
            }
        }

    stage('Example') {
      steps {
        sh '''
            cat file.txt
        '''
      }
    }
  }
}
 

