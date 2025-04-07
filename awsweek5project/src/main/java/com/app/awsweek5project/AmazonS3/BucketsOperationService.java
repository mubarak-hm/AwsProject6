package com.app.awsweek5project.AmazonS3;
import com.app.awsweek5project.DTO.ImageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Component
public class BucketsOperationService {
    private  final S3Config s3Config;

    @Value("${aws.s3-bucket}")
    private  String bucketName ;


    public BucketsOperationService( S3Config s3Config  ) {
        this.s3Config=s3Config;
    }


    public void  addObject(MultipartFile file, String key) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
        Optional<MultipartFile> files = Optional.ofNullable(file);


     files.ifPresentOrElse(f-> {
         try {
             s3Config.getS3Client().putObject(putObjectRequest, RequestBody.fromBytes(f.getBytes()));
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     },()-> {
      throw  new RuntimeException(" failed to find file");

     } );

    }

    public  void removeObject(String key) {
        DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
         s3Config.getS3Client().deleteObject(deleteObjectRequest);
    }

//    public List<ImageDTO> listAllObjects(){
//        ListObjectsV2Request listObjectsRequest= ListObjectsV2Request.builder().bucket(bucketName).build();
//        ListObjectsV2Response response = s3Config.getS3Client().listObjectsV2(listObjectsRequest);
//        String region = Region.EU_CENTRAL_1.id();
//        String url= "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
//        List<ImageDTO> ou=  response.contents() != null ? response.contents().stream().map(object-> new ImageDTO(object.key(),url+object.key())).toList(): Collections.emptyList();
//        System.out.println(ou);
//        return ou;
    }





