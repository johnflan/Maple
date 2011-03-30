package com.johnflan.javacv.detectcontours;


import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class DetectContours {


	public static void main(String[] args) throws Exception {
		
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();
        
        IplImage frameRaw = grabber.grab();
        IplImage frameProcessed = cvCreateImage( cvGetSize(frameRaw), IPL_DEPTH_8U, 1 );
        
        CanvasFrame canvasFramePre = new CanvasFrame("Preprocessing");
        canvasFramePre.setCanvasSize(frameRaw.width(), frameRaw.height());
        
        CanvasFrame canvasFramePost = new CanvasFrame("Postprocessing");
        canvasFramePost.setCanvasSize(frameRaw.width(), frameRaw.height());
    	
    	FramePreProcessor framePreProcessor = new FramePreProcessor();
    	ContourProcessor contourProcessor = new ContourProcessor();
    	
    	CvSeq contour = new CvSeq(null);
        
        
        while (canvasFramePre.isVisible() && canvasFramePost.isVisible() &&  (frameRaw = grabber.grab()) != null) {

        	//display unprocessed image
        	canvasFramePre.showImage(frameRaw);
        	
        	//convert to grayscale and equalise levels
        	framePreProcessor.prepareImage(frameRaw, frameProcessed);
        	
        	System.out.println("RAW image size: " + frameRaw.imageSize() + ", Processed image size: " + frameProcessed.imageSize());
        	
    		//create binary image
        	//framePreProcessor.detectEdges(frameProcessed, frameProcessed);
        	framePreProcessor.applyThreshold(frameProcessed, frameProcessed);
        	
        	
        	
        	contourProcessor.detect(frameProcessed, contour);
    		
    		
            canvasFramePost.showImage(frameProcessed);
            

        }
        
        grabber.stop();
        canvasFramePre.dispose();
        canvasFramePost.dispose();

	}
	

}
