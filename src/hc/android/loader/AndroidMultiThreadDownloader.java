package hc.android.loader;

import hc.android.ActivityManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;
import android.widget.ProgressBar;

public class AndroidMultiThreadDownloader {
	private ProgressBar progress = new ProgressBar(ActivityManager.getActivity(), null, android.R.attr.progressBarStyleHorizontal);
	private int totalByted;
	int downloadByte;
	boolean isError;
	boolean isCancel;
	Thread refreshProgress = null;
	String fileName;
	DownloadThread[] dts;
	
	public AndroidMultiThreadDownloader(){
	}
	
	public ProgressBar getFinishPercent(){
		return progress;
	}
	
	public void download(final Vector url_download, final File file, final String md5,
			final Runnable biz, final Runnable failBiz, final boolean isVisiable) {
		this.fileName = file.getName();
		final int threadNum = url_download.size();
		dts = new DownloadThread[threadNum];
		
        final String firstURL = (String)url_download.get(0);
        try {  
			URL url = new URL(firstURL);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setReadTimeout(5000);  
            if (conn.getResponseCode() == 200) {  
                totalByted = conn.getContentLength();  
                RandomAccessFile raf = new RandomAccessFile(file, "rw");  
                raf.setLength(totalByted);  
                raf.close();  
                final int block = totalByted / threadNum;  
                int startIdx = 0;
                int endIdx = 0;
                for (int threadId = 0; threadId < threadNum; threadId++) { 
                	if(threadId != (threadNum - 1)){
                		endIdx = startIdx + block;
                		dts[threadId] = new DownloadThread(threadId, this, startIdx, endIdx, file, new URL((String)url_download.get(threadId)));
                		startIdx = endIdx + 1;
                	}else{
                		dts[threadId] = new DownloadThread(threadId, this, startIdx, totalByted, file, new URL((String)url_download.get(threadId)));
                	}
                	dts[threadId].start();
                }  
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
            isError = true;  
        }  
        
		final long startMS = System.currentTimeMillis();
		final String dnFileName = firstURL.substring(firstURL.lastIndexOf("/") + 1);
		final String desc_str = buildDownloadMsg(dnFileName, fileName, md5, downloadByte, totalByted, startMS);

        refreshProgress = new Thread(){
        	int ms = 0;
        	int totalMS = 0; 
        	public void run(){
        		totalMS = totalByted / 1024 / 1024;
        		while(isError == false && isCancel == false && (downloadByte < totalByted)){
        			try{
        				Thread.sleep(1000);
        			}catch (Exception e) {
					}
        			final String desc_str = buildDownloadMsg(dnFileName, fileName, md5, downloadByte, totalByted, startMS);
        			final int percent = downloadByte * 100 / totalByted;
					progress.setProgress(percent);
					progress.postInvalidate();
					
        			int newMS = downloadByte / 1024 / 1024;
        			if(newMS != ms){
        				ms = newMS;
        				System.out.println(fileName + " installed " + ms + "MB, total " + totalMS + "MS.");
        			}
        		}
        		if(isError){
    				final String message = "Error on download file, please retry later.";
    				System.out.println(message);
        			if(failBiz != null){
        				failBiz.run();
        			}
        		}else if(isCancel){
        			return;
        		}else{
        			String filemd5 = StringUtil.getMD5(file);
        			if(filemd5.toLowerCase().equals(md5.toLowerCase())){
        				biz.run();
        			}else{
        				if(failBiz != null){
            				failBiz.run();
            			}
        			}
        		}
        	}
        };
        refreshProgress.start();
    }  
	
	int lastDispReaded;
	final int avgSecond = 5;
	int[] lastDispReadedArr = new int[avgSecond];
	int storeLastIdx = 0;
	private String buildDownloadMsg(String fromURL, String storeFile, String md5, 
			int readed, int total, long startMS){
		final int readedSec = readed - lastDispReaded;
		lastDispReaded = readed;
		
		lastDispReadedArr[storeLastIdx%avgSecond] = readedSec;
		int lastFiveTotal = 0;
		for (int i = 0; i < avgSecond; i++) {
			lastFiveTotal += lastDispReadedArr[i];
		}
		storeLastIdx++;
		int avg = (storeLastIdx<=avgSecond)?(lastFiveTotal/storeLastIdx):(lastFiveTotal/avgSecond);
		avg = avg/1024;
		String out = "<html><BR>";
		float process = (float) readed / total * 100;// 算出百分比
		long costMS = System.currentTimeMillis() - startMS;
		long leftSeconds = ((readed==0)?3600:((costMS * total / readed - costMS) / 1000));
		float totalM = (total * 1.0F) / 1024.0F / 1024.0F;
		float readedM = (readed * 1.0F) / 1024.0F / 1024.0F;
		out += "<STRONG>Downloaded :    " + String.format("%.2f", readedM) + "M from " + String.format("%.2f", totalM) + "M, " + ((int)process) + "%</STRONG><BR><BR>";
		out += "Source :      " + fromURL + "<BR>";
		//out += "Download to :   " + storeFile + "<BR>";
		out += "MD5 :           " + md5 + "<BR>";
 		out += "speed :         " + ((costMS==0)?0:(avg)) + " KB/s<BR>";
		out += "cost time : " + toHHMMSS((int)(costMS / 1000)) + "<BR>";
		out += "time left :     " + toHHMMSS((int)(leftSeconds)) + "<BR>";
		
		out += "</html>";
		return out;
	}
	private static String toHHMMSS(int timeSecond){
		int hour = timeSecond / 60 / 60;
		int minute = (timeSecond - hour * 60) / 60;
		int second = timeSecond % 60;
		return (hour > 9?String.valueOf(hour):"0"+String.valueOf(hour)) + ":" + 
				(minute > 9?String.valueOf(minute):"0"+String.valueOf(minute)) + ":" + 
				(second > 9?String.valueOf(second):"0"+String.valueOf(second));
	}
	
	public synchronized boolean searchNewTask(DownloadThread dt){
		final int fastAvg = dt.getAvgSpeed();
		
		int minspeedThreadid = -1;
		int minleftSecond = 999999999;
		for (int threadId = 0; threadId < dts.length; threadId++) { 
			if(threadId != dt.threadId){
				DownloadThread dthread = dts[threadId];
				final int lefts = dthread.calLeftSecond();
				if(lefts == -1 && dthread.end > dthread.start){
					minspeedThreadid = threadId;
					break;
				}else if(lefts > 0){
					if(lefts < minleftSecond && lefts > 10){
						if(dthread.getAvgSpeed() < fastAvg){
							minleftSecond = lefts;
							minspeedThreadid = threadId;
						}
					}
				}
			}
		}
		if(minspeedThreadid == -1){
			return false;
		}
		
		//System.out.println("find min speed threadID : " + minspeedThreadid + ", has " + minleftSecond + " seconds task.");
		final DownloadThread cutDT = dts[minspeedThreadid];
		final int avgSpeed = cutDT.getAvgSpeed();
		if(avgSpeed == -1){
			if(cutDT.end > cutDT.start){
				dt.end = cutDT.end;
				dt.start = cutDT.start + cutDT.downloadBS;
				cutDT.end = cutDT.start;
				//System.out.println("move all left from ThreadID:" + cutDT.threadId + ", downloaded : " + cutDT.downloadBS);
				return true;
			}
		}else if(avgSpeed != 0){
			final int leftBS = cutDT.end - cutDT.start - cutDT.downloadBS;
			final int cutBS = leftBS * fastAvg / (avgSpeed + fastAvg);
			dt.end = cutDT.end;
			cutDT.end -= cutBS;
			//System.out.println("ThreadID : " + cutDT.threadId + " start[" + cutDT.start +"," + cutDT.end + "].");
			dt.start = cutDT.end + 1;
			//System.out.println("ThreadID : " + minspeedThreadid + " cut " + cutBS + " bytes.");
			return true;
		}
		
		return false;
	}
}
class DownloadThread extends Thread {  
    int start, end;  
    File file = null;  
    URL url = null;  
    AndroidMultiThreadDownloader main;
    long startMS;
    int downloadBS;
    final int threadId;
    boolean isError = false;
    
