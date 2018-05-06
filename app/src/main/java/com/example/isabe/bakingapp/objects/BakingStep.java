package com.example.isabe.bakingapp.objects;

/**
 * Created by isabe on 5/5/2018.
 */

public class BakingStep {
    public int id;
    public String briefStepDescription;
    public String longStepDescription;
    public String videoUrl;
    public String thumbnailStepUrl;

    public BakingStep(int id, String shortDesc, String longDesc, String video, String image){
        this.id = id;
        this.briefStepDescription = shortDesc;
        this.longStepDescription = longDesc;
        this.videoUrl = video;
        this.thumbnailStepUrl = image;
    }

    public int getId() {
        return id;
    }

    public String getBriefStepDescription() {
        return briefStepDescription;
    }

    public String getLongStepDescription() {
        return longStepDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailStepUrl() {
        return thumbnailStepUrl;
    }
}
