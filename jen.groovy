node {
    stage('SCM') {
        git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/mdshihabuddin10/locallib-with-compose'
    }
    stage('SonarQube Analysis') {
        def scannerHome = tool 'SonarScanner'
        withSonarQubeEnv() {
            sh "${scannerHome}/bin/sonar-scanner"
        }
    }
}
