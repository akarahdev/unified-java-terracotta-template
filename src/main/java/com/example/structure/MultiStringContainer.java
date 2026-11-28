package com.example.structure;

import java.util.ArrayList;
import java.util.List;

public class MultiStringContainer {
    private final List<StringBuilder> stringBuilders = new ArrayList<>();
    private final String joiner;

    public MultiStringContainer(String joiner) {
        this.joiner = joiner;
        this.stringBuilders.add(new StringBuilder());
    }

    public void add(String entry) {
        this.stringBuilders.getLast().append(entry).append(joiner);
        if(this.stringBuilders.getLast().length() > 9000) {
            this.stringBuilders.add(new StringBuilder());
        }
    }

    public List<String> toStrings() {
        return this.stringBuilders.stream().map(StringBuilder::toString).toList();
    }
}
