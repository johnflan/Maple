package com.johnflan.javacv.detectcontours;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
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

}
