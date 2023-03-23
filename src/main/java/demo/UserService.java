package demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    
    @Autowired
    private UserRepository userRepository;
    //jhon.addMembershipYear("1999");

    // private List<User> userRepository = new ArrayList<>();

    public UserService() {
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // public List<User> getUsersWithAgeOlderThan(int age) {
    //     return userRepository.findAll().stream().filter(user -> user.getAge()>age).toList();
    // }

    public List<User> getUsersWithAgeOlderThan(int age) {
        return userRepository.findUsersByAgeAfter(age);
    }

    // public User getOldestUser() {
    //     User oldest = null;
    //     if (userRepository.size()>0) {
    //         oldest = userRepository.get(0);
    //         for (User user : userRepository) {
    //             if (user.getAge() > oldest.getAge())
    //                 oldest = user;
    //         }
    //     }
    //     return oldest;
    // }

    public User getOldestUser(){
        User oldest = null;
        if (userRepository.findAll().size() > 0){
            oldest = userRepository.findAll().get(0);
            for (User user : userRepository.findAll()) {
                if (user.getAge() > oldest.getAge()){
                    oldest = user;
                }
            }
        }
        return oldest;
    }

    public User getUserWithName(String name) {
        return userRepository.findAll().stream().filter(user -> user.getName().equals(name)).toList().get(0);
    }

    public User getUserWithEmail(String email){
        // for (int i = 0; userRepository.size() > i; i++){
        //     if (userRepository.get(i).getEmail() == email){
        //         return userRepository.get(i);
        //     }
        // }
        // return null;
        return userRepository.findUserByEmail(email);
    }

    public List<User> getUsersWithAgeBetween(int min, int max){
        return userRepository.findAll().stream().filter(user -> user.getAge()>min && user.getAge()<max).toList();
    }

    public User addUser(User user) {
        User userFound = userRepository.save(user);
        return userFound;
    }

    public User removeUser(String email){ 
        User foundUser = userRepository.findUserByEmail(email);
        User returnUser = foundUser;
        
        userRepository.delete(foundUser);
        // if (e == email){
        //     userRepository.delete(foundUser);
        // }
        return returnUser;
    }

    // public List<User> getAllMembersFromYear(int year){

    //     List<User> users = new ArrayList<User>();

    //     for (int i = 0; userRepository.size() > i; i++){
    //         // if (userRepository.get(i).getMembershipYears(i) == year){
    //         //     users.add(userRepository.get(i));
    //         // }
    //         userRepository.get(i).getMembershipYears(year);
    //     }

    //     return users;
    // }

    // public User getUserWithAge(int age){
    //     for (int i = 0; userRepository.size() > i; i++){
    //         if (userRepository.get(i).getAge() == age){
    //             return userRepository.get(i);
    //         }
    //     }
    //     return null;
    // }
    
}