package com.tvpage.lib.model;

/**
 * Created by MTPC-110 on 4/6/2017.
 */

public class TvPageChannelModel {
    private String tags;

    private String loginId;

    private String count_videos;

    private String date_modified;

    private String status;

    private String visibility;

    private Settings settings;

    private String data;

    private String referenceId;

    private String date_created;

    private String entityType;

    private String sourceId;

    private String id;

    private String category;

    private String title;

    private String duration;

    private String search;

    private String transcripts;

    private String description;

    private String titleTextEncoded;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getCount_videos() {
        return count_videos;
    }

    public void setCount_videos(String count_videos) {
        this.count_videos = count_videos;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(String transcripts) {
        this.transcripts = transcripts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleTextEncoded() {
        return titleTextEncoded;
    }

    public void setTitleTextEncoded(String titleTextEncoded) {
        this.titleTextEncoded = titleTextEncoded;
    }

    public static class Settings {
        private String colorClass;

        private String canvasType;

        private String canvasImage;

        private String canvasCropped;

        private String canvasUrl;

        public String getColorClass() {
            return colorClass;
        }

        public void setColorClass(String colorClass) {
            this.colorClass = colorClass;
        }

        public String getCanvasType() {
            return canvasType;
        }

        public void setCanvasType(String canvasType) {
            this.canvasType = canvasType;
        }

        public String getCanvasImage() {
            return canvasImage;
        }

        public void setCanvasImage(String canvasImage) {
            this.canvasImage = canvasImage;
        }

        public String getCanvasCropped() {
            return canvasCropped;
        }

        public void setCanvasCropped(String canvasCropped) {
            this.canvasCropped = canvasCropped;
        }

        public String getCanvasUrl() {
            return canvasUrl;
        }

        public void setCanvasUrl(String canvasUrl) {
            this.canvasUrl = canvasUrl;
        }


    }


}
