package com.vamshi.morgan.nevernest;

public class DownloadResult {

    enum Code{
        Success,
        InProgress,
        ConnectionError,
        TimeOut,
        HttpError
    }

    public Code getCode(){
        return Code.Success;
    }

    public int getHTTPCode(){
        return 200;
    }
}
