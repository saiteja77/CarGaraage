pipeline {
  agent {
    docker {
      image 'gradle:6.5.0-jdk8'
    }

  }
  stages {
    stage('build') {
      environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
      }
      steps {
        sh '''gradle -v
printenv'''
      }
    }

  }
}