    public int getAvgSpeed(){
    	if(isError){
    		return -1;
    	}
    	if(downloadBS == 0){
    		return 0;
    	}
    	final int speed = (int)(downloadBS / 1024 * 1000 / (System.currentTimeMillis() - startMS));
    	//System.out.println("ThreadID : " + threadId + " avgSpeed : " + speed + "KB/s");
		return speed;
    }
    
    public int calLeftSecond(){
    	if(isError){
    		return -1;
    	}
    	if(downloadBS == 0){
    		return 0;
    	}
    	final int avgSpeed = getAvgSpeed();
    	if(avgSpeed <= 0){
    		return -1;
    	}
		final int leftBytes = end - start - downloadBS;
		final int seconds = leftBytes / 1024 / avgSpeed;
    	//System.out.println("ThreadID :" + threadId + " has left seconds:" + seconds + ", leftBytes : " + leftBytes);
    	return seconds;
    }
    
    public DownloadThread(int threadId, AndroidMultiThreadDownloader main, int start, int end, File file, URL url) {  
        this.main = main;
        this.threadId = threadId;
    	this.start = start;
    	this.end = end;
        this.file = file;  
        this.url = url;  
    }  
  
    public void run() {
    	RandomAccessFile raf = null;
    	boolean getNewTask = true;
        try {  
            raf = new RandomAccessFile(file, "rw");  

            while(getNewTask){
	            getNewTask = false;
	            //System.out.println("ThreadID : " + threadId + " starting block[" + start + "," + end + "].");
	            
	            resetSpeed();

	            downloadHttp206(raf);

            	getNewTask = main.searchNewTask(this);
            }
        } catch (Exception e) { 
        	System.err.println("Error multi-thread download source:" + url.toString());
        	isError = true;
        	main = null;
        }  
        try {
			raf.close();
		} catch (Throwable e) {
		}
    }

