pipeline {
    agent any
        tools {
            maven 'mvn3'
            jdk 'jdk11'
        }


    stages {
        stage('Checkout') {
            steps {
                git branch: 'br1', credentialsId: 'git-credential', url: 'https://github.com/mdshihabuddin10/pro-spring1'
            }
        }
        stage('Maven Build') {
            steps {
                script {

                sh "sed -i 's|MYCON|jdbc:mysql://clouddevdb.cjldgoxvvwoc.us-west-2.rds.amazonaws.com:3306/shihabdb?useSSL=true|g' src/main/resources/application.properties"
                sh "sed -i 's/MYDBUSER/dbshi/g' src/main/resources/application.properties"
                sh "sed -i 's/MYDBPASS/db@127/g' src/main/resources/application.properties"
                sh "cat src/main/resources/application.properties"
                sh 'mvn clean package'
                sh 'mv target/*.jar target/app.jar'
            }
            }
        }

        stage('Deploy stage') {
            steps {
                script {
                    sshagent(['aws1']) {
                        // some block
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@34.212.87.76 'sudo systemctl stop springapp.service'"
                        sh 'scp -o StrictHostKeyChecking=no target/app.jar ubuntu@34.212.87.76:/home/ubuntu/springapplication'
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@34.212.87.76 'sudo systemctl start springapp.service'"
                    }
                }
            }
        }
    }
}
