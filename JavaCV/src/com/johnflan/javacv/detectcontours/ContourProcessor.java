package com.johnflan.javacv.detectcontours;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class ContourProcessor {
	
	//CvSeq contour = new CvSeq(null);
	CvMemStorage storage = CvMemStorage.create();
	int numOfContours;
	
	public void detect(IplImage binaryImage, CvSeq contours){
		
		cvClearMemStorage(storage);
		numOfContours = cvFindContours(binaryImage, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
		System.out.println("Number of contours detected:" + numOfContours);
		
	}
	
	public void draw(IplImage rawImage, CvSeq contours){
		
		//first cvScalar value external colour of contour: Green ***
		//second cvScalar hole colour: Blue
		
		if (!contours.isNull()){
			cvDrawContours(rawImage, contours, cvScalar(0, 255, 47, 255), cvScalar(255, 0, 0, 255), 100, 1, 8);
		}
	}
}
