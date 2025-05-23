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
                    string(credentialsId: 'MAIL_USERNAME', variable: 'MAIL_USERNAME'),
                    string(credentialsId: 'MAIL_PASSWORD', variable: 'MAIL_PASSWORD'),
                    string(credentialsId: 'CLOUD_NAME', variable: 'CLOUD_NAME'),
                    string(credentialsId: 'CLOUD_API_KEY', variable: 'CLOUD_API_KEY'),
                    string(credentialsId: 'CLOUD_API_SECRET', variable: 'CLOUD_API_SECRET'),
                    string(credentialsId: 'APP_ID', variable: 'APP_ID'),
                    string(credentialsId: 'MAC_KEY', variable: 'MAC_KEY'),
                    string(credentialsId: 'URL_CREATE_ORDER', variable: 'URL_CREATE_ORDER'),
                    string(credentialsId: 'PAYPAL_CLIENT_ID', variable: 'PAYPAL_CLIENT_ID'),
                    string(credentialsId: 'PAYPAL_SECRET', variable: 'PAYPAL_SECRET'),
                    string(credentialsId: 'PAYPAL_API_URL', variable: 'PAYPAL_API_URL')
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