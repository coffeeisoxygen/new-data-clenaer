package com.coffeecode.model;

public class DataModel {
    public static final String DATETIME_HEADER = "DateTime";
    private String[][] dataArray; // Data utama
    private String[] headerArray; // Header
    private int columnCount; // Jumlah kolom
    private int rowCount; // Jumlah baris

    // Getter & Setter
    public String[][] getDataArray() {
        return dataArray;
    }

    public void setDataArray(String[][] dataArray) {
        this.dataArray = dataArray;
    }

    public String[] getHeaderArray() {
        return headerArray;
    }

    public void setHeaderArray(String[] headerArray) {
        this.headerArray = headerArray;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}