package com.johnflan.javacv.detectcontours;


import java.awt.Toolkit;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
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
        //!!IplImage frameRaw = cvLoadImage(
        //!!        "/home/johnflan/workspace/Maple/docs/misc_images/testLeaf.png",
        //!!        CV_LOAD_IMAGE_UNCHANGED);
        
        
        IplImage frameProcessed = cvCreateImage( cvGetSize(frameRaw), IPL_DEPTH_8U, 1 );
        
        CanvasFrame canvasFramePre = new CanvasFrame("Preprocessing");
        canvasFramePre.setCanvasSize(frameRaw.width(), frameRaw.height());
        
        CanvasFrame canvasFramePost = new CanvasFrame("Postprocessing");
        canvasFramePost.setCanvasSize(frameRaw.width(), frameRaw.height());
        
    	
    	FramePreProcessor framePreProcessor = new FramePreProcessor();
    	ContourProcessor contourProcessor = new ContourProcessor();
    	
    	CvMemStorage storage = CvMemStorage.create();
    	
    	CvSeq contourPtr = new CvSeq(null);
    	CvSeq contourLow;
    	
        
    	//Load Sample contour
    	CvSeq sampleLeaf = contourProcessor.loadTestLeafContour();
    	CvSeq sampleLeafLow = cvApproxPoly(sampleLeaf, Loader.sizeof(CvContour.class), storage,CV_POLY_APPROX_DP,1,1);
    	//contourProcessor.printContourPoints(sampleLeafLow);
        
        while (canvasFramePre.isVisible() && canvasFramePost.isVisible() &&  (frameRaw = grabber.grab()) != null) {

        	//convert to grayscale and equalise levels
        	framePreProcessor.prepareImage(frameRaw, frameProcessed);	
        	framePreProcessor.dilate(frameProcessed);
    		framePreProcessor.applyThreshold(frameProcessed, frameProcessed);
    		cvNot(frameProcessed, frameProcessed); //invert the image

    		canvasFramePost.showImage(frameProcessed);
        	//Detect and Draw contours
        	cvFindContours(cvCloneImage(frameProcessed), storage, contourPtr, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
        	
        	//Approx contours
        	contourLow = cvApproxPoly(contourPtr, Loader.sizeof(CvContour.class), storage,CV_POLY_APPROX_DP,1,1);
        	
        	
        	for (; contourLow != null; contourLow = contourLow.h_next()){
        		        		
        		//CvScalar colour = CV_RGB( Math.random() * 255,  Math.random() * 255,  Math.random() * 255 );
        		CvScalar colour = CV_RGB( 0,  255,  0 );
        		
        		//Find bounding box
        		CvRect rect;
        		CvPoint p1 = new CvPoint();
        		CvPoint p2 = new CvPoint();
        		
        		rect = cvBoundingRect(contourLow, 1);
        		
        		//If the width is less than 1/10 of the image width skip it
        		if ((frameRaw.width() * .2) > rect.width() )
        			continue;
        		
        		
        		//if (contourLow == null || sampleLeaf == null){
        		//	System.out.println("Error contourLow or sampleLeaf contain null value");
        		//	continue;
        		//}
        		
        		double matchValue = cvMatchShapes(contourLow, sampleLeafLow, CV_CONTOURS_MATCH_I1, 0);
        		//double matchValue = .1;
        		
        		if (matchValue > .05)
        			continue;
        		
        		//Draw bounding box
        		p1.x(rect.x());
        		p2.x(rect.x() + rect.width());
        		p1.y(rect.y());
        		p2.y(rect.y() + rect.height());
        		cvRectangle(frameRaw, p1, p2, colour, 2, 8, 0);
        		      		
        		//Draw contour 
        		//cvDrawContours(frameRaw, contourLow, colour, colour, -1, 0,8, cvPoint(0,0));
        		
        		
        		//----Calculate the objects features using hu moments
        		//cvMoments(contourLow, moments, 0);
        		//cvGetHuMoments(moments, humoments);
        		
    			System.out.println("Result from cvMatchShapes: " + matchValue);        		
        	}
        	     	
        	
        	canvasFramePre.showImage(frameRaw);
        	canvasFramePost.showImage(frameProcessed);
//        	cvReleaseMemStorage(storage); 
//        	storage = new CvMemStorage();
        	
        }
        
        grabber.stop();
        canvasFramePre.dispose();
        canvasFramePost.dispose();

	}
}
