package ru.itsm365.vita.coder.model;

import java.util.List;

public class Table {
    private int start;
    private int end;
    private boolean hasHeader;
    private List<List<String>> data;

    public Table(int start, int end, boolean hasHeader, List<List<String>> data) {
        this.start = start;
        this.end = end;
        this.hasHeader = hasHeader;
        this.data = data;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public List<List<String>> getData() {
        return data;
    }
}
