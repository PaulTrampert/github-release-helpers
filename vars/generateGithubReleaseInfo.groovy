import com.ptrampert.github.SemVer
import com.ptrampert.github.ReleaseInfo

def call(
    owner,
    repo,
    tagPrefix,
    credentialsId = null,
    githubApiRoot = "https://api.github.com"
) {
    def lastVersion = new SemVer()
    try {
        lastVersion = getLastVersion()
    } catch (e) {
        echo e.message
    }
    echo lastVersion.toString()
    ArrayList<Change> changes = getChanges owner, repo, "${tagPrefix}${lastVersion.toString()}", credentialsId, githubApiRoot
    ReleaseInfo release = new ReleaseInfo(previousVersion: lastVersion, changelog: changes)
    return release
}
