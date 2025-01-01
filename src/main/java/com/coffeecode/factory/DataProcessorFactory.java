package com.coffeecode.factory;

import com.coffeecode.model.CsvProcessor;
import com.coffeecode.model.FileType;
import com.coffeecode.model.LoadfileData;
import com.coffeecode.model.XlsxProcessor;

public class DataProcessorFactory {
    private DataProcessorFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static LoadfileData createProcessor(FileType fileType) {
        return switch (fileType) {
            case CSV -> new CsvProcessor();
            case XLSX -> new XlsxProcessor();
        };
    }
}