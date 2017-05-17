package com.tvpage.lib.view;


import com.tvpage.lib.utils.TvPageInterfaces;

import org.json.JSONObject;

public class TvPageBuilder {

    TvPageInterfaces.OnVideoViewReady onVideoViewReady;
    TvPageInterfaces.OnVideoViewError onVideoViewError;
    TvPageInterfaces.OnMediaReady onMediaReady;
    TvPageInterfaces.OnMediaError onMediaError;
    TvPageInterfaces.OnMediaComplete onMediaComplete;
    TvPageInterfaces.OnVideoEnded onVideoEnded;
    TvPageInterfaces.OnVideoPlaying onVideoPlaying;
    TvPageInterfaces.OnVideoPaused onVideoPaused;
    TvPageInterfaces.OnVideoBuffering onVideoBuffering;
    TvPageInterfaces.OnMediaPlayBackQualityChanged onMediaPlayBackQualityChanged;
    TvPageInterfaces.OnMediaProviderChanged onMediaProviderChanged;
    TvPageInterfaces.OnSeek onSeek;
    TvPageInterfaces.OnVideoLoad onVideoLoad;
    TvPageInterfaces.OnVideoCued onVideoCued;
    TvPageInterfaces.OnReady onReady;
    TvPageInterfaces.OnStateChanged onStateChanged;
    TvPageInterfaces.OnError onError;

    TvPagePlayer tvPagePlayer;


    public TvPageBuilder(TvPagePlayer tvPagePlayer) {
        this.tvPagePlayer = tvPagePlayer;
    }


    public TvPageInterfaces.OnError getOnError() {
        return onError;
    }

    public TvPageBuilder setOnError(TvPageInterfaces.OnError onError) {
        this.onError = onError;
        return this;
    }

    public TvPageInterfaces.OnReady getOnReady() {
        return onReady;
    }


    public TvPageInterfaces.OnStateChanged getOnStateChanged() {
        return onStateChanged;
    }

    public TvPageBuilder setOnStateChanged(TvPageInterfaces.OnStateChanged onStateChanged) {
        this.onStateChanged = onStateChanged;
        return this;
    }

    public TvPageBuilder setOnReady(TvPageInterfaces.OnReady onReady) {
        this.onReady = onReady;
        return this;
    }

    public TvPageInterfaces.OnMediaReady getOnMediaReady() {
        return onMediaReady;
    }

    public TvPageBuilder setOnMediaReady(TvPageInterfaces.OnMediaReady onMediaReady) {
        this.onMediaReady = onMediaReady;
        return this;
    }

    public TvPageInterfaces.OnMediaError getOnMediaError() {
        return onMediaError;
    }

    public TvPageBuilder setOnMediaError(TvPageInterfaces.OnMediaError onMediaError) {
        this.onMediaError = onMediaError;
        return this;
    }

    public TvPageInterfaces.OnVideoViewReady getOnVideoViewReady() {
        return onVideoViewReady;
    }

    public TvPageBuilder setOnVideoViewReady(TvPageInterfaces.OnVideoViewReady onVideoViewReady) {
        this.onVideoViewReady = onVideoViewReady;
        return this;
    }

    public TvPageInterfaces.OnVideoViewError getOnVideoViewError() {
        return onVideoViewError;
    }

    public TvPageBuilder setOnVideoViewError(TvPageInterfaces.OnVideoViewError onVideoViewError) {
        this.onVideoViewError = onVideoViewError;
        return this;
    }

    public TvPageInterfaces.OnMediaComplete getOnMediaComplete() {
        return onMediaComplete;
    }

    public TvPageBuilder setOnMediaComplete(TvPageInterfaces.OnMediaComplete onMediaComplete) {
        this.onMediaComplete = onMediaComplete;
        return this;
    }

    public TvPageInterfaces.OnVideoEnded getOnVideoEnded() {
        return onVideoEnded;
    }

    public TvPageBuilder setOnVideoEnded(TvPageInterfaces.OnVideoEnded onVideoEnded) {
        this.onVideoEnded = onVideoEnded;
        return this;
    }

    public TvPageInterfaces.OnVideoPlaying getOnVideoPlaying() {
        return onVideoPlaying;
    }

    public TvPageBuilder setOnVideoPlaying(TvPageInterfaces.OnVideoPlaying onVideoPlaying) {
        this.onVideoPlaying = onVideoPlaying;
        return this;
    }

    public TvPageInterfaces.OnVideoPaused getOnVideoPaused() {
        return onVideoPaused;
    }

    public TvPageBuilder setOnVideoPaused(TvPageInterfaces.OnVideoPaused onVideoPaused) {
        this.onVideoPaused = onVideoPaused;
        return this;
    }

    public TvPageInterfaces.OnVideoBuffering getOnVideoBuffering() {
        return onVideoBuffering;
    }

    public TvPageBuilder setOnVideoBuffering(TvPageInterfaces.OnVideoBuffering onVideoBuffering) {
        this.onVideoBuffering = onVideoBuffering;
        return this;
    }

