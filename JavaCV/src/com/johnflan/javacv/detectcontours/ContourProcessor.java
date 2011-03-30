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
	
	public void detect(IplImage binaryImage, CvSeq contour){
		
		cvFindContours(binaryImage, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
		
	}
}
