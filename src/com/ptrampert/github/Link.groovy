package com.ptrampert.github

class Link implements Serializable {
    private static final long serialVersionUID = 1234L

    String text
    String href

    String getMarkdown() {
        return "[${text}](${href})"
    }
}
