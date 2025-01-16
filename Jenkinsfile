pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Pull the latest code from the Git repository
                git branch: 'main', url: 'https://github.com/Hamza007-pro/TP_Pipeline.git'
        }

        stage('Build Services') {
            steps {
                script {
                    // Build each microservice using Maven
                    def services = ['Gateway', 'client', 'Voiture']
                    services.each { service ->
                        dir(service) {
                            sh "mvn clean package -DskipTests"
                        }
                    }
                }
            }
        }

        stage('Run SonarQube Analysis') {
            steps {
                script {
                    // Run SonarQube analysis for each service
                    def services = ['Gateway', 'client', 'Voiture']
                    services.each { service ->
                        dir(service) {
                            withSonarQubeEnv('SonarQube') { // Ensure the SonarQube server is configured in Jenkins
                                sh """
                                mvn sonar:sonar \
                                    -Dsonar.projectKey=${service} \
                                    -Dsonar.host.url=http://localhost:30000 \
                                    -Dsonar.login=sqp_1ff59e4e31696dabab7bd7fd9317ef7793a903ac
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run tests for each microservice
                    def services = ['Gateway', 'client', 'Voiture']
                    services.each { service ->
                        dir(service) {
                            sh "mvn test"
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    // Build Docker images locally for each service
                    def services = [
                        ['name': 'gateway-service', 'path': './Gateway'],
                        ['name': 'client-service', 'path': './client'],
                        ['name': 'voiture-service', 'path': './Voiture']
                    ]
                    services.each { service ->
                        sh "docker build -t ${service.name}:latest -f ${service.path}/Dockerfile ${service.path}"
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    // Use Docker Compose to deploy the services
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Clean up workspace after pipeline run
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for details.'
        }
    }
}
