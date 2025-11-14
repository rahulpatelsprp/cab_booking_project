import React, { useEffect, useState } from 'react';
import UserContainer from '../../common/UserContainer';
import Header from '../../common/Header';
import { getRatings } from '../../api/RatingController';
export default function RatingHistory() {

  const [reviews,setReviews]=useState();
  useEffect(()=>{
    getRatings().then(resp=>setReviews([...resp.data]))
  },[])
  const renderStars = (rating) => {
    const fullStars = '★'.repeat(rating);
    const emptyStars = '☆'.repeat(5 - rating);
    return (
      <span className="text-yellow-400" aria-label={`${rating} out of 5 stars`}>
        {fullStars}
        <span className="text-gray-300">{emptyStars}</span>
      </span>
    );
  };

  if (!reviews || reviews.length === 0) {
    return (
      <UserContainer>
        <Header/>
      <div className="max-w-xl mx-auto p-4 text-center text-gray-500">
        No reviews yet.
      </div>
      </UserContainer>
    );
  }
  return (
    <UserContainer>
      <Header/>
    <div className="max-w-2xl mx-auto p-4 sm:p-6 font-sans">
      <h2 className="text-2xl font-semibold text-center mb-6 pb-3 border-b-2 border-gray-100 text-gray-800">
        Feedbacks
      </h2>
      <div className="space-y-4">
        {reviews.map((review) => (
          <div
            key={review.ratingId}
            className="bg-white p-5 rounded-lg border-1 border-neutral-300"
          >
            <div className="flex items-center mb-2">
              {renderStars(review.score)}
              <span className="ml-3 text-sm font-medium text-gray-600">
                ({review.score}/5)
              </span>
            </div>
            <p className="text-gray-700 leading-relaxed italic">
              "{review.comment}"
            </p>
          </div>
        ))}
      </div>
    </div>
    </UserContainer>
  );
}