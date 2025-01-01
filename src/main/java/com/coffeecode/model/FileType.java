package com.coffeecode.model;

public enum FileType {
    CSV(".csv"),
    XLSX(".xlsx");

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}