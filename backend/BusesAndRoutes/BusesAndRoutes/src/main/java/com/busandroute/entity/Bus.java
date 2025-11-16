package com.busandroute.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busNumber;
    private String busType;  
    private Integer totalSeats;
    private String operatorName;
	public Bus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Bus(Long id, String busNumber, String busType, Integer totalSeats, String operatorName) {
		super();
		this.id = id;
		this.busNumber = busNumber;
		this.busType = busType;
		this.totalSeats = totalSeats;
		this.operatorName = operatorName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBusNumber() {
		return busNumber;
	}
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public Integer getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}

