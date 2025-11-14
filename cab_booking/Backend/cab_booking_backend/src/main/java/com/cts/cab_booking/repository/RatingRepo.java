package com.cts.cab_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.cab_booking.entity.Rating;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
	public List<Rating> findByToUserIdUserId(Integer id);
	
	@Query("select avg(r.score) from Rating r where r.toUserId.userId=:userId")
	public Double getAverageRating(@Param("userId") Integer userId);
}
