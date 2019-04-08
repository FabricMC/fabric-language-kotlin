pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh "rm -rf build/libs/"
                sh "chmod +x gradlew"
                sh "./gradlew -PincludeSample=false clean build --refresh-dependencies --full-stacktrace"
            }
        }

        stage('Upload to curseforge') {
            when {
                branch 'master'
            }
            steps {
                sh 'git log --format=format:%B ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}..${GIT_COMMIT} > changelog.txt'
                sh './gradlew -PincludeSample=false  -Prelease -Pchangelog_file=changelog.txt curseforge308769'
            }
        }

        stage('Archive artifacts') {
            when {
                branch 'master'
            }
            steps {
                sh "./gradlew -PincludeSample=false publish"
            }
        }

        stage('increment build') {
            when {
                branch 'master'
            }
            steps {
                sh "./gradlew -PincludeSample=false buildNumberIncrement"
            }
        }
    }
}