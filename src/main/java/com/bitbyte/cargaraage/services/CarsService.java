package com.bitbyte.cargaraage.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.bitbyte.cargaraage.configurations.AWSConfig;
import com.bitbyte.cargaraage.entities.Car;
import com.bitbyte.cargaraage.entities.ImageUploadData;
import com.bitbyte.cargaraage.exceptionhandlers.InvalidRequestException;
import com.bitbyte.cargaraage.repositories.CarsRepository;
import com.bitbyte.cargaraage.repositories.ImageUploadDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static java.util.UUID.randomUUID;

@Service
@Slf4j
public class CarsService {

    private final AmazonS3 amazonS3Client;
    private final AWSConfig awsConfig;
    private final ImageUploadDataRepository imageUploadDataRepository;
    private final CarsRepository carsRepository;
    private final String folderUrl;

    @Autowired
    public CarsService(AmazonS3 amazonS3Client, AWSConfig awsConfig, ImageUploadDataRepository imageUploadDataRepository, CarsRepository carsRepository) {
        this.amazonS3Client = amazonS3Client;
        this.awsConfig = awsConfig;
        this.imageUploadDataRepository = imageUploadDataRepository;
        this.carsRepository = carsRepository;
        folderUrl = awsConfig.getS3Url() + "/" + awsConfig.getS3BucketName() + "/";
    }

    public ImageUploadData uploadFile(MultipartFile file) throws IOException {
        log.info("CarsService :: uploadFile: Method start");
        String uuid = randomUUID().toString();

        log.info("CarsService :: uploadFile: Generating the fileToUpload...");
        File fileToUpload = convertMultiPartToFile(file, uuid);
        log.info("CarsService :: uploadFile: Generated the fileToUpload -> {}", fileToUpload.getName());

        log.info("CarsService :: uploadFile: Generating the thumbnail...");
        File thumbnail = createThumbnail(file, uuid);
        log.info("CarsService :: uploadFile: Generated the thumbnail -> {}", thumbnail.getName());

        String imageKey = awsConfig.getS3FolderName()+ "/" + fileToUpload.getName();
        String thumbnailKey = awsConfig.getS3FolderName()+ "/" + thumbnail.getName();

        String imageUrl = folderUrl + imageKey;
        String thumbnailUrl = folderUrl + thumbnailKey;

        log.info("CarsService :: uploadFile: Uploading the image...");
        amazonS3Client.putObject(awsConfig.getS3BucketName(), imageKey, fileToUpload);
        log.info("CarsService :: uploadFile: Saved the image in the location -> {}", imageUrl);

        log.info("CarsService :: uploadFile: Uploading the thumbnail...");
        amazonS3Client.putObject(awsConfig.getS3BucketName(), thumbnailKey, thumbnail);
        log.info("CarsService :: uploadFile: Saved the image in the location -> {}", thumbnailUrl);

        log.info("CarsService :: uploadFile: Deleting the locally saved files...");
        fileToUpload.delete();
        thumbnail.delete();
        log.info("CarsService :: uploadFile: Saving the Image Data");
        return imageUploadDataRepository.save(new ImageUploadData(false, imageUrl, thumbnailUrl));
    }

    private File convertMultiPartToFile(MultipartFile file, String uuid) throws IOException {
        String fileType = getFileType(file);
        String fileName = uuid + "." + fileType;

        return getFile(fileName, file.getBytes());
    }

    private File createThumbnail(MultipartFile file, String uuid) throws IOException{
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage thumbImg = null;
        BufferedImage img = ImageIO.read(file.getInputStream());
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 100, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, file.getContentType().split("/")[1] , thumbOutput);

        String fileType = getFileType(file);
        String fileName = uuid + "-thumbnail." + fileType;
        return getFile(fileName, thumbOutput.toByteArray());
    }

    private String getFileType(MultipartFile file) {
        String[] splitFileName = file.getOriginalFilename().split("\\.");
        return splitFileName[splitFileName.length - 1];
    }

    private File getFile(String fileName, byte[] bytes) throws IOException {

        File convertedFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(bytes);
        fos.close();
        return convertedFile;
    }

    public Car saveCar(Car car) {
        if (car.getImages() != null && !car.getImages().isEmpty()) {
            List<ImageUploadData> images = new ArrayList<>();
            for (ImageUploadData image : car.getImages()) {
                Optional<ImageUploadData> imageUploadData = imageUploadDataRepository.findById(image.getId());
                if (imageUploadData.isPresent()) {
                    imageUploadData.get().setUsed(true);
                    images.add(imageUploadData.get());
                } else throw new InvalidRequestException("Invalid image details");
            }
            car.setImages(imageUploadDataRepository.saveAll(images));
        }
        return carsRepository.save(car);
    }

    /**
     * Runs Once everyday (24 * 60 * 60 * 1000) with an initial delay of one minute
     * after the application startup.
     *
     * Deletes all the images which are created one day ago and not used
     */
    @Scheduled(fixedRate = 86400000, initialDelay = 60000)
    public void deleteUnusedImages() {
        Date today = new Date();
        log.info("CarsService :: deleteUnusedImages: Today's date -> {}", today.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        log.info("CarsService :: deleteUnusedImages: Yesterday's date -> {}", yesterday.toString());
        List<ImageUploadData> images = imageUploadDataRepository.findByUsedAndCreatedDateIsLessThanEqual(false, yesterday);
        if (images != null && !images.isEmpty()) {
            log.info("CarsService :: deleteUnusedImages: Found {} unused images", images.size());
            DeleteObjectsRequest request = new DeleteObjectsRequest(awsConfig.getS3BucketName());

            List<DeleteObjectsRequest.KeyVersion> imageKeys = new ArrayList<>();
            images.forEach(image -> {
                imageKeys.add(new DeleteObjectsRequest.KeyVersion(image.getImageUrl().replace(folderUrl, ""), null));
                imageKeys.add(new DeleteObjectsRequest.KeyVersion(image.getThumbnailUrl().replace(folderUrl, ""), null));
            });

            request.setKeys(imageKeys);
            log.info("CarsService :: deleteUnusedImages: Deleting {} images from S3", request.getKeys().size());
            DeleteObjectsResult result =  amazonS3Client.deleteObjects(request);
            result.getDeletedObjects();
            log.info("CarsService :: deleteUnusedImages: Deleted {} images from S3", result.getDeletedObjects().size());
            log.info("CarsService :: deleteUnusedImages: Deleting {} images from the database", images.size());
            imageUploadDataRepository.deleteAll(images);
            log.info("CarsService :: deleteUnusedImages: Deletion Successful");
        }
    }
}
