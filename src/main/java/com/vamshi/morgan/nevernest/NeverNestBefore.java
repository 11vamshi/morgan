package com.vamshi.morgan.nevernest;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
* https://www.youtube.com/watch?v=CFRhGnuXG-4&ab_channel=CodeAesthetic
*  code similar to @see com.vamshi.morgan.reentrant.ReentrantWithCondition's method 1 and method 2
*
*/


public class NeverNestBefore extends Thread{

    ConcurrentLinkedQueue<String> requestedUrls = new ConcurrentLinkedQueue<String>();
    ConcurrentLinkedQueue<String> failedUrls = new ConcurrentLinkedQueue<String>();
    LinkedList<DownloadState> downloads = new LinkedList<>();

    Lock requestLock = new ReentrantLock();
    private Condition newRequest = requestLock.newCondition();

    private boolean connectionDisabled = false;
    private String downloadDir = "/Users/vpatha/Downloads/morgan";

    public void appendDownload(String url){
        requestLock.lock();
        try{
            requestedUrls.offer(url);
            newRequest.signalAll();

        }finally {
            requestLock.unlock();
        }
    }

    public void run(){
        while(true){

            String path;
            while((path = requestedUrls.poll()) != null){
                downloads.add(new DownloadState(path));
            }

            if(!this.connectionDisabled){
                ListIterator<DownloadState> iterator = downloads.listIterator();

                while(iterator.hasNext() && !this.connectionDisabled){
                    DownloadState downloadState = iterator.next();

                    if(downloadState.getState() == DownloadState.State.Pending){

                        Download download = new Download(downloadState.getUrl(), this.downloadDir);
                        download.start();
                        downloadState.setDownload(download);

                        download.moveTo(DownloadState.State.InProgress);
                    }

                    if(downloadState.getState() == DownloadState.State.InProgress){
                        // do something
                    }

                    if(downloads.isEmpty() || requestedUrls.isEmpty()){
                        requestLock.lock();
                        try{
                            newRequest.await();
                        }catch (InterruptedException e){
                            return;
                        }
                        finally {
                            requestLock.unlock();
                        }
                    }
                }
            }
        }
    }

}
