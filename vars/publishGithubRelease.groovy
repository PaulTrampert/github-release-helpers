import groovy.json.JsonOutput
import com.ptrampert.github.ReleaseInfo

def call(
        owner,
        repo,
        ReleaseInfo releaseInfo,
        versionPrefix = null,
        credentialsId = null,
        filePath = null) {
    def gitCommit
    if (isUnix()) {
        gitCommit = sh returnStdout: true, script: 'git rev-parse HEAD'
    }
    else {
        gitCommit = bat returnStdout: true, script: 'git rev-parse HEAD'
    }

    echo JsonOutput.toJson([
            tag_name: releaseInfo.nextVersion().toString(),
            target_commitish: gitCommit,
            name: releaseInfo.nextVersion().toString(),
            body: releaseInfo.changelogToMarkdown()
    ])
}