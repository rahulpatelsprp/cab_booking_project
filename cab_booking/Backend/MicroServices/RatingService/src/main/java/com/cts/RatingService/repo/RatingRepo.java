package com.cts.RatingService.repo;


import com.cts.RatingService.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Integer> {

    @Query(value = "select avg(r.score) from Rating r where r.toUserId=:userId")
     Double getAverageRating(@Param("userId") Integer userId);}
