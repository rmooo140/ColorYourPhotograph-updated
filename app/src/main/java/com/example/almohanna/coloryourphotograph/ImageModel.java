package com.example.almohanna.coloryourphotograph;

public class ImageModel {
  byte[] image;
  String imageId;

  public ImageModel(byte[] image, String imageId) {
    this.image = image;
    this.imageId = imageId;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }
}
