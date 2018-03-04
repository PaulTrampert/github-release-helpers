package com.ptrampert.github

class SemVer implements Serializable {
    private static final long serialVersionUID = 1234L

    int major
    int minor
    int patch

    static SemVer parse(str) {
        def matcher = (str =~ /(\d+)\.(\d+)\.(\d+)/)
        def result = new SemVer()
        if (matcher.matches()) {
            throw new Exception("Cannot parse ${str} as SemVer")
        }
        result.major = Integer.parseInt(matcher.group(1))
        result.minor = Integer.parseInt(matcher.group(2))
        result.patch = Integer.parseInt(matcher.group(3))
        return result
    }

    String toString() {
        return "${major}.${minor}.${patch}".toString()
    }

    SemVer clone() {
        return parse(this.toString())
    }

    void increment(ChangeLevel level) {
        switch(level) {
            case ChangeLevel.PATCH:
                patch++
                break
            case ChangeLevel.MINOR:
                patch = 0
                minor++
                break
            case ChangeLevel.MAJOR:
                patch = 0
                minor = 0
                major++
                break
        }
    }
}
