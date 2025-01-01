package com.coffeecode.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.coffeecode.utils.CustomException;
import com.coffeecode.utils.LoggerUtil;

public class XlsxProcessor implements LoadfileData {

    @Override
    public DataModel loadData(String filePath) throws CustomException {
        long startTime = System.currentTimeMillis();
        LoggerUtil.setThreadContext("operation", "xlsx_load");
        LoggerUtil.setThreadContext("filepath", filePath);

        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = WorkbookFactory.create(fis)) {

            LoggerUtil.logIO("Starting XLSX file read: {}", filePath);

            Sheet sheet = workbook.getSheetAt(0); // Assume the first sheet is the target
            List<String[]> rows = new ArrayList<>();
            String[] header = null;

            for (Row row : sheet) {
                String[] rowData = processRow(row);
                if (header == null && containsHeader(rowData, DataModel.DATETIME_HEADER)) {
                    header = rowData;
                    LoggerUtil.logInfo("Found header with {} column", DataModel.DATETIME_HEADER);
                } else if (!isRowEmpty(rowData)) {
                    rows.add(rowData);
                }
            }

            if (header == null) {
                LoggerUtil.logError("No valid header found",
                        new CustomException("No valid header found with DateTime column"));
                throw new CustomException("No valid header found with DateTime column");
            }

            DataModel dataModel = new DataModel();
            dataModel.setHeaderArray(header);
            dataModel.setDataArray(rows.toArray(new String[0][0]));
            dataModel.setRowCount(rows.size());
            dataModel.setColumnCount(header.length);

            long endTime = System.currentTimeMillis();
            LoggerUtil.logPerformance("XLSX file loading completed in {} ms", (endTime - startTime));
            LoggerUtil.logAudit("Successfully loaded XLSX file: {} with {} rows", filePath, rows.size());

            return dataModel;

        } catch (IOException e) {
            LoggerUtil.logError("Error reading XLSX file: " + filePath, e);
            throw new CustomException("Error reading XLSX file: " + filePath, e);
        } finally {
            LoggerUtil.clearThreadContext();
        }
    }

    // Process a single row into a String array
    private String[] processRow(Row row) {
        int cellCount = row.getLastCellNum();
        String[] rowData = new String[cellCount];

        for (int i = 0; i < cellCount; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            rowData[i] = getCellValue(cell);
        }

        return rowData;
    }

    // Get the value of a cell as a String
    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING -> {
                return cell.getStringCellValue().trim();
            }
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> {
                return String.valueOf(cell.getBooleanCellValue());
            }
            case FORMULA -> {
                return cell.getCellFormula();
            }
            case BLANK -> {
                return "";
            }
            default -> {
                return "";
            }
        }
    }

    // Cek apakah baris kosong
    private boolean isRowEmpty(String[] row) {
        if (row == null)
            return true;
        for (String cell : row) {
            if (cell != null && !cell.trim().isEmpty())
                return false;
        }
        return true;
    }

    // Deteksi header dengan kata kunci "DateTime"
    private boolean containsHeader(String[] row, String headerText) {
        for (String cell : row) {
            if (cell != null && cell.equalsIgnoreCase(headerText))
                return true;
        }
        return false;
    }
}
