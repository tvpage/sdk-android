package com.tvpage.lib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MTPC-110 on 3/30/2017.
 */

public class TvPageVideoModel implements Parcelable {

    private String merchandised;

    private String tags;

    private String loginId;

    private Asset asset;

    private String visibility;

    private String date_modified;

    private String VIDEO_LENGTH;

    private String status;

    private String settings;

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

    private String published;

    private String entityIdParent;

    public String getEntityIdParent() {
        return entityIdParent;
    }

    public void setEntityIdParent(String entityIdParent) {
        this.entityIdParent = entityIdParent;
    }

    public String getMerchandised() {
        return merchandised;
    }

    public void setMerchandised(String merchandised) {
        this.merchandised = merchandised;
    }

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

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getVIDEO_LENGTH() {
        return VIDEO_LENGTH;
    }

    public void setVIDEO_LENGTH(String VIDEO_LENGTH) {
        this.VIDEO_LENGTH = VIDEO_LENGTH;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
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

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }


    public static class Asset implements Parcelable {


        private String dashUrl;

        private String mediaDuration;

        private String thumbnailUrl;

        private String type;

        private String prettyDuration;

        private List<VideoMeta> videoMeta;

        private String hlsUrl;

        private List<User> user;

        private List<Sources> sources;

        private String tags;

        private String hdvFlag;

        private String author;

        private String description;

        private String videoId;

        public String getDashUrl() {
            return dashUrl;
        }

        public void setDashUrl(String dashUrl) {
            this.dashUrl = dashUrl;
        }

        public String getMediaDuration() {
            return mediaDuration;
        }

        public void setMediaDuration(String mediaDuration) {
            this.mediaDuration = mediaDuration;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrettyDuration() {
            return prettyDuration;
        }

        public void setPrettyDuration(String prettyDuration) {
            this.prettyDuration = prettyDuration;
        }

        public List<VideoMeta> getVideoMeta() {
            return videoMeta;
        }

        public void setVideoMeta(List<VideoMeta> videoMeta) {
            this.videoMeta = videoMeta;
        }

        public String getHlsUrl() {
            return hlsUrl;
        }

        public void setHlsUrl(String hlsUrl) {
            this.hlsUrl = hlsUrl;
        }

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

        public List<Sources> getSources() {
            return sources;
        }

        public void setSources(List<Sources> sources) {
            this.sources = sources;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getHdvFlag() {
            return hdvFlag;
        }

        public void setHdvFlag(String hdvFlag) {
            this.hdvFlag = hdvFlag;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.dashUrl);
            dest.writeString(this.mediaDuration);
            dest.writeString(this.thumbnailUrl);
            dest.writeString(this.type);
            dest.writeString(this.prettyDuration);
            dest.writeList(this.videoMeta);
            dest.writeString(this.hlsUrl);
            dest.writeList(this.user);
            dest.writeList(this.sources);
            dest.writeString(this.tags);
            dest.writeString(this.hdvFlag);
            dest.writeString(this.author);
            dest.writeString(this.description);
            dest.writeString(this.videoId);
        }

        public Asset() {
        }

        protected Asset(Parcel in) {
            this.dashUrl = in.readString();
            this.mediaDuration = in.readString();
            this.thumbnailUrl = in.readString();
            this.type = in.readString();
            this.prettyDuration = in.readString();
            this.videoMeta = new ArrayList<VideoMeta>();
            in.readList(this.videoMeta, VideoMeta.class.getClassLoader());
            this.hlsUrl = in.readString();
            this.user = new ArrayList<User>();
            in.readList(this.user, User.class.getClassLoader());
            this.sources = new ArrayList<Sources>();
            in.readList(this.sources, Sources.class.getClassLoader());
            this.tags = in.readString();
            this.hdvFlag = in.readString();
            this.author = in.readString();
            this.description = in.readString();
            this.videoId = in.readString();
        }

        public static final Creator<Asset> CREATOR = new Creator<Asset>() {
            @Override
            public Asset createFromParcel(Parcel source) {
                return new Asset(source);
            }

            @Override
            public Asset[] newArray(int size) {
                return new Asset[size];
            }
        };
    }

