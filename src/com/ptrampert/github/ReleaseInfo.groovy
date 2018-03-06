package com.ptrampert.github

class ReleaseInfo implements Serializable {
    private static final long serialVersionUID = 1234L

    SemVer previousVersion
    List<Change> changelog

    SemVer nextVersion() {
        ChangeLevel maxChange = changelog.max {
            it.changeLevel
        }.getChangeLevel()
        SemVer next = previousVersion.clone()
        next.increment(maxChange)
        return next
    }
}
