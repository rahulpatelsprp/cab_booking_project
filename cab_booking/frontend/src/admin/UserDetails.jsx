import React, { useState, useEffect } from "react";
import { allUserDetails } from "../api/UserController";
import styles from "./adminDashboard.module.css";
import { FiEdit, FiTrash2 } from "react-icons/fi";

const UserDetails = () => {
  const [users, setUsers] = useState({});

  useEffect(() => {
    allUserDetails()
      .then((res) => {
        const filteredUsers = res.data.filter((user) => user.role === "USER");
        setUsers(filteredUsers);
        console.log(res.data);
      })
      .catch((err) => console.error("Failed to fetch users:", err));
  }, []);

  return (
    <div>
      <h2>User Details</h2>
      {users.length > 0 ? (
        <div className={styles.profileCardContainer}>
          {users.map((user) => {
            const userImageUrl = `https://picsum.photos/seed/${user.userId}/90/90`;

            return (
              <div key={user.userId} className={styles.profileCard}>
                <img
                  src={userImageUrl}
                  alt={`${user.name}'s profile`}
                  className={styles.profileImage}
                />
                <h3 className={styles.profileName}>{user.name}</h3>
                <p className={styles.profileDetail}>{user.email}</p>
                <p className={styles.profileDetail}>
                  <strong>ID:</strong> {user.userId}
                </p>
                <div className={styles.profileButtons}></div>
              </div>
            );
          })}
        </div>
      ) : (
        <p>Loading users...</p>
      )}
    </div>
  );
};

export default UserDetails;
