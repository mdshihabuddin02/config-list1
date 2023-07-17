pipeline {
    agent any

        tools {
            maven 'mvn3'
            jdk 'jdk17'
        }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/mdshihabuddin10/locallib-with-compose'
            }
        }
        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'sona1') {
                    // Configure the SonarQube analysis properties
                    script {
                        def scannerHome = tool 'sonar2'
                        sh "${scannerHome}/bin/sonar-scanner"
                    }
                }
            }
        }
    }
}
