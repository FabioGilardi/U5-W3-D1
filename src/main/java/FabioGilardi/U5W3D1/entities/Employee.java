package FabioGilardi.U5W3D1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password", "role", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class Employee implements UserDetails {

    //    ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String username, name, surname, email, avatar, password;

    @Enumerated(EnumType.STRING)
    private EmployeeAuthorities role;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Device> deviceList;


    //  CONSTRUCTOR
    public Employee(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        setDeafaultAvatar();
        this.role = EmployeeAuthorities.USER;
    }

    //    METHODS
    public void setDeafaultAvatar() {
        this.avatar = "https://ui-avatars.com/api/?name=" + this.name + "+" + this.surname;
    }

    //    USER DETAILS OVERRIDES
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
