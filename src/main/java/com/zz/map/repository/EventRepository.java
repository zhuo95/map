package com.zz.map.repository;

import com.zz.map.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
    Page<Event> findAllByPlaceId(Long placeId, Pageable pageable);
}
