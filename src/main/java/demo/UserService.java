package demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private List<User> userRepository = new ArrayList<>();

    public UserService() {
    }

    public List<User> getAllUsers() {
        return userRepository;
    }

    public List<User> getUsersWithAgeOlderThan(int age) {
        return userRepository.stream().filter(user -> user.getAge()>age).toList();
    }

    public User getOldestUser() {
        User oldest = null;
        if (userRepository.size()>0) {
            oldest = userRepository.get(0);
            for (User user : userRepository) {
                if (user.getAge() > oldest.getAge())
                    oldest = user;
            }
        }
        return oldest;
    }

    public User getUserWithName(String name) {
        return userRepository.stream().filter(user -> user.getName().equals(name)).toList().get(0);
    }

    public User getUserWithEmail(String email){
        for (int i = 0; userRepository.size() > i; i++){
            if (userRepository.get(i).getEmail() == email){
                return userRepository.get(i);
            }
        }
        return null;
    }

    public List<User> getUsersWithAgeBetween(int min, int max){
        return userRepository.stream().filter(user -> user.getAge()>min && user.getAge()<max).toList();
    }

    public boolean addUser(User user) {
        for (int i = 0; userRepository.size() > i; i++){
            if (user.getEmail() == userRepository.get(i).getEmail()){
                return false;
            }
        }
        return userRepository.add(user);
    }

    public User removeUser(String email){ 
        for (int i = 0; userRepository.size() > i; i++){
            if (userRepository.get(i).getEmail() == email){
                User foundUser = userRepository.get(i);
                userRepository.remove(i);
                return foundUser;
            }
        }
        return null;
    }

    public List<User> getAllMembersFromYear(int year){

        List<User> users = new ArrayList<User>();

        for (int i = 0; userRepository.size() > i; i++){
            // if (userRepository.get(i).getMembershipYears(i) == year){
            //     users.add(userRepository.get(i));
            // }
            userRepository.get(i).getMembershipYears(year);
        }

        return users;
    }

    
}