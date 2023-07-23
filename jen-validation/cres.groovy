pipeline {
    agent any

        tools {
            maven 'mvn3'
            jdk 'jdk17'
        }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'br1', credentialsId: 'git-credential', url: 'https://github.com/mdshihabuddin10/pro-spring1'
            }
        }
        stage('Example Stage') {
            steps {
                withCredentials([string(credentialsId: 'git_token', variable: 'var1')]) {
                     script {
                  def name=var1
                }
                // some block
                    
                }
            }
        }
            
                  stage('second stage') {
            steps {
                script {
                    sh 'echo $name'
                }
            }
        }
        }
    }
