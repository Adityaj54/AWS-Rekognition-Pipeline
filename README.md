# AWS-Rekognition-Pipeline
<ul>
<li>build an image recognition pipeline in AWS, using two EC2 instances, S3, SQS, and Rekognition</li>

<li>created 2 EC2 instances (EC2 A and B in the figure), with Amazon Linux AMI, that will work in parallel</li>
<li>Instance A will read images from an S3 bucket  and perform object detection in the images. When a car is detected using Rekognition, with confidence higher than 90%, the index of that image (e.g., 2.jpg) is sent to the SQS (messeging queue) </li>
  <li> 
 Instance B reads indexes of images from SQS as soon as these indexes become available in the queue, and performs text recognition on these images</li>
</ul>


![Screenshot](flowchart.PNG)


