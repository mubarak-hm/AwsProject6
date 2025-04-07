package com.app.awsweek5project.AmazonS3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
@Component
public class S3Config {
    @Value("${aws.access-key}")
    private String awsAccessKey;
    @Value("${aws.secret-key}")
    private String awsSecretKey;
    public S3Client getS3Client (){
         AwsCredentials credentials = AwsBasicCredentials.create(awsAccessKey,awsSecretKey);
        System.out.println(credentials);
         return  S3Client.builder().region(Region.EU_CENTRAL_1).credentialsProvider(StaticCredentialsProvider.create(credentials)).build();

     }
}
