package pet.adoption.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String species;

  private int age;

  private boolean adopted = false;

  /** Path to pet photo, e.g. "uploads/pets/1.jpg" */
  private String imagePath;

  private LocalDateTime createdAt;

  @ManyToOne(optional = false)
  private Shelter shelter;

  @PrePersist
  void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean isAdopted() {
    return adopted;
  }

  public void setAdopted(boolean adopted) {
    this.adopted = adopted;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  /** Returns just the filename for the image, e.g. "1.jpg" */
  @Transient
  public String getImageFilename() {
    if (imagePath == null || imagePath.isBlank()) {
      return null;
    }
    int lastSlash = imagePath.lastIndexOf('/');
    return lastSlash >= 0 ? imagePath.substring(lastSlash + 1) : imagePath;
  }

  public Shelter getShelter() {
    return shelter;
  }

  public void setShelter(Shelter shelter) {
    this.shelter = shelter;
  }
}
