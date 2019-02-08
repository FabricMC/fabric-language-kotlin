pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                def scmVars = checkout scm
                env.GIT_COMMIT = scmVars.GIT_COMMIT
                env.GIT_PREVIOUS_SUCCESSFUL_COMMIT = scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
            }
        }

        stage('Build') {
            steps {
                sh "rm -rf build/libs/"
                sh "chmod +x gradlew"
                sh "./gradlew clean build --refresh-dependencies --full-stacktrace"
            }
        }

        stage('Upload to curseforge') {
            when {
                branch 'master'
            }
            steps {
                sh 'git log --format=format:%B ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}..${GIT_COMMIT} > changelog.txt'
                sh './gradlew -Prelease -Pchangelog_file=changelog.txt curseforge308769'
            }
        }

        stage('Archive artifacts') {
            when {
                branch 'master'
            }
            steps {
                sh "./gradlew publish"
            }
        }

        stage('increment build') {
            when {
                branch 'master'
            }
            steps {
                sh "./gradlew buildNumberIncrease"
            }
        }
    }
}