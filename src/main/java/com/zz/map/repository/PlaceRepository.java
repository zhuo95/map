package com.zz.map.repository;


import com.zz.map.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.persistence.LockModeType;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,String> {

    List<Place> findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(Double la1,Double la2, Double lo1, Double lo2);

    //行锁，悲观锁
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select j from Place j where j.id = :id ")
    Place getPlaceForUpdate(@Param("id") String id);
}
