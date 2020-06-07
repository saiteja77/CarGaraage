pipeline {
  agent {
    docker {
      image 'gradle:6.5.0-jdk8'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'gradle clean'
        sh 'gradle test'
        sh 'gradle bootJar'
        git(poll: true, url: 'https://github.com/saiteja77/cargaraage.git', branch: 'master', credentialsId: '7d25113a-668f-4738-a084-bdfa1f6c09c7')
      }
    }

  }
  environment {
    AWS_ACCESS_KEY = credentials('AWS_ACCESS_KEY')
    AWS_SECRET_KEY = credentials('AWS_SECRET_KEY')
    MONGO_URI = credentials('MONGO_URI')
  }
}