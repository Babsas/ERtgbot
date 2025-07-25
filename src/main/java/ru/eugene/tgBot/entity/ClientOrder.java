package ru.eugene.tgBot.entity;

import jakarta.persistence.*;

@Entity
public class ClientOrder {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Client client;

    @Column (nullable = false)
    private Integer status;

    @Column (nullable = false)
    private Double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
