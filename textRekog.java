package com.amazonaws.samples;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class textRekog {

	public static  void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-1").build();	
		String queueUrl = sqs.getQueueUrl("myQueue").getQueueUrl();
		 ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		 receiveMessageRequest.setMaxNumberOfMessages(7);
		 receiveMessageRequest.setWaitTimeSeconds(20);
		boolean flag=true;
		while(flag) {
		 List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();       
		 
		 for (Message label : messages) {
			
			//System.out.println("Key "+label.getBody());
			GetText(label.getBody());
			sqs.deleteMessage(queueUrl, label.getReceiptHandle());
		}
	
		
	}}
	
	
	public static void GetText(String photo) throws IOException {
		
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-1").build();
		DetectTextRequest request = new DetectTextRequest()
	              .withImage(new Image()
	              .withS3Object(new S3Object()
	              .withName(photo)
	              .withBucket("njit-cs-643")));
	      try {
	          DetectTextResult result = rekognitionClient.detectText(request);
	          List<TextDetection> textDetections = result.getTextDetections();
	          FileWriter myWriter = new FileWriter("output.txt",true);
	          
	          for (TextDetection text: textDetections) {
	        	  		if(text.getDetectedText()!=null) {
	        	  			 myWriter.write("\n\nDetected Text for " + photo +" is:\n\n Text:"+   text.getDetectedText()+ "\n\nConfidence:" +  text.getConfidence().toString()+"\n\n");
	                  System.out.println("written"+photo);
	                  
	                  break;}
	          }
	          myWriter.close();
	       } catch(AmazonRekognitionException e) {
	          e.printStackTrace();
	       }
	}
}
