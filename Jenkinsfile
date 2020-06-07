pipeline {
  agent {
    docker {
      image 'gradle:6.5.0-jdk8'
    }

  }
  stages {
    stage('build') {
      steps {
        sh '''gradle -v
printenv'''
      }
    }

  }
}
