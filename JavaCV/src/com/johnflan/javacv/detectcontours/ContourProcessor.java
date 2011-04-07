package com.johnflan.javacv.detectcontours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.CvSeqReader;
import com.googlecode.javacv.cpp.opencv_core.CvSeqWriter;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class ContourProcessor {
	
	//CvSeq contour = new CvSeq(null);
	int numOfContours;
	
	public void detect(IplImage binaryImage, CvSeq contours, CvMemStorage storage){
		
		cvClearMemStorage(storage);
		//numOfContours = cvFindContours(binaryImage, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
		numOfContours = cvFindContours(binaryImage, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
		
		System.out.println("Number of contours detected:" + numOfContours);
		
	}
	
	public void draw(IplImage rawImage, CvSeq contours){
		
		//first cvScalar value external colour of contour: Green ***
		//second cvScalar hole colour: Blue
		
		if (!contours.isNull()){
			cvDrawContours(rawImage, contours, cvScalar(0, 255, 47, 255), cvScalar(255, 0, 0, 255), 100, 1, 8);
		}
	}
	
	public CvSeq loadContours(CvMemStorage storage){

		CvPoint p1 = new CvPoint();
		CvPoint p2 = new CvPoint(); 
		CvPoint p3 = new CvPoint();
		CvPoint p4 = new CvPoint(); 
		CvSeqWriter writer = new CvSeqWriter(); 
        cvStartWriteSeq(0, Loader.sizeof(CvSeq.class), Loader.sizeof(CvPoint.class), storage, writer); 
        
        //Load points from file
        {
        	
        	p1.x(1); p1.y(1);
        	p2.x(300); p2.y(300);
        	p3.x(320); p3.y(260);
        	p4.x(1); p4.y(1);
        	
        	CV_WRITE_SEQ_ELEM(p1, writer);
        	CV_WRITE_SEQ_ELEM(p2, writer);
        	CV_WRITE_SEQ_ELEM(p3, writer);
        	CV_WRITE_SEQ_ELEM(p4, writer);
        }
        
        
		
		return cvEndWriteSeq( writer); 
	}
	
	public CvSeq loadTestLeafContour(){
		
		IplImage frameRaw = cvLoadImage(
                "/home/johnflan/workspace/Maple/docs/misc_images/testLeaf.png",
                CV_LOAD_IMAGE_UNCHANGED);
		
		IplImage frameProcessed = cvCreateImage( cvGetSize(frameRaw), IPL_DEPTH_8U, 1 );
		
		CvMemStorage storage = CvMemStorage.create();
		CvSeq contour = new CvSeq();
		
		FramePreProcessor framePreProcessor = new FramePreProcessor();
		
		framePreProcessor.prepareImage(frameRaw, frameProcessed);	
    	framePreProcessor.dilate(frameProcessed);
    	
    	framePreProcessor.applyThreshold(frameProcessed, frameProcessed);
		cvNot(frameProcessed, frameProcessed); //invert the image
		
		cvFindContours(cvCloneImage(frameProcessed), storage, contour, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
		
		//cvApproxPoly(contourPtr, Loader.sizeof(CvContour.class), storage,CV_POLY_APPROX_DP,1,1);
		
		return contour;
		
	}
	public void printContourPoints(CvSeq contour){
		
		//write out the each contour - so we can save it
		CvPoint point = new CvPoint();
		CvSeq extractSeq = contour;
		CvSeqReader cvSeqReader = new CvSeqReader();
		cvStartReadSeq(extractSeq, cvSeqReader, 0);
		for(int i = 0; i< extractSeq.total(); i++){
			CV_READ_SEQ_ELEM(point, cvSeqReader);
			System.out.println(point);
		}
		
	}

	public Map<String, CvSeq> loadTestLeaves() {
		Map<String, CvSeq> leafSet = new HashMap<String, CvSeq>();
		Map<String, String>  fileNames = new HashMap<String, String>();

		fileNames.put("Maple", "/home/johnflan/workspace/Maple/docs/misc_images/testimg/leaf.png");
		//fileNames.put("Green Ash", "/home/johnflan/workspace/Maple/docs/misc_images/testimg/green_ash.png");
		//fileNames.put("Horse Chestnut Buckeye", "/home/johnflan/workspace/Maple/docs/misc_images/testimg/horse_chestnut_buckeye.png");
		//fileNames.put("White Birch", "/home/johnflan/workspace/Maple/docs/misc_images/testimg/white_birch.png");
		//fileNames.put("Twig", "/home/johnflan/workspace/Maple/docs/misc_images/testimg/twig1.png");
		
		
		Iterator it = fileNames.entrySet().iterator();
		
		while (it.hasNext()){
			Map.Entry<String, String> file = (Map.Entry) it.next();
			
			IplImage frameRaw = cvLoadImage(file.getValue(), CV_LOAD_IMAGE_UNCHANGED);
			
			if (frameRaw.isNull())
				continue;
			
			IplImage frameProcessed = cvCreateImage( cvGetSize(frameRaw), IPL_DEPTH_8U, 1 );
			
			CvMemStorage storage = CvMemStorage.create();
			CvSeq contour = new CvSeq();
			
			FramePreProcessor framePreProcessor = new FramePreProcessor();
			
			framePreProcessor.prepareImage(frameRaw, frameProcessed);	
	    	framePreProcessor.dilate(frameProcessed);
	    	
	    	framePreProcessor.applyThreshold(frameProcessed, frameProcessed);
			cvNot(frameProcessed, frameProcessed); //invert the image
			
			cvFindContours(cvCloneImage(frameProcessed), storage, contour, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
			
			CvScalar colour = CV_RGB( 0,  255,  0 );
			CanvasFrame canvasFramePre = new CanvasFrame("Loading");
	        canvasFramePre.setCanvasSize(frameRaw.width(), frameRaw.height());
	        cvDrawContours(frameRaw, contour, colour, colour, -1, 0,8, cvPoint(0,0));
	        canvasFramePre.showImage(frameRaw);
	        canvasFramePre.dispose();

			if (!contour.isNull())
				leafSet.put(file.getKey(), contour);

		}
		
		


		return leafSet;
	}

}
