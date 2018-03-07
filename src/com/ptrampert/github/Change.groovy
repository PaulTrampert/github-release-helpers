package com.ptrampert.github

class Change implements Serializable {
    private static final long serialVersionUID = 1234L

    Link author
    Link change
    String description
    ChangeLevel changeLevel

    String getMarkdown() {
        return "* **${changeLevel.toString()}** ${change.getMarkdown()} - ${author.getMarkdown()} - ${description}"
    }
}
