import com.ptrampert.github.SemVer

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
    def changes = getChanges owner, repo, "${tagPrefix}${lastVersion.toString()}", credentialsId, githubApiRoot

}
