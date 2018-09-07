package com.ptrampert.github

class ReleaseInfo implements Serializable {
    private static final long serialVersionUID = 1234L

    SemVer previousVersion
    ArrayList<Change> changelog
    String prerelease

    SemVer nextVersion() {
        if (changelog == null || changelog.size() == 0) {
            return previousVersion
        }
        ChangeLevel maxChange = changelog.max {
            it.changeLevel.getValue()
        }.getChangeLevel()
        SemVer next = previousVersion.clone()
        next.increment(maxChange)
        next.prerelease = prerelease
        return next
    }

    String changelogToMarkdown() {
        def majorChanges = []
        def minorChanges = []
        def patches = []

        for (def change : changelog) {
            if (change.changeLevel == ChangeLevel.MAJOR) {
                majorChanges.add(change)
            }
            else if (change.changeLevel == ChangeLevel.MINOR) {
                minorChanges.add(change)
            }
            else {
                patches.add(change)
            }
        }

        def markdown = ""
        markdown += "## Changelog\r\n"
        for (def change : majorChanges) {
            markdown += "${change.getMarkdown()}\r\n"
        }
        for (def change : minorChanges) {
            markdown += "${change.getMarkdown()}\r\n"
        }
        for (def change : patches) {
            markdown += "${change.getMarkdown()}\r\n"
        }
        return markdown
    }
}
