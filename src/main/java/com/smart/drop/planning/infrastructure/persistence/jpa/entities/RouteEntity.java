package com.smart.drop.planning.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Routes")
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RouteId")
    private Integer routeId;

    @Column(name = "Origin", nullable = false, length = 255)
    private String origin;

    @Column(name = "Destination", nullable = false, length = 255)
    private String destination;

    @Column(name = "DistanceKm", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "EstimatedTime", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AssignedCarrierId")
    private CarrierEntity assignedCarrier;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public BigDecimal getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(BigDecimal estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public CarrierEntity getAssignedCarrier() {
        return assignedCarrier;
    }

    public void setAssignedCarrier(CarrierEntity assignedCarrier) {
        this.assignedCarrier = assignedCarrier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

