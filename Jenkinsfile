pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'pirul-fireshield'
        DOCKER_REGISTRY = 'docker.io'
        APP_VERSION = '0.0.1-SNAPSHOT'
    }
    
    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging application...'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    echo 'Build successful!'
                }
            }
        }
        
        stage('Build Docker Image') {
            when {
                expression { env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'master' }
            }
            steps {
                echo 'Building Docker image...'
                script {
                    def dockerfile = """
                    FROM eclipse-temurin:17-jdk-alpine AS builder
                    WORKDIR /app
                    COPY . .
                    RUN ./mvnw package -DskipTests

                    FROM eclipse-temurin:17-jre-alpine
                    WORKDIR /app
                    COPY --from=builder /app/target/*.jar app.jar
                    EXPOSE 8080
                    ENTRYPOINT ["java", "-jar", "app.jar"]
                    """
                    writeFile file: 'Dockerfile', text: dockerfile
                    
                    bat "docker build -t ${DOCKER_IMAGE}:${APP_VERSION} ."
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                expression { env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'master' }
            }
            steps {
                echo 'Deploying to staging environment...'
                // Add your staging deployment commands here
                // e.g., kubectl apply, docker-compose, etc.
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
            emailext (
                subject: "SUCCESS: Build ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build ${env.BUILD_NUMBER} completed successfully.",
                to: 'admin@example.com'
            )
        }
        failure {
            echo 'Pipeline failed!'
            emailext (
                subject: "FAILURE: Build ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build ${env.BUILD_NUMBER} failed. Check logs: ${env.BUILD_URL}",
                to: 'admin@example.com'
            )
        }
    }
}
