import groovy.json.JsonOutput
import com.ptrampert.github.ReleaseInfo

def call(
        owner,
        repo,
        ReleaseInfo releaseInfo,
        versionPrefix = '',
        credentialsId = null,
        githubApiRoot = 'https://api.github.com'
) {
    def gitCommit
    if (isUnix()) {
        gitCommit = sh returnStdout: true, script: 'git rev-parse HEAD'
    }
    else {
        gitCommit = bat returnStdout: true, script: 'git rev-parse HEAD'
    }

    def releaseBody = JsonOutput.toJson([
            tag_name: "${versionPrefix}${releaseInfo.nextVersion().toString()}",
            target_commitish: gitCommit.trim(),
            name: "${versionPrefix}${releaseInfo.nextVersion().toString()}",
            body: releaseInfo.changelogToMarkdown()
    ])

    def response = httpRequest(
            url: "${githubApiRoot}/repos/${owner}/${repo}/releases".toString(),
            authentication: credentialsId,
            httpMode: 'POST',
            contentType: 'APPLICATION_JSON',
            body: releaseBody,
    )
    if (response.status > 399) {
        throw new Exception("Failed to create github release")
    }
}