pipeline {
	agent any

    environment {
		DOCKER_COMPOSE = 'docker-compose'
    }

    stages {
		stage('Build') {
			steps {
				echo 'Đang build ứng dụng...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
			steps {
				echo 'Đang chạy tests...'
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
			steps {
				echo 'Đang build Docker image...'
                sh 'docker build -t vj-airline:${BUILD_NUMBER} .'
            }
        }

        stage('Stop Old Containers') {
			steps {
				echo 'Đang dừng containers cũ...'
                sh '''
                    ${DOCKER_COMPOSE} down || true
                    docker system prune -f || true
                '''
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
					echo 'Đang tạo file .env và deploy...'
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

                        echo "Đang khởi động MySQL và Redis trước..."
                        ${DOCKER_COMPOSE} up -d mysql redis

                        echo "Đang chờ MySQL khởi động hoàn tất..."
                        sleep 30

                        echo "Đang khởi động ứng dụng..."
                        ${DOCKER_COMPOSE} up -d app

                        echo "Đang khởi động Redis Insight..."
                        ${DOCKER_COMPOSE} up -d redis-insight

                        echo "Checking container status..."
                        ${DOCKER_COMPOSE} ps
                    '''
                }
            }
        }

        stage('Health Check') {
			steps {
				echo 'Đang kiểm tra tình trạng ứng dụng...'
                script {
					sleep(time: 30, unit: 'SECONDS')
                    sh '''
                        echo "Checking application logs..."
                        ${DOCKER_COMPOSE} logs --tail=50 app

                        echo "Testing application endpoint..."
                        for i in {1..10}; do
                            if curl -f http://localhost:8088/actuator/health 2>/dev/null; then
                                echo "Ứng dụng đã sẵn sàng!"
                                exit 0
                            fi
                            echo "Đang chờ ứng dụng khởi động... (lần thử $i/10)"
                            sleep 10
                        done
                        echo "Cảnh báo: Không thể kết nối đến ứng dụng"
                    '''
                }
            }
        }
    }

    post {
		always {
			echo 'Đang dọn dẹp workspace...'
            cleanWs()
        }
        success {
			echo 'Pipeline hoàn thành thành công! ✅'
        }
        failure {
			echo 'Pipeline thất bại! ❌'
            sh '''
                echo "Checking container logs for debugging..."
                ${DOCKER_COMPOSE} logs app || true
                ${DOCKER_COMPOSE} logs mysql || true
            '''
        }
    }
}