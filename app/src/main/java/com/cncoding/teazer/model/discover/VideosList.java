package com.cncoding.teazer.model.discover;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class VideosList {
    private boolean next_page;
    private ArrayList<Videos> videos;
    private Throwable error;

    public VideosList(boolean next_page, ArrayList<Videos> videos) {
        this.next_page = next_page;
        this.videos = videos;
    }

    public VideosList(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<Videos> getVideos() {
        return videos;
    }
}