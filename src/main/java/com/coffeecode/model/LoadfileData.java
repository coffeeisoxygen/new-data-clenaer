package com.coffeecode.model;

import com.coffeecode.utils.CustomException;

public interface LoadfileData {
    DataModel loadData(String filePath) throws CustomException; // Method load
}