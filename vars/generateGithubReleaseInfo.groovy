import com.ptrampert.github.SemVer
import com.ptrampert.github.ReleaseInfo
import com.ptrampert.github.Change

def call(
    owner,
    repo,
    tagPrefix,
    credentialsId = null,
    githubApiRoot = "https://api.github.com",
    branch = null,
    buildNumber = null
) {
    def prerelease = null
    if (branch != null) {
        prerelease = "${branch.take(7)}.${buildNumber.toString().take(3)}"
    }
    def lastVersion = new SemVer()
    try {
        lastVersion = getLastVersion()
    } catch (e) {
        echo e.message
    }
    echo lastVersion.toString()
    ArrayList<Change> changes = getChanges owner, repo, "${tagPrefix}${lastVersion.toString()}", credentialsId, githubApiRoot
    ReleaseInfo release = new ReleaseInfo(previousVersion: lastVersion, changelog: changes, prerelease: prerelease)
    return release
}
