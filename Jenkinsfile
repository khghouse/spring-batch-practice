pipeline {
    agent any

    /**
    environment {
        DOTENV = credentials('spring-batch-env')
    }
   */

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
            withCredentials([file(credentialsId: 'spring-batch-env-file', variable: 'DOTENV_FILE')]) {
                sh 'cp "$DOTENV_FILE" .env'
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
            /**
            steps {
                script {
                    writeFile file: '.env', text: "${DOTENV}"
                }
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
            */
        }
    }
}