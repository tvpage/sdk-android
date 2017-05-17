package com.tvpage.lib.api_listeners;

import com.tvpage.lib.model.TvPageResponseModel;

/**
 * Created by MTPC-110 on 4/4/2017.
 */

public interface OnTvPageResponseApiListener {
    void onSuccess(TvPageResponseModel tvPageResponseModel);

    void onFailure(Throwable throwable);
}
