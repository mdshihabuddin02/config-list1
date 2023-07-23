pipeline {
    agent any
        tools {
            maven 'mvn3'
            jdk 'jdk11'
        }
  stages {
            stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'sona1') {
                    // Configure the SonarQube analysis properties
                    script {
                        def scannerHome = tool 'SonarScanner'
                        //sh "${scannerHome}/bin/sonar-scanner"
                        sh "mvn clean verify sonar:sonar -Dsonar.projectKey=pro-spring1 -Dsonar.projectName='pro-spring1'"
                    }
                }
            }
            
}

  }
}