pipeline {
    agent any



stages {
        stage('Checkout') {
            steps {
                git branch: 'br1', credentialsId: 'git-credential', url: 'https://github.com/mdshihabuddin10/pro-spring1'
            }
        }
        stage("AV Scan"){
            steps{
                    sh "/usr/bin/clamscan -irf ."
            }
        }

}
}