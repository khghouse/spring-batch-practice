pipeline {
    agent any

    environment {
        ENV_FILE = credentials('spring-batch-env')
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
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }
}