package com.tvpage.lib.utils;

/**
 * Created by MTPC-110 on 3/8/2017.
 */

public class TvPageInterfaces {

    public interface OnVideoViewReady {
        public void OnVideoViewReady(boolean isVideoLoaded);
    }


    public interface OnVideoViewError {
        public void OnVideoViewError(String error);
    }

    public interface OnMediaReady {
        public void OnMediaReady(boolean isMediaReady);
    }

    public interface OnMediaError {
        public void OnMediaError(String error);
    }

    public interface OnMediaComplete {
        public void OnMediaComplete(boolean isMediaCompleted);
    }

    public interface OnVideoEnded {
        public void OnVideoEnded(boolean isVideoEnded);
    }

    public interface OnVideoPlaying {
        public void OnVideoPlaying(boolean isVideoPlaying);
    }

    public interface OnVideoPaused {
        public void OnVideoPaused(boolean isVideoPaused);
    }

    public interface OnVideoBuffering {
        public void OnVideoBuffering(boolean isVideoBuffering);
    }

    public interface OnMediaPlayBackQualityChanged {
        public void OnMediaPlayBackQualityChanged(String selectedQuality);
    }

    public interface OnMediaProviderChanged {
        public void OnMediaProviderChanged(String selectedProvider);
    }

    public interface OnSeek {
        public void OnSeek(String currentVideoTime);
    }

    public interface OnVideoLoad {
        public void OnVideoLoad(boolean isVideoLoaded);
    }

    public interface OnVideoCued {
        public void OnVideoCued(boolean isVideoLoaded);
    }

    public interface OnReady {
        public void OnPlayerReady();
    }

    public interface OnStateChanged {
        public void OnStateChanged();
    }

    public interface OnError {
        public void OnError();
    }


}
