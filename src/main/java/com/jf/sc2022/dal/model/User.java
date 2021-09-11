package com.jf.sc2022.dal.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long    id;
    @Column(unique = true, nullable = false)
    private String  username;
    @Column(nullable = false)
    private String  password;
    @Column(unique = true, nullable = false)
    private String  email;
    private String  firstName;
    private String  lastName;
    private boolean isEnabled;

//    @OneToMany
//    private List<Payment>      transactionHistory;
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<ImageListing> listings;
//    private boolean            isEnabled;
}
