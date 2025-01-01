package com.coffeecode.services;

import java.util.ArrayList;
import java.util.List;

import com.coffeecode.factory.DataProcessorFactory;
import com.coffeecode.interfaces.OnDataUpdateListener;
import com.coffeecode.model.DataModel;
import com.coffeecode.model.FileType;
import com.coffeecode.model.LoadfileData;
import com.coffeecode.utils.CustomException;
import com.coffeecode.utils.LoggerUtil;

public class DataModelServices {
    private final List<OnDataUpdateListener> listeners = new ArrayList<>();

    public void addListener(OnDataUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnDataUpdateListener listener) {
        listeners.remove(listener);
    }

    public void processFile(String filePath) {
        try {
            FileType fileType = detectFileType(filePath);
            LoadfileData processor = DataProcessorFactory.createProcessor(fileType);

            notifyProgress(0);
            DataModel dataModel = processor.loadData(filePath);
            notifyDataLoaded(dataModel);
            notifyProgress(100);

        } catch (CustomException e) {
            LoggerUtil.logError("Error processing file", e);
            notifyError(e.getMessage());
        }
    }

    private FileType detectFileType(String filePath) throws CustomException {
        String lowercasePath = filePath.toLowerCase();
        if (lowercasePath.endsWith(FileType.CSV.getExtension())) {
            return FileType.CSV;
        } else if (lowercasePath.endsWith(FileType.XLSX.getExtension())) {
            return FileType.XLSX;
        }
        throw new CustomException("Unsupported file type");
    }

    private void notifyDataLoaded(DataModel dataModel) {
        for (OnDataUpdateListener listener : listeners) {
            listener.onDataLoaded(dataModel);
        }
    }

    private void notifyError(String message) {
        for (OnDataUpdateListener listener : listeners) {
            listener.onError(message);
        }
    }

    private void notifyProgress(int progress) {
        for (OnDataUpdateListener listener : listeners) {
            listener.onProgress(progress);
        }
    }
}