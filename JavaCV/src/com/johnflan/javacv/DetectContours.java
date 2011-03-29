package com.johnflan.javacv;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class DetectContours {


	public static void main(String[] args) throws Exception {
		
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();
        
        IplImage imgFrame = grabber.grab();
        IplImage imgGray = cvCreateImage( cvGetSize(imgFrame), IPL_DEPTH_8U, 1 );
        
        CanvasFrame canvasFramePre = new CanvasFrame("Preprocessing");
        canvasFramePre.setCanvasSize(imgFrame.width(), imgFrame.height());
        
        CanvasFrame canvasFramePost = new CanvasFrame("Postprocessing");
        canvasFramePost.setCanvasSize(imgFrame.width(), imgFrame.height());
        
        // Kernel size
    	int N = 7;
        
        // Edge Detection Variables
    	int aperature_size = N;
    	double lowThresh = 10;
    	double highThresh = 50;
        
        
        while (canvasFramePre.isVisible() && canvasFramePost.isVisible() &&  (imgFrame = grabber.grab()) != null) {

        	cvCvtColor(imgFrame, imgGray, CV_BGR2GRAY);
        	canvasFramePre.showImage(imgGray);
        	
        	// Perform histogram equalization
    		cvEqualizeHist( imgGray, imgGray );
        	
        	// Edge Detection
    		//cvCanny( imgGray, imgGray, lowThresh*N*N, highThresh*N*N, aperature_size );
    		
    		//cvSmooth(imgGray, imgGray, CV_GAUSSIAN, 9, 9, 2, 2);
    		
    		
            canvasFramePost.showImage(imgGray);

        }
        
        grabber.stop();
        canvasFramePre.dispose();
        canvasFramePost.dispose();cvCvtColor(imgFrame, imgGray, CV_BGR2GRAY);

	}
	

}