    public TvPageInterfaces.OnMediaPlayBackQualityChanged getOnMediaPlayBackQualityChanged() {
        return onMediaPlayBackQualityChanged;
    }

    public TvPageBuilder setOnMediaPlayBackQualityChanged(TvPageInterfaces.OnMediaPlayBackQualityChanged onMediaPlayBackQualityChanged) {
        this.onMediaPlayBackQualityChanged = onMediaPlayBackQualityChanged;
        return this;
    }

    public TvPageInterfaces.OnMediaProviderChanged getOnMediaProviderChanged() {
        return onMediaProviderChanged;
    }

    public TvPageBuilder setOnMediaProviderChanged(TvPageInterfaces.OnMediaProviderChanged onMediaProviderChanged) {
        this.onMediaProviderChanged = onMediaProviderChanged;
        return this;
    }

    public TvPageInterfaces.OnSeek getOnSeek() {
        return onSeek;
    }

    public TvPageBuilder setOnSeek(TvPageInterfaces.OnSeek onSeek) {
        this.onSeek = onSeek;
        return this;
    }

    public TvPageInterfaces.OnVideoLoad getOnVideoLoad() {
        return onVideoLoad;
    }

    public TvPageBuilder setOnVideoLoad(TvPageInterfaces.OnVideoLoad onVideoLoad) {
        this.onVideoLoad = onVideoLoad;
        return this;
    }

    public TvPageInterfaces.OnVideoCued getOnVideoCued() {
        return onVideoCued;
    }

    public TvPageBuilder setOnVideoCued(TvPageInterfaces.OnVideoCued onVideoCued) {
        this.onVideoCued = onVideoCued;
        return this;
    }

    public TvPageBuilder initialise() {

        if (this.onVideoViewReady != null) {
            tvPagePlayer.setOnVideoViewReady(this.onVideoViewReady);
        }
        if (this.onVideoViewError != null) {
            tvPagePlayer.setOnVideoViewError(this.onVideoViewError);
        }
        if (this.onMediaReady != null) {
            tvPagePlayer.setOnMediaReady(this.onMediaReady);
        }
        if (this.onMediaError != null) {
            tvPagePlayer.setOnMediaError(this.onMediaError);
        }
        if (this.onMediaComplete != null) {
            tvPagePlayer.setOnMediaComplete(this.onMediaComplete);
        }
        if (this.onVideoEnded != null) {
            tvPagePlayer.setOnVideoEnded(this.onVideoEnded);
        }
        if (this.onVideoPlaying != null) {
            tvPagePlayer.setOnVideoPlaying(this.onVideoPlaying);
        }
        if (this.onVideoPaused != null) {
            tvPagePlayer.setOnVideoPaused(this.onVideoPaused);
        }
        if (this.onVideoBuffering != null) {
            tvPagePlayer.setOnVideoBuffering(this.onVideoBuffering);
        }
        if (this.onMediaPlayBackQualityChanged != null) {
            tvPagePlayer.setOnMediaPlayBackQualityChanged(this.onMediaPlayBackQualityChanged);
        }
        if (this.onMediaProviderChanged != null) {
            tvPagePlayer.setOnMediaProviderChanged(this.onMediaProviderChanged);
        }
        if (this.onSeek != null) {
            tvPagePlayer.setOnSeek(this.onSeek);
        }
        if (this.onVideoLoad != null) {
            tvPagePlayer.setOnVideoLoad(this.onVideoLoad);
        }
        if (this.onVideoCued != null) {
            tvPagePlayer.setOnVideoCued(this.onVideoCued);
        }

        if (this.onReady != null) {
            tvPagePlayer.setOnReady(this.onReady);
        }

        if (this.onStateChanged != null) {
            tvPagePlayer.setOnStateChanged(this.onStateChanged);
        }

        if (this.onError != null) {
            tvPagePlayer.setOnError(this.onError);
        }

        return this;
    }




   /* public TvPageBuilder loadUrl(String mUrlToPlay) {

        //tvPagePlayer.setMurlToPlay(mUrlToPlay);
        //tvPagePlayer.playVideo();
        return this;
    }*/

    public TvPageBuilder size(int width, int height) {
        if (tvPagePlayer != null) {
            tvPagePlayer.resize(width, height);
        }
        return this;
    }

    public TvPageBuilder mediaProviders(String mediaProvidersCommaSeprated) {
        if (tvPagePlayer != null) {
            tvPagePlayer.setMediaProviders(mediaProvidersCommaSeprated);
        }
        return this;
    }

    public TvPageBuilder apiBaseUrl(String apiBaseUrl) {
        if (tvPagePlayer != null) {
            tvPagePlayer.setApiBaseUrl(apiBaseUrl);
        }
        return this;
    }

    public TvPageBuilder controls(JSONObject jsonObject) {
        if (tvPagePlayer != null) {
            tvPagePlayer.setControls(jsonObject);
        }
        return this;
    }
}