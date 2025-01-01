package com.coffeecode.interfaces;

import com.coffeecode.model.DataModel;

public interface OnDataUpdateListener {
    void onDataLoaded(DataModel dataModel);

    void onError(String message);

    void onProgress(int progress);
}