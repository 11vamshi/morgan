package com.vamshi.morgan.nevernest;

public class DownloadState {



    enum State{
        InProgress, Pending
    }

    State state;
    String url;

    Download download;

    public DownloadState(String path) {

    }

    public State getState() {
        return this.state;
    }
    public String getUrl() {
        return this.url;
    }
    public void setDownload(Download download) {
    }

}
