pipeline {
    agent any
        tools {
            maven 'mvn3'
            jdk 'jdk11'
        }

    environment {
                DOCKER_REGISTRY = 'https://index.docker.io/v1/'
                DOCKER_IMAGE_NAME = 'mdshihabuddin/locallibrary'
                DOCKER_IMAGE_TAG = '1.0'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'br1', credentialsId: 'git-credential', url: 'https://github.com/mdshihabuddin10/pro-spring1'
            }
        }
        stage("Maven Build"){
            steps{
                sh "mvn clean package"
                sh "mv target/*.jar target/app.jar"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                        def dockerImage = docker.build(imageTag, '-f Dockerfile .')
                        stash name: 'dockerImage', includes: 'dockerImage'
                }
            }
        }

        stage('Trivy Scan') {
            steps {
                script {
                    sh "/home/shi/tools/trivy1/trivy ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                        withDockerRegistry(credentialsId: 'docker-cred') {
                            unstash 'dockerImage'
                            dockerImage.push(DOCKER_IMAGE_TAG)
                        }
                }
            }
        }

        stage('Deploy stage') {
            steps {
                script {
                        sh "sed -i 's/TAG/${DOCKER_IMAGE_TAG}/g' app-deployment.yml"
                        sh '/usr/local/bin/kubectl apply -f app-deployment.yml'
                        sh '/usr/local/bin/kubectl apply -f app-svc.yml'
                }
            }
            }
        }
    }
