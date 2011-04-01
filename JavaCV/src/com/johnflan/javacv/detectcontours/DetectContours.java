package com.johnflan.javacv.detectcontours;


import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class DetectContours {


	public static void main(String[] args) throws Exception {
		
        //!!OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        //!!grabber.start();
        
        //!!IplImage frameRaw = grabber.grab();
        IplImage frameRaw = cvLoadImage(
                "/home/johnflan/workspace/Maple/docs/misc_images/testLeaf.png",
                CV_LOAD_IMAGE_UNCHANGED);
        
        
        IplImage frameProcessed = cvCreateImage( cvGetSize(frameRaw), IPL_DEPTH_8U, 1 );
        IplImage frameTemp; 
        
        CanvasFrame canvasFramePre = new CanvasFrame("Preprocessing");
        canvasFramePre.setCanvasSize(frameRaw.width(), frameRaw.height());
        
        CanvasFrame canvasFramePost = new CanvasFrame("Postprocessing");
        canvasFramePost.setCanvasSize(frameRaw.width(), frameRaw.height());
        
    	
    	FramePreProcessor framePreProcessor = new FramePreProcessor();
    	ContourProcessor contourProcessor = new ContourProcessor();
    	
    	boolean alternateProcessingType = false;
    	CvSeq contourPtr = new CvSeq(null);
        
        
        //!!while (canvasFramePre.isVisible() && canvasFramePost.isVisible() &&  (frameRaw = grabber.grab()) != null) {
        	
    		
        	//convert to grayscale and equalise levels
        	framePreProcessor.prepareImage(frameRaw, frameProcessed);	
        	framePreProcessor.dilate(frameProcessed);
        	
    		//alternate type of binary conversion performed on each frame
        	if (alternateProcessingType){
        		framePreProcessor.detectEdges(frameProcessed, frameProcessed);
        		alternateProcessingType = false;
        	} else {
        		framePreProcessor.applyThreshold(frameProcessed, frameProcessed);
        		cvNot(frameProcessed, frameProcessed); //invert the image
        		alternateProcessingType = true;
        	}
        	
        	
        	
        	//Detect and Draw contours
        	contourProcessor.detect(cvCloneImage(frameProcessed), contourPtr);
        	contourProcessor.draw(frameRaw, contourPtr);
        	
        	System.out.println("Contours: " + contourPtr.total());
        	
        	while(contourPtr != null){
        		frameTemp = cvCloneImage(frameRaw);
        		
        		CvRect rect = cvBoundingRect(contourPtr, 1);
        		
        		System.out.println("x: " + rect.x() + " y: " + rect.y());

        		
        		cvRectangle(
        				frameTemp, 
        				cvPoint(rect.x(), rect.y()), 
        				cvPoint(rect.width(), rect.height()), 
        				cvScalar(0, 0, 255, 255), 
        				2,
        				CV_AA, 
        				2
        		);
        		
        		canvasFramePre.showImage(frameTemp);
        		contourPtr = contourPtr.h_next();
        	}
        	
        	
        	
        	
        	//Approximate the contour
        	//CvMemStorage polyStorage = CvMemStorage.create();
        	//cvApproxPoly(
        	//		contourPtr,						//sequence of contours
        	//		Loader.sizeof(CvContour.class),
        	//		null, //polyStorage, 			//Container for approximated contours. If it is NULL, the input sequences' storage is used.
        	//		CV_POLY_APPROX_DP,				//Type of approx
        	//		10,							//precision of algo
        	//		2								//0 for first contour 1 for all contours
        	//);
        	

        	
        	//if 
        	//System.out.println("Contours: " + contourPtr.fir);
        	
        	canvasFramePre.showImage(frameRaw);
        	canvasFramePost.showImage(frameProcessed);

        //!!}
        
        //!!grabber.stop();
        cvWaitKey(40000);
        canvasFramePre.dispose();
        canvasFramePost.dispose();

	}

	private static void draw2Dbox(IplImage image, CvBox2D box, CvScalar  colour) {

		//cvEllipseBox(image.clone(), box, colour, 2, CV_AA, 2);
		
	}
	

}
