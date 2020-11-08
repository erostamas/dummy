pipeline {
    parameters {
        string(defaultValue: 'refs/heads/master',
        description: '"Build Now" workaround',
        name: 'GERRIT_REFSPEC',
        trim: false)
    }
    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '30'))
        timeout(time: 15, unit: 'MINUTES')
        timestamps()
    }
    stages {
        stage('Prepare') {
            steps {
                sh 'git clean -xdff'
            }
        }
        stage('Build') {
            sh 'build.sh'
        }
    }
}