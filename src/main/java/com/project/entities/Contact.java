package com.project.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cId;

    private String name;

    private String lastName;

    private String work;

    private String email;

    private String phone;

    private String image;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    private User user;

    public Contact() {
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return lastName;
    }

    public void setSecondName(String secondName) {
        this.lastName = secondName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return cId == contact.cId && Objects.equals(name, contact.name) && Objects.equals(lastName, contact.lastName) && Objects.equals(work, contact.work) && Objects.equals(email, contact.email) && Objects.equals(phone, contact.phone) && Objects.equals(image, contact.image) && Objects.equals(description, contact.description) && Objects.equals(user, contact.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cId, name, lastName, work, email, phone, image, description, user);
    }
}
