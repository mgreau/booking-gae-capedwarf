package com.mgreau.book.wildfly.booking.entities;

import java.io.Serializable;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Hotel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
    
	private String hotelName;
    
	private String address;
    
	private String city;
    
	private String state;
    
	private String zip;
    
	private String country;
    
    private Long price;
    
    private Text image;
    
    private Integer numberOfStars;
    
	public Hotel(){
    	
    }

    public String toString() {
        return "Hotel(" + hotelName + "," + address + "," + city + "," + zip + ")";
    }
    
    public Text getImage() {
		return image;
	}

	public Integer getNumberOfStars() {
		return numberOfStars;
	}

	public void setNumberOfStars(Integer numberOfStars) {
		this.numberOfStars = numberOfStars;
	}

	public void setImage(Text image) {
		this.image = image;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String name) {
		this.hotelName = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}
    
}
