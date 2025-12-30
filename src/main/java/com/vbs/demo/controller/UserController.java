package com.vbs.demo.controller;

import com.vbs.demo.dto.DisplayDto;
import com.vbs.demo.dto.LoginDto;
import com.vbs.demo.dto.UpdateDto;
import com.vbs.demo.models.History;
import com.vbs.demo.models.Transaction;
import com.vbs.demo.models.User;
import com.vbs.demo.repositories.HistoryRepo;
import com.vbs.demo.repositories.TransactionRepo;
import com.vbs.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.TabableView;
import java.util.List;

//#Ye manager jo request accept karega
@RestController //ye system ko batane keliye ki ye Manager hai
@CrossOrigin(origins =  "*") //ek port se dusre port communication karne keliye
public class UserController {
    @Autowired //Interface ka obj banane keliye
    UserRepo userRepo; //Manager ko majdur ka access dene ko
    @Autowired
    HistoryRepo historyRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @PostMapping("/register") //Request ke condition(Post,/register) satisfy karne ko
    public String register(@RequestBody User user) //Class ka obj pass kiya
    {
        userRepo.save(user); //majdur ko bulake data pass kardiya
        return "Signup Successful"; //credit khaliya
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginDto u)
    {
        User user = userRepo.findByUsername(u.getUsername());
        if(user == null)
        {
            return "User not found";
        }
        if(!u.getPassword().equals(user.getPassword()))
        {
            return "Incorrect Password";
        }
        if(!u.getRole().equals(user.getRole()))
        {
            return "Incorrect Role";
        }
        return String.valueOf(user.getId());
    }

    @GetMapping("/get-details/{id}")   //get use kiya cause khali database se lenay
    public DisplayDto display(@PathVariable int id)  //return datatype struct jaisay and ek hi vastu hai toh int id likh diya
    {
        User user = userRepo.findById(id).orElseThrow(()->new RuntimeException("User Not Found")); //Id primary key toh izzat de
        DisplayDto displaydto = new DisplayDto();
        displaydto.setUsername(user.getUsername());
        displaydto.setBalance(user.getBalance());

        return displaydto;
    }

    @PostMapping("/update")
    public String update(@RequestBody UpdateDto obj)
    {
        User user = userRepo.findById(obj.getId()).orElseThrow(()-> new RuntimeException("Not Found"));
        if(obj.getKey().equalsIgnoreCase("name"))
        {
            if(user.getName().equals(obj.getValue())) return "Cannot Be Same";
            user.setName(obj.getValue());
        }
        else if(obj.getKey().equalsIgnoreCase("password"))
        {
            if(user.getPassword().equals(obj.getValue()))  return "Cannot Be Same";
            user.setPassword(obj.getValue());
        }
        else if(obj.getKey().equalsIgnoreCase("email"))
        {
            if(user.getEmail().equals(obj.getValue())) return "Cannot Be Same";
            User user2 = userRepo.findByEmail(obj.getValue());
            if(user2 != null) return "Email Already Exists";
            user.setEmail(obj.getValue());
        }
        else
        {
            return "Invalid Key";
        }
        userRepo.save(user);
        return "Updated Successfully";
    }

    @PostMapping("/add/{adminId}")
    public String add(@RequestBody User user,@PathVariable int adminId)
    {
        History h1 = new History();
        h1.setDescription("Admin "+adminId+" Created User "+user.getUsername());
        historyRepo.save(h1);
        userRepo.save(user);
        if(user.getBalance()>0)
        {
            Transaction t = new Transaction();
            t.setAmount(user.getBalance());
            t.setCurrBalance(user.getBalance());
            t.setDescription("Rs "+user.getBalance()+" Deposited Successfully");
            t.setUserId(user.getId());
            transactionRepo.save(t);
        }
        return "Added Successfully";
    }

    @GetMapping("/users")
    public List<User> getAllUsers(@RequestParam String sortBy,@RequestParam String order) //requestpara is for optional stuff , agar hua toh thode changes , nhi hua toh default wala chalega par chalega
    {
        Sort sort;
        if(order.equals("desc"))
        {
            sort = Sort.by(sortBy).descending();
        }
        else
        {
            sort = Sort.by(sortBy).ascending();
        }
        return userRepo.findAllByRole("customer",sort);
    }

    @GetMapping("/users/{keyword}")
    public List<User> search(@PathVariable String keyword)
    {
        return userRepo.findByUsernameContainingIgnoreCaseAndRole(keyword,"customer");
    }

    @DeleteMapping("delete-user/{userId}/admin/{adminId}")
    public String delete(@PathVariable int userId,@PathVariable int adminId)
    {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("Not Found"));
        if(user.getBalance()>0)
        {
            return "Balance Should be Zero";
        }
        userRepo.delete(user);

        History h1 = new History();
        h1.setDescription("Admin "+adminId+" Deleted User "+user.getUsername());
        historyRepo.save(h1);

        return  "User Deleted Successfully";
    }
}
