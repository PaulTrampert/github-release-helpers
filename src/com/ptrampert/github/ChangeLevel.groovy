package com.ptrampert.github

enum ChangeLevel {
    PATCH(0),
    MINOR(1),
    MAJOR(2)

    ChangeLevel(int value) {
        this.value = value
    }

    final int value
}
