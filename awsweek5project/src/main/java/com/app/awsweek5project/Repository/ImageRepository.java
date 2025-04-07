package com.app.awsweek5project.Repository;

import com.app.awsweek5project.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long > {
    void deleteByName(String name);
    Image findByName(String name);
}
