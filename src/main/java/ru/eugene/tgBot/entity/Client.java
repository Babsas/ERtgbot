package ru.eugene.tgBot.entity;
import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    @Column (unique=true)
    private Long externalId;

    @Column (nullable = false, length = 255)
    private String fullName;

    @Column (nullable = false, length = 15)
    private String phoneNumber;

    @Column (nullable = false, length = 400)
    private String address;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
