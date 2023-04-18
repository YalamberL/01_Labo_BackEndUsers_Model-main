package demo;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/users")
public class UserRestController {


    @Autowired
    private UserService userService;


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // @GetMapping("/oldest")
    // public User getOldestUser() {
    //     return userService.getOldestUser();
    // }

    @GetMapping("/search/olderthan")
    public List<User> searchUsersWithAgeOlderThan(@RequestParam("age") int age) {
        return userService.getUsersWithAgeOlderThan(age);
    }

    @GetMapping("/search/{name}")
    public User searchUserWithName(@PathVariable("name") String name) {
        return userService.getUserWithName(name);
    }

    @GetMapping("/adults")
    public List<User> getAllAdulUsers(){
        return userService.getUsersWithAgeOlderThan(17);
    }

    @GetMapping("/search/email/{email}")
    public User searchUserByEmail(@PathVariable("email") String email){
        return userService.getUserWithEmail(email);
    }


    // @GetMapping("/search")
    // public List<User> searchAllUsersWithEmailAndAge(@RequestParam("email") String email, @RequestParam("age") int age){
    //     List<User> foundUsers = new ArrayList<User>();
    //     foundUsers.add(userService.getUserWithEmail(email));
    //     foundUsers.add(userService.getUserWithAge(age));

    //     return foundUsers;
    // }

    @GetMapping("/search/age/{min}/{max}")
    public List<User> searchUsersBetweenGivenAge(@PathVariable("min") int min, @PathVariable("max") int max){
        return userService.getUsersWithAgeBetween(min, max);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user){
        return userService.addUser(user);
    }

    @DeleteMapping("/{email}")
    public User deleteUser(@PathVariable("email") String email){
        return userService.removeUser(email);
    }

    @DeleteMapping("/{id}")
    public User deleteUserById(@PathVariable("id") Long id){
        return userService.removeUserById(id).get();
    }
}