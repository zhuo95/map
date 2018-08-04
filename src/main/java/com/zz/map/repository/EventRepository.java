package com.zz.map.repository;

import com.zz.map.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findAllByPlaceIdAndExpireTimeAfterAndStatus(String placeId, Date date,int status);

    Page<Event> findAllByCategoryAndExpireTimeAfterAndStatus(Integer category,Date date,int status ,Pageable pageable);

    List<Event> findAllByUserIdAndStatus(Long userId,int status);
}
