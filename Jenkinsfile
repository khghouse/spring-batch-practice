pipeline {
    agent any

    environment {
        DOTENV_FILE = credentials('spring-batch-env-file')
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/khghouse/spring-batch-practice.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './gradlew clean build -Dspring.profiles.active=dev'
            }
        }

        stage('Build and Run with Docker Compose') {
            steps {
                script {
                    sh 'rm -f .env'
                    sh 'cp $DOTENV_FILE .env'
                }
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }
}