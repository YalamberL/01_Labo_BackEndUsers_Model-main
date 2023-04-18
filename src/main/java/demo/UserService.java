package demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public List<User> getUsersWithAgeOlderThan(int age) throws ServiceException {
        if (userRepository.findUsersByAgeAfter(age).isEmpty() == true){
            throw new ServiceException("users", "no users with age " + age + " found");
        }
        return userRepository.findUsersByAgeAfter(age);
    }

    public User getOldestUser() throws ServiceException {
        if (userRepository.findAllByOrderByAgeDesc() == null){
            throw new ServiceException("users", "no oldest user found");
        }
        User oldest = null;
        List<User> users = new ArrayList<User>();
        users.addAll((Collection<? extends User>) userRepository.findAllByOrderByAgeDesc());
        if (users.size() > 0){
            oldest = users.get(0);
            for (User user : users) {
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

    public User getUserWithEmail(String email) throws ServiceException {
        if (userRepository.findUserByEmail(email) == null){
            throw new ServiceException("user", "no user found with email: " + email);
        }
        return userRepository.findUserByEmail(email);
    }

    public List<User> getUsersWithAgeBetween(int min, int max){
        return userRepository.findAll().stream().filter(user -> user.getAge()>min && user.getAge()<max).toList();
    }

    public User addUser(User user) throws ServiceException {
        if (userRepository.findUserByEmail(user.getEmail()) != null){
            throw new ServiceException("email", "email already taken");
        }
        User userFound = userRepository.save(user);
        return userFound;
    }

    public User removeUser(String email) throws ServiceException{ 
        if (userRepository.findUserByEmail(email) == null){
            throw new ServiceException("user", "user with this email does not exist");
        }
        User foundUser = userRepository.findUserByEmail(email);
        User returnUser = foundUser;
        
        userRepository.delete(foundUser);
        // if (e == email){
        //     userRepository.delete(foundUser);
        // }
        return returnUser;
    }

    public Optional<User> removeUserById(Long id){
        Optional<User> foundUser = userRepository.findById(id);
        Optional<User> returnUser = foundUser;
        userRepository.delete(foundUser.get());
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