pipeline {
  agent {
    docker {
      image 'gradle:6.5.0-jdk8'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'printenv'
      }
    }

  }
  environment {
    AWS_ACCESS_KEY = 'AWS_ACCESS_KEY'
    AWS_SECRET_KEY = 'AWS_SECRET_KEY'
    MONGO_URI = 'MONGO_URI'
  }
}