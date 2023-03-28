pipeline {
    agent any

    tools {
        maven "M3"
    }

    stages {
        stage('Build') {
            steps {
                git branch: 'main', url: "${GIT_URL}"
                sh "mvn clean compile"
            }
        }

        stage('Package & tests') {
            steps {
                sh "mvn package"
            }
        }

		stage('Deploy to FTP') {
            steps {
                sh "curl -T target/*.jar '${FTP_URL}' --user ${FTP_USER}:${FTP_PASSWORD}"
            }
        }
    }
}