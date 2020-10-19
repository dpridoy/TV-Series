package com.dpridoy.tvseries.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowDetails {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("runtime")
    @Expose
    private String runtime;

    @SerializedName("image_path")
    @Expose
    private String imagePath;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    @SerializedName("pictures")
    @Expose
    private List<String> pictures = null;
    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = null;

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getRating() {
        return rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    //    @SerializedName("id")
//    @Expose
//    private Integer id;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("permalink")
//    @Expose
//    private String permalink;
//    @SerializedName("description_source")
//    @Expose
//    private String descriptionSource;
//    @SerializedName("start_date")
//    @Expose
//    private String startDate;
//    @SerializedName("end_date")
//    @Expose
//    private Object endDate;
//    @SerializedName("country")
//    @Expose
//    private String country;
//    @SerializedName("status")
//    @Expose
//    private String status;
    //    @SerializedName("network")
//    @Expose
//    private String network;
//    @SerializedName("youtube_link")
//    @Expose
//    private Object youtubeLink;
    //    @SerializedName("image_thumbnail_path")
//    @Expose
//    private String imageThumbnailPath;
    //    @SerializedName("rating_count")
//    @Expose
//    private String ratingCount;
//    @SerializedName("countdown")
//    @Expose
//    private Object countdown;

}
