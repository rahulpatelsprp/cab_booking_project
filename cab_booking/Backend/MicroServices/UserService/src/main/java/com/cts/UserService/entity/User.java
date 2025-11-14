package com.cts.UserService.entity;



import com.cts.UserService.dto.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"email","phone"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserId")
    private int userId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email",unique = true)
    private String email;

    @Column(name = "Phone")
    private long phone;
    
    @Column(name="role")
    private Role role;

    @Column(name = "PasswordHash")
    private String passwordHash;

}
