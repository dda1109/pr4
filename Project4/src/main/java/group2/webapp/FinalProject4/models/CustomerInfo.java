package group2.webapp.FinalProject4.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "CustomerInfo")
public class CustomerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bithday;
    private String phone;
    private String email;
    private String linkAVT;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CustomerInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getBithday() {
        return bithday;
    }

    public void setBithday(Date bithday) {
        this.bithday = bithday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkAVT() {
        return linkAVT;
    }

    public void setLinkAVT(String linkAVT) {
        this.linkAVT = linkAVT;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
