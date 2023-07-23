@Library("lib1") _
pipeline {
    agent any

stages{
        stage('lib use') {
            steps {
                    // lib use
                    sayHello(name:"Shihab",dayOfWeek:"Saturday")
                }
            }
        }
}

