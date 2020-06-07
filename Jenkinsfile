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
      }
    }

  }
  environment {
    AWS_ACCESS_KEY = credentials('AWS_ACCESS_KEY')
    AWS_SECRET_KEY = credentials('AWS_SECRET_KEY')
    MONGO_URI = credentials('MONGO_URI')
  }
}