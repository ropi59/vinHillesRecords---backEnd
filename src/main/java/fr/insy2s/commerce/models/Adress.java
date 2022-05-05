package fr.insy2s.commerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= "adress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "adress_id")
    private Long id;
    private String streetNumber;
    private String streetName;
    @Column (name = "zip_code")
    private String zipCode;
    private String city;
    private String country;

    @ManyToMany(mappedBy = "adressList")
    @JsonIgnore
    private List<Member> memberList;


    public Adress(Long id, String streetNumber, String street, String zipCode, String city, String country) {
        this.id = id;
        this.streetNumber = streetNumber;
        this.streetName = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }
}
