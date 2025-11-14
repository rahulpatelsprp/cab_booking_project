package com.cts.RatingService.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RatingUserAndDriverIdDto {
    int userId;
    int toUserId;
    int fromUserId;
    int score;
    String Comment;
}