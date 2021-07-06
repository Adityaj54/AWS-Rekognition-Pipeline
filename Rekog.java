package com.amazonaws.aws;

import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
 
public class Rekog {

 public static void main(String[] args) throws Exception {

	 String bucket_name = "njit-cs-643";
     System.out.format("Objects in S3 bucket %s:\n", bucket_name);
     BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
    		   "ASIA3NQGKQV4LQBHWXUN",
    		   "LVEDOzlCVPDi7YtngV/I7dKHrX03QSOnpd6cvn/J",
    		   "FwoGZXIvYXdzELH//////////wEaDLBwLlH7yj27N87FliK+AZEz3nrSnpfBmziSPtGtLcBntdVAKfaFTMAM/CQPO2qPwwVVf1w4EZ++o7H2qGRCoZ6DvJIBw1xruykeUjyrZ7Y4DclaW7X9QNHucQBmj7n+zvmGz79oaTtMmhMt15CY+89MnBJpVoLjsb/TKo8lMNuLSxRPANI9OP0WcyqskQmIK0eMrTbhkTnf3XnD9nW9G5Z02y/8mAdQT2CnBLGi/cxzORG2H1oxncc0lAEbbTBhtTczaIbOC5CJ+PXqm9EowuXnhgYyLRyaXSYluc55raYJpkJcBM7Y7yrRMkU24uAWxw3QemvnNPn2po77mHwfRL/67w==");

    		
     final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(sessionCredentials)).withRegion(Regions.US_EAST_1).build();
     ListObjectsV2Result result = s3.listObjectsV2(bucket_name);
     List<S3ObjectSummary> objects = result.getObjectSummaries();
     for (S3ObjectSummary os : objects) {
    	 String Key =  os.getKey();
    	
     


    AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-1").build();

  
	DetectLabelsRequest request = new DetectLabelsRequest()
         .withImage(new Image()
         .withS3Object(new S3Object()
         .withName(Key).withBucket(bucket_name)))
         .withMaxLabels(3)
         .withMinConfidence(95F);

    try {
       DetectLabelsResult result1 = rekognitionClient.detectLabels(request);
       List <Label> labels = result1.getLabels();

       

       for (Label label: labels) {
    	  if(label.getName().contains("Car")) {
    	  
    		  sendIndex(Key);
          System.out.println("Successfully Sent key "+Key+" for TextRekog");
          break;
          
    	  }
    	  
    	  
       }
       
    } catch(AmazonRekognitionException e) {
       e.printStackTrace();
    }}
 }
 
 public static void sendIndex(String Key) {
	 
	 
	 AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-1").build();	
		String queueUrl = sqs.getQueueUrl("myQueue").getQueueUrl();
	
		SendMessageRequest send_msg_request = new SendMessageRequest()
		        .withQueueUrl(queueUrl)
		        .withMessageBody(Key)
		        .withDelaySeconds(15);
		sqs.sendMessage(send_msg_request);
		
 }
}