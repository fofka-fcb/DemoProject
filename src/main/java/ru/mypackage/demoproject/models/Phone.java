package ru.mypackage.demoproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_of_user", referencedColumnName = "user_id")
    private ApplicationUser user;

    @Column(name = "type")
    private String type;

    @Column(name = "phone")
    private String phone;

    @Column(name = "country_code")
    private Integer country_code;

    @Column(name = "city_code")
    private Integer city_code;

    public Phone() {
    }

    public Phone(String type, String phone, Integer country_code, Integer city_code) {
        this.type = type;
        this.phone = phone;
        this.country_code = country_code;
        this.city_code = city_code;
    }

    public Phone(Integer id, String type, String phone, Integer country_code, Integer city_code) {
        this.id = id;
        this.type = type;
        this.phone = phone;
        this.country_code = country_code;
        this.city_code = city_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user.getId();
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCountry_code() {
        return country_code;
    }

    public void setCountry_code(Integer country_code) {
        this.country_code = country_code;
    }

    public Integer getCity_code() {
        return city_code;
    }

    public void setCity_code(Integer city_code) {
        this.city_code = city_code;
    }
}
