package com.coffeecode.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coffeecode.utils.CustomException;
import com.coffeecode.utils.LoggerUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class CsvProcessor implements LoadfileData {
    @Override
    public DataModel loadData(String filePath) throws CustomException {
        long startTime = System.currentTimeMillis();
        LoggerUtil.setThreadContext("operation", "csv_load");
        LoggerUtil.setThreadContext("filepath", filePath);

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            LoggerUtil.logIO("Starting CSV file read: {}", filePath);

            String[] row;
            List<String[]> rows = new ArrayList<>();
            String[] header = null;

            while ((row = reader.readNext()) != null) {
                if (header == null && containsHeader(row, DataModel.DATETIME_HEADER)) {
                    header = row;
                    LoggerUtil.logInfo("Found header with {} column", DataModel.DATETIME_HEADER);
                } else if (!isRowEmpty(row)) {
                    rows.add(row);
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
            LoggerUtil.logPerformance("CSV file loading completed in {} ms", (endTime - startTime));
            LoggerUtil.logAudit("Successfully loaded CSV file: {} with {} rows", filePath, rows.size());

            return dataModel;

        } catch (FileNotFoundException e) {
            LoggerUtil.logError("File not found: " + filePath, e);
            throw new CustomException("File not found: " + filePath, e);
        } catch (IOException e) {
            LoggerUtil.logError("Error reading CSV file: " + filePath, e);
            throw new CustomException("Error reading CSV file: " + filePath, e);
        } catch (CsvValidationException e) {
            LoggerUtil.logError("Invalid CSV format in file: " + filePath, e);
            throw new CustomException("Invalid CSV format in file: " + filePath, e);
        } finally {
            LoggerUtil.clearThreadContext();
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
