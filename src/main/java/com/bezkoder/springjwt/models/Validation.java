package com.bezkoder.springjwt.models;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "validation")

public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Instant creation;
    private Instant expiration;
    private Instant activation;
    private String code;
    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;

    public Validation(Integer id, Instant creation, Instant expiration, Instant activation, String code, User user) {
        this.id = id;
        this.creation = creation;
        this.expiration = expiration;
        this.activation = activation;
        this.code = code;
        this.user = user;
    }

    public Validation() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public Instant getActivation() {
        return activation;
    }

    public void setActivation(Instant activation) {
        this.activation = activation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
