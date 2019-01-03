package com.example.isabe.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isabe on 5/5/2018.
 */

public class BakingStep implements Parcelable {
    private final int id;
    private final String briefStepDescription;
    private final String longStepDescription;
    private final String videoUrl;
    private final String thumbnailStepUrl;

    public BakingStep(int id, String shortDesc, String longDesc, String video, String image) {
        this.id = id;
        this.briefStepDescription = shortDesc;
        this.longStepDescription = longDesc;
        this.videoUrl = video;
        this.thumbnailStepUrl = image;
    }

    BakingStep(Parcel in) {
        id = in.readInt();
        briefStepDescription = in.readString();
        longStepDescription = in.readString();
        videoUrl = in.readString();
        thumbnailStepUrl = in.readString();
    }

    public static final Parcelable.Creator<BakingStep> CREATOR = new Parcelable.Creator<BakingStep>() {
        @Override
        public BakingStep createFromParcel(Parcel in) {
            return new BakingStep(in);
        }

        @Override
        public BakingStep[] newArray(int size) {
            return new BakingStep[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(briefStepDescription);
        dest.writeString(longStepDescription);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailStepUrl);
    }
}
