import com.ptrampert.github.Change
import com.ptrampert.github.ChangeLevel
import com.ptrampert.github.Link

@NonCPS
def call(
        owner,
        repo,
        lastVersion,
        credentialsId = null,
        githubApiRoot = "https://api.github.com"
) {
    ArrayList<Change> changes = new ArrayList<Change>()
    def apiRoot = githubApiRoot
    def responseBody = makeRequest(
            "${apiRoot}/repos/${owner}/${repo}/compare/${lastVersion.toString()}...${env.BRANCH_NAME ?: 'master'}".toString(),
            credentialsId
    )
    def commits = responseBody.commits
    def pullRequests = []
    def commitsInPrs = []
    for (def commit : commits) {
        def matcher = (commit.commit.message =~ /Merge pull request #(\d+)/)
        if (matcher.find()) {
            def pr = makeRequest("${apiRoot}/repos/${owner}/${repo}/pulls/${matcher.group(1)}", credentialsId)
            def prCommits = makeRequest(pr.commits_url, credentialsId)
            pullRequests.add(pr)
            for(def prCommit : prCommits) {
                commitsInPrs.add(prCommit.sha)
            }
            commitsInPrs.add(commit.sha)
        }
    }

    for (def pr : pullRequests) {
        Change change = new Change()
        change.author = new Link(text: pr.user.login, href: pr.user.html_url)
        change.change = new Link(text: pr.id, href: pr.html_url)
        change.description = pr.title
        ChangeLevel maxChange = ChangeLevel.PATCH
        for (def label : pr.labels) {
           try {
                ChangeLevel labeledChangeLevel = ChangeLevel.valueOf(label.name.toUpperCase())
                if (labeledChangeLevel.getValue() > maxChange.getValue()) {
                    maxChange = labeledChangeLevel
                }
            } catch (Exception e) {
               echo "Unrecognized label ${label.name}"
            }
        }
        change.changeLevel = maxChange
        changes.add(change)
    }

    for (def commit : commits) {
        if (!(commit.sha in commitsInPrs)) {
            Change change = new Change()
            change.author = new Link(text: commit.author.login, href: commit.author.html_url)
            change.change = new Link(text: commit.sha.substring(0, 5), href: commit.html_url)
            change.description = commit.commit.message
            change.changeLevel = ChangeLevel.PATCH
            changes.add(change)
        }
    }
    return changes
}