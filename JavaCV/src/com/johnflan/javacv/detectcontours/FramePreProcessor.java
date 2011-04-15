package com.johnflan.javacv.detectcontours;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class FramePreProcessor {
	
    // Kernel size
	int N = 7;
    
    // Edge Detection Variables
	int aperature_size = N;
	double lowThresh = 400;
	double highThresh = 1255;
	
	public void prepareImage(IplImage inFrame, IplImage outFrame){

		cvCvtColor(inFrame, outFrame, CV_BGR2GRAY);
		cvEqualizeHist( outFrame, outFrame );
		
	}
	
	
	public void detectEdges(IplImage inFrame, IplImage outFrame){

		// Edge Detection
		cvCanny( inFrame, outFrame, lowThresh*N*N, highThresh*N*N, aperature_size );
		
	}
	
	
	public void applyThreshold(IplImage inFrame, IplImage outFrame){

		// do some threshold for wipe away useless details
        //cvThreshold(inFrame, outFrame, 64, 255, CV_THRESH_BINARY);
        //cvAdaptiveThreshold(inFrame, outFrame, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 71, 15);
		//500 contracts the selected area
		cvAdaptiveThreshold(inFrame, outFrame, 500, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 255, 5);
		
	}


	public void dilate(IplImage frameProcessed) {
		
		cvDilate(frameProcessed, frameProcessed, null, 2);
		
	}

}
