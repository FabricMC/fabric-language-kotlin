node {
   stage('Checkout') {
       def scmVars = checkout scm
       env.GIT_COMMIT = scmVars.GIT_COMMIT
       env.GIT_PREVIOUS_SUCCESSFUL_COMMIT = scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
   }

   stage('Build') {
       sh "rm -rf build/libs/"
       sh "chmod +x gradlew"
       sh "./gradlew clean build --refresh-dependencies --full-stacktrace"
   }

   stage('Upload to curseforge') {
       sh 'git log --format=format:%B ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}..${GIT_COMMIT} > changelog.txt'
       sh './gradlew -Prelease -Pchangelog_file=changelog.txt curseforge308769'
   }

   stage('Archive artifacts') {
       sh "./gradlew publish"
   }

   stage('increment build') {
       sh "./gradlew buildNumberIncrease"
   }
}
