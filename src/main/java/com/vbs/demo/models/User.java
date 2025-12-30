package com.vbs.demo.models;
//ye model hai, table banata hai database mei
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //isse table banega
@Data //getter setter keliye
@AllArgsConstructor //data constructer se save karne keliye
@NoArgsConstructor
public class User {
    //table ke columns: and unke properties (jo annotations mei hai) unke just upar:
    @Id        //ye primary key banayega
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //ye automatic number generate karne ko
    @Column(nullable = false)    //empty nahi honna chahiye
    int id;
    @Column(nullable = false,unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false,unique = true)
    String email;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String role;
    @Column(nullable = false)
    double balance;
}
