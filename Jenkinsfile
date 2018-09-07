def releaseInfo

pipeline {
    agent any;

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Generate Release Info Test') {
            steps {
                script {
                    releaseInfo = generateGithubReleaseInfo(
                            'PaulTrampert',
                            'github-release-helpers',
                            'v',
                            'Github User/Pass'
                    )

                    echo releaseInfo.nextVersion().toString()
                    echo releaseInfo.changelogToMarkdown()
                }
            }
        }

        stage('Publish Release Test') {
            steps {
                script {
                    publishGithubRelease(
                            'PaulTrampert',
                            'github-release-helpers',
                            releaseInfo,
                            'v',
                            'github_token'
                    )
                }
            }
        }
    }
}