	public HttpURLConnection downloadHttp206(RandomAccessFile raf)
			throws IOException, ProtocolException, Exception {
    	HttpURLConnection conn = null;
		conn = (HttpURLConnection) url.openConnection();  
		conn.setRequestMethod("GET");  
//		conn.setReadTimeout(0);//无穷  
		conn.setRequestProperty("Range", "bytes=" + start + "-" + end);  
		if (conn.getResponseCode() == 206) {  
		    raf.seek(start);  
		    InputStream inStream = conn.getInputStream();  
		    byte[] b = new byte[1024 * 10];  
		    int len = 0;  
		    boolean hasIOException = false;
		    
		    do{
		    	hasIOException = false;
		    	try{
				    while ((!main.isCancel) && ((start + downloadBS) <= end) && (len = inStream.read(b)) != -1) {  
				    	raf.write(b, 0, len);  
				        synchronized (main) {
				        	main.downloadByte += len;
						}
				        downloadBS += len;
				        
//				        if(threadId == 0 && downloadBS > 1024 * 1024 * 2){
//                    		//System.out.println("ThreadID : 3 raise exception.");
//                    		throw new IOException();
//                    	}
		//	                    if(threadId == 2){
		//	                    	try{
		//	                    		Thread.sleep(100);
		//	                    	}catch (Exception e) {
		//							}
		//	                    }else if(threadId == 3){
		//	                    	if(downloadBS > 100000){
		//	                    		//System.out.println("ThreadID : 3 raise exception.");
		//	                    		throw new Exception();
		//	                    	}
		//	                    }else{
		//	                    	try{
		//	                    		Thread.sleep(20);
		//	                    	}catch (Exception e) {
		//							}
		//	                    }
				    } 
		    	}catch (IOException e) {
		    		hasIOException = true;
		    		start += downloadBS;
		    		downloadBS = 0;
		    		
		    		try{
		    			conn.disconnect();
		    		}catch (Exception ex) {
					}
		    		try{
		    			Thread.sleep(5000);
		    		}catch (Exception ex) {
					}
		    		
		    		conn = (HttpURLConnection) url.openConnection();  
		    		conn.setRequestMethod("GET");  
//		    		conn.setReadTimeout(0);//无穷  
		    		conn.setRequestProperty("Range", "bytes=" + start + "-" + end);  
	    		    inStream = conn.getInputStream();  
				}
		    }while(hasIOException);
		}else{
			throw new Exception("No 206");
		}
		conn.disconnect();
		return conn;
	}

	private void resetSpeed() {
		startMS = System.currentTimeMillis();
		downloadBS = 0;
	}  
}  