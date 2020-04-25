package com.bitbyte.cargaraage.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "image_data")
public class ImageUploadData {
    @Id
    private String id;
    private boolean used;
    private String imageUrl;
    private String thumbnailUrl;
    @CreatedDate
    private Date createdDate;

    public ImageUploadData(boolean used, String imageUrl, String thumbnailUrl) {
        this.used = used;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