    public static class VideoMeta implements Parcelable {
        private String duration;

        private String height;

        private String width;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.duration);
            dest.writeString(this.height);
            dest.writeString(this.width);
        }

        public VideoMeta() {
        }

        protected VideoMeta(Parcel in) {
            this.duration = in.readString();
            this.height = in.readString();
            this.width = in.readString();
        }

        public static final Creator<VideoMeta> CREATOR = new Creator<VideoMeta>() {
            @Override
            public VideoMeta createFromParcel(Parcel source) {
                return new VideoMeta(source);
            }

            @Override
            public VideoMeta[] newArray(int size) {
                return new VideoMeta[size];
            }
        };
    }

    public static class User implements Parcelable {
        private String lastName;

        private String emailAddress;

        private String firstName;

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.lastName);
            dest.writeString(this.emailAddress);
            dest.writeString(this.firstName);
        }

        public User() {
        }

        protected User(Parcel in) {
            this.lastName = in.readString();
            this.emailAddress = in.readString();
            this.firstName = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }

    public static class Sources implements Parcelable {
        private String height;

        private String file;

        private String width;

        private String quality;

        private String mime;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.height);
            dest.writeString(this.file);
            dest.writeString(this.width);
            dest.writeString(this.quality);
            dest.writeString(this.mime);
        }

        public Sources() {
        }

        protected Sources(Parcel in) {
            this.height = in.readString();
            this.file = in.readString();
            this.width = in.readString();
            this.quality = in.readString();
            this.mime = in.readString();
        }

        public static final Creator<Sources> CREATOR = new Creator<Sources>() {
            @Override
            public Sources createFromParcel(Parcel source) {
                return new Sources(source);
            }

            @Override
            public Sources[] newArray(int size) {
                return new Sources[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.merchandised);
        dest.writeString(this.tags);
        dest.writeString(this.loginId);
        dest.writeParcelable(this.asset, flags);
        dest.writeString(this.visibility);
        dest.writeString(this.date_modified);
        dest.writeString(this.VIDEO_LENGTH);
        dest.writeString(this.status);
        dest.writeString(this.settings);
        dest.writeString(this.data);
        dest.writeString(this.referenceId);
        dest.writeString(this.date_created);
        dest.writeString(this.entityType);
        dest.writeString(this.sourceId);
        dest.writeString(this.id);
        dest.writeString(this.category);
        dest.writeString(this.title);
        dest.writeString(this.duration);
        dest.writeString(this.search);
        dest.writeString(this.transcripts);
        dest.writeString(this.description);
        dest.writeString(this.titleTextEncoded);
        dest.writeString(this.published);
        dest.writeString(this.entityIdParent);
    }

    public TvPageVideoModel() {
    }

    protected TvPageVideoModel(Parcel in) {
        this.merchandised = in.readString();
        this.tags = in.readString();
        this.loginId = in.readString();
        this.asset = in.readParcelable(Asset.class.getClassLoader());
        this.visibility = in.readString();
        this.date_modified = in.readString();
        this.VIDEO_LENGTH = in.readString();
        this.status = in.readString();
        this.settings = in.readString();
        this.data = in.readString();
        this.referenceId = in.readString();
        this.date_created = in.readString();
        this.entityType = in.readString();
        this.sourceId = in.readString();
        this.id = in.readString();
        this.category = in.readString();
        this.title = in.readString();
        this.duration = in.readString();
        this.search = in.readString();
        this.transcripts = in.readString();
        this.description = in.readString();
        this.titleTextEncoded = in.readString();
        this.published = in.readString();
        this.entityIdParent = in.readString();
    }

    public static final Creator<TvPageVideoModel> CREATOR = new Creator<TvPageVideoModel>() {
        @Override
        public TvPageVideoModel createFromParcel(Parcel source) {
            return new TvPageVideoModel(source);
        }

        @Override
        public TvPageVideoModel[] newArray(int size) {
            return new TvPageVideoModel[size];
        }
    };
}
