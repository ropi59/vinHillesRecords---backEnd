package fr.insy2s.commerce.models;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= "member")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    public Member(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "member_id")
    private Long id;
    @Column (name = "first_name")
    private String firstName;
    @Column (name = "last_name")
    private String lastName;
    private String email;
    @Column (name = "phone_number")
    private String phoneNumber;
    private String password;
    private Boolean isActive;
    @Column (name = "reset_key")
    private String resetKey;
    private String avatar;
    @OneToOne
    @JoinColumn (name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable (name ="adress_has_member",
        joinColumns = @JoinColumn(name="member_id"),
        inverseJoinColumns = @JoinColumn(name="adress_id"))
    private List<Adress> adressList;

}
