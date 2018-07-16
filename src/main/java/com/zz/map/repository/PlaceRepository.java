package com.zz.map.repository;

import com.zz.map.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Long> {

    List<Place> findAllByLatitudeBetweenAndLongitudeBetween(Double la1, Double la2, Double lo1, Double lo2);

    //表锁，悲观锁
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Place findByLatitudeAndLongitude( Double latitude, Double longitude);
}