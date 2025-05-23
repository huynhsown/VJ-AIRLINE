pipeline {
	agent any

    environment {
		DOCKER_COMPOSE = 'docker-compose'
    }

    stages {
		stage('Build') {
			steps {
				sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
			steps {
				sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
			steps {
				sh 'docker build -t vj-airline:${BUILD_NUMBER} .'
            }
        }

        stage('Deploy') {
			steps {
				withCredentials([
                    string(credentialsId: 'mail-username', variable: 'MAIL_USERNAME'),
                    string(credentialsId: 'mail-password', variable: 'MAIL_PASSWORD'),
                    string(credentialsId: 'cloud-name', variable: 'CLOUD_NAME'),
                    string(credentialsId: 'cloud-api-key', variable: 'CLOUD_API_KEY'),
                    string(credentialsId: 'cloud-api-secret', variable: 'CLOUD_API_SECRET'),
                    string(credentialsId: 'zalopay-app-id', variable: 'APP_ID'),
                    string(credentialsId: 'zalopay-mac-key', variable: 'MAC_KEY'),
                    string(credentialsId: 'zalopay-url', variable: 'URL_CREATE_ORDER'),
                    string(credentialsId: 'paypal-client-id', variable: 'PAYPAL_CLIENT_ID'),
                    string(credentialsId: 'paypal-secret', variable: 'PAYPAL_SECRET'),
                    string(credentialsId: 'paypal-api-url', variable: 'PAYPAL_API_URL')
                ]) {
					sh '''
                        echo "MAIL_USERNAME=${MAIL_USERNAME}" > .env
                        echo "MAIL_PASSWORD=${MAIL_PASSWORD}" >> .env
                        echo "CLOUD_NAME=${CLOUD_NAME}" >> .env
                        echo "CLOUD_API_KEY=${CLOUD_API_KEY}" >> .env
                        echo "CLOUD_API_SECRET=${CLOUD_API_SECRET}" >> .env
                        echo "APP_ID=${APP_ID}" >> .env
                        echo "MAC_KEY=${MAC_KEY}" >> .env
                        echo "URL_CREATE_ORDER=${URL_CREATE_ORDER}" >> .env
                        echo "PAYPAL_CLIENT_ID=${PAYPAL_CLIENT_ID}" >> .env
                        echo "PAYPAL_SECRET=${PAYPAL_SECRET}" >> .env
                        echo "PAYPAL_API_URL=${PAYPAL_API_URL}" >> .env
                        ${DOCKER_COMPOSE} down
                        ${DOCKER_COMPOSE} up -d
                    '''
                }
            }
        }
    }

    post {
		always {
			cleanWs()
        }
        success {
			echo 'Pipeline completed successfully!'
        }
        failure {
			echo 'Pipeline failed!'
        }
    }
}