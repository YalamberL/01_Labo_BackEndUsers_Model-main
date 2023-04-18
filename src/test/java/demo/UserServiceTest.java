package demo;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User elke;
    private User eric;
    private User yuki;
    private User miyo;

    @BeforeEach
    public void setUp() {
        //given
        elke = new User("Elke", 45, "elke@ucll.be", "t");
        eric = new User("Eric", 65, "eric@ucll.be", "t");
        yuki = new User("Yuki", 13, "yuki@ucll.be", "t");
        miyo = new User("Miyo", 15, "miyo@ucll.be", "t");
    }

    @Test
    public void givenNoUsers_whenValidUserAdded_ThenUserIsAddedAndUserIsReturned() throws ServiceException{
        // given
        when(userRepository.save(elke)).thenReturn(elke);

        // when
        User added = userService.addUser(elke);

        // then
        assertEquals(elke.getName(), added.getName());
        assertEquals(elke.getAge(), added.getAge());
        assertEquals(elke.getPassword(), added.getPassword());
        assertEquals(elke.getEmail(), added.getEmail());
    }

    @Test
    void given4Users_whenNewUserWithAlreadyUsedEmailIsAdded_thenUserIsNotAddedAndServiceExceptionIsThrown() {
        //given
        User otherElke = new User("Elke", 20, "elke@ucll.be", "elkeelke");
        when(userRepository.findUserByEmail("elke@ucll.be")).thenReturn(elke);

        //when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->userService.addUser(otherElke));
        
        // then
        assertEquals("email", ex.getField());  
        assertEquals("email already taken", ex.getMessage());
    }

    @Test
    public void givenUsersWhith1UserOlderThan20_whenGetUsersOlderThan20_thenListWith1UserOlderThan20IsReturned() throws ServiceException {
        //given
        List<User> usersAbove20 = new ArrayList<User>();
        usersAbove20.add(elke);
        when(userRepository.findUsersByAgeAfter(20)).thenReturn(usersAbove20);

        //when
        List<User> result = userService.getUsersWithAgeOlderThan(20);

        //then
        assertEquals(usersAbove20.size(), result.size());
        assertTrue(result.contains(elke));
        assertFalse(result.contains(miyo));
    }

    @Test
    public void givenUsersWhithNoUsersOlderThan20_whenGetUsersOlderThan20_thenServiceExceptionIsThrown() {
        //given
        List<User> usersAbove20 = new ArrayList<User>();
        when(userRepository.findUsersByAgeAfter(20)).thenReturn(usersAbove20);

        //when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->userService.getUsersWithAgeOlderThan(20));
        
        // then
        assertEquals("users", ex.getField());  
        assertEquals("no users with age 20 found", ex.getMessage());
    }
    
    @Test
    void given4UsersWhere2UsersWithAge44_whenSearchForUsersOlderThan43_then2UsersAreReturned() throws ServiceException {
        //given
        List<User> usersAbove43 = new ArrayList<>();
        usersAbove43.add(elke);
        usersAbove43.add(eric);
        when(userRepository.findUsersByAgeAfter(43)).thenReturn(usersAbove43);

        //when
        List<User> usersAged44 = userService.getUsersWithAgeOlderThan(43);

        //then
        assertEquals(2, usersAged44.size());
        assertTrue(containsUserWithName(usersAged44, "Elke"));
        assertTrue(containsUserWithName(usersAged44, "Eric"));
        assertFalse(containsUserWithName(usersAged44, "Miyo"));
    }

    @Test
    void given4UsersWhere0UsersWithAge80_whenSearchForUsersOlderThan80_thenServiceExceptionIsThrown() {
        //given
        List<User> usersAbove80 = new ArrayList<>();
        when(userRepository.findUsersByAgeAfter(80)).thenReturn(usersAbove80);

        //when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->userService.getUsersWithAgeOlderThan(80));
        
        // then
        assertEquals("users", ex.getField());  
        assertEquals("no users with age 80 found", ex.getMessage());
    }

    @Test
    void given4Users_whenSearchForOldestUser_thenOldestUserIsReturned() throws ServiceException{
        //given
        List<User> orderedByAge = new ArrayList<User>();
        orderedByAge.add(eric);
        orderedByAge.add(elke);
        orderedByAge.add(miyo);
        orderedByAge.add(yuki);
        when(userRepository.findAllByOrderByAgeDesc()).thenReturn(orderedByAge);

        //when
        User oldestUser = userService.getOldestUser();

        //then
        assertEquals(65, oldestUser.getAge());
        assertEquals("Eric", oldestUser.getName());
    }

    @Test
    void givenNoUsers_whenSearchForOldestUser_thenServiceExceptionIsThrown() {
        //given
        when(userRepository.findAllByOrderByAgeDesc()).thenReturn(null);
        
        //when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->userService.getOldestUser());
        
        // then
        assertEquals("users", ex.getField());  
        assertEquals("no oldest user found", ex.getMessage());
    }

    @Test
    void given4Users_whenSearchForUserWithExistingEmail_thenUserIsReturned() throws ServiceException {
        //given
        when(userRepository.findUserByEmail("miyo@ucll.be")).thenReturn(miyo);

        //when
        User foundUser = userService.getUserWithEmail("miyo@ucll.be");

        //then
        assertEquals(15, foundUser.getAge());
        assertEquals("Miyo", foundUser.getName());
    }

    @Test
    void given4Users_whenSearchForUserWithNonExistingEmail_thenServiceExceptionIsThrown() {
        //given
        when(userRepository.findUserByEmail("carmen@gmail.be")).thenReturn(null);

        //when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->userService.getUserWithEmail("carmen@gmail.be"));
        
        // then
        assertEquals("user", ex.getField());  
        assertEquals("no user found with email: carmen@gmail.be", ex.getMessage());
    }

    @Test
    void given4Users_whenRemoveExistingUser_thenUserIsRemovedAndRemovedUserIsReturned() throws ServiceException {
        //given
        when(userRepository.findUserByEmail("yuki@ucll.be")).thenReturn(yuki);

        //when
        User removedUser = userService.removeUser("yuki@ucll.be");

        //then
        assertEquals(yuki.getName(), removedUser.getName());
    }

    @Test
    void given4Users_whenRemoveNonExistingUser_thenUserIsNotRemovedAndServiceExceptionIsThrown() {
        //when
        ServiceException ex = Assertions.assertThrows(ServiceException.class, ()->userService.removeUser("stijn@ucll.be"));
        
        // then
        assertEquals("user", ex.getField());  
        assertEquals("user with this email does not exist", ex.getMessage());
    }

    private boolean containsUserWithName(List<User> users, String name) {
        return users.stream().anyMatch(user -> user.getName().equals(name));
    }
}



















// package demo;

// import java.util.List;
// import java.util.ArrayList;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import static org.mockito.Mockito.when;
// import static org.junit.jupiter.api.Assertions.*;

// @ExtendWith(MockitoExtension.class)
// class UserServiceTest {

//     @Mock
//     UserRepository userRepository;

//     @InjectMocks
//     UserService userService;
//     //given
//     //UserService serviceWithUsers = new UserService();
    
//     private User elke; 
//     private User miyo;
//     private User eric; 
//     private User yuki; 
//     private User stijn; 

//     UserService serviceWithoutUsers = new UserService();

//     @BeforeEach
//     void setUp() {
//         //given
//         elke = new User("Elke", 44, "elke@ucll.be", "elke");
//         miyo = new User("Miyo", 15, "miyo@ucll.be", "miyo");
//         eric = new User("Eric", 65, "eric@kuleuven.be", "eric");
//         yuki = new User("Yuki", 13, "yuki@ucll.be", "yuki");
//         stijn = new User("Stijn", 45, "stijn@ucll.be", "stijn");
    
//     }

//     @Test
//     public void givenNoUsers_whenValidUserAdded_ThenUserIsAddedAndUserIsReturned() {
//         // given
//         when(userRepository.save(elke)).thenReturn(elke);
//         // when
//         boolean added = userService.addUser(elke);
//         // then
//         assertTrue(added);
//     }

//     @Test
//     public void givenUsersWhith1UserOlderThan20_whenGetUsersOlderThan20_thenListWith1UserOlderThan20IsReturned() {

//         //given
//         List<User> usersAbove20 = new ArrayList<User>();
//         usersAbove20.add(elke);
//         when(userRepository.findUsersByAgeAfter(20)).thenReturn(usersAbove20);
//         //when
//         List<User> result = userService.getUsersWithAgeOlderThan(20);
//         //then
//         assertEquals(usersAbove20.size(), result.size());
//         assertTrue(result.contains(elke));
//         assertFalse(result.contains(miyo));
//     }

//     @Test
//     public void givenUsersWhithNoUsersOlderThan20_whenGetUsersOlderThan20_thenEmptyListIsReturned() {
//         //given
//         List<User> usersAbove20 = new ArrayList<User>();
//         when(userRepository.findUsersByAgeAfter(20)).thenReturn(usersAbove20);
//         //when
//         List<User> result = userService.getUsersWithAgeOlderThan(20);
//         //then
//         assertEquals(usersAbove20.size(), result.size());
//         assertFalse(result.contains(yuki));
//         assertFalse(result.contains(miyo));
//     }

//     @Test
//     public void given4Users_whenNewUserWithNotAlreadyUsedEmailIsAdded_thenUserIsAdded() {
//         //given
//         User stijn = new User("Stijn", 46, "stijn@ucll.be", "t");
//         when(userRepository.save(stijn)).thenReturn(stijn);
//         //when
//         boolean added = userService.addUser(stijn);
//         //then
//         assertTrue(added);
//     }

//     @Test
//     public void given4Users_whenNewUserWithAlreadyUsedEmailIsAdded_thenUserIsNotAdded() {
//         //given
//         User otherElke = new User("Elke", 20, "elke@ucll.be", "elkeelke");
//         when(userRepository.findUserByEmail("elke@ucll.be")).thenReturn(elke);
//         //when
//         boolean added = userService.addUser(otherElke);
//         //then
//         assertFalse(added);
//     }

//     @Test
//     void given4UsersWhere2UsersWithAge44_whenSearchForUsersOlderThan43_then2UsersAreReturned() {
//         //given
//         List<User> usersAbove43 = new ArrayList<>();
//         usersAbove43.add(elke);
//         usersAbove43.add(eric);
//         when(userRepository.findUsersByAgeAfter(43)).thenReturn(usersAbove43);
//         //when
//         List<User> usersAged44 = userService.getUsersWithAgeOlderThan(43);
//         //then
//         assertEquals(2, usersAged44.size());
//         assertTrue(containsUserWithName(usersAged44, "Elke"));
//         assertTrue(containsUserWithName(usersAged44, "Eric"));
//         assertFalse(containsUserWithName(usersAged44, "Miyo"));
//     }

//     @Test
//     void given4UsersWhere0UsersWithAge80_whenSearchForUsersOlderThan80_thenAnEmpyListIsReturned() {
//         //given
//         List<User> usersAbove80 = new ArrayList<>();
//         when(userRepository.findUsersByAgeAfter(80)).thenReturn(usersAbove80);
//         //when
//         List<User> usersAged81 = userService.getUsersWithAgeOlderThan(80);
//         //then
//         assertEquals(0, usersAged81.size());
//     }

//     @Test
//     void given4Users_whenSearchForOldestUser_thenOldestUserIsReturned() {
//         //given
//         List<User> orderedByAge = new ArrayList<User>();
//         orderedByAge.add(eric);
//         orderedByAge.add(elke);
//         orderedByAge.add(miyo);
//         orderedByAge.add(yuki);
//         when(userRepository.findAllByOrderByAgeDesc()).thenReturn(orderedByAge);
//         //when
//         User oldestUser = userService.getOldestUser();
//         //then
//         assertEquals(65, oldestUser.getAge());
//         assertEquals("Eric", oldestUser.getName());
//     }

//     @Test
//     void givenNoUsers_whenSearchForOldestUser_thenNullValueIsReturned() {
//         //given
//         when(userRepository.findAllByOrderByAgeDesc()).thenReturn(null);
    
//         //when
//         User oldestUser = userService.getOldestUser();
//         //then
//         assertNull(oldestUser);
//     }

//     @Test
//     void given4Users_whenSearchForUserWithExistingEmail_thenUserIsReturned() {
//         //given
//         when(userRepository.findUserByEmail("miyo@ucll.be")).thenReturn(miyo);
//         //when
//         User foundUser = userService.getUserWithEmail("miyo@ucll.be");
//         //then
//         assertEquals(15, foundUser.getAge());
//         assertEquals("Miyo", foundUser.getName());
//     }

//     @Test
//     void given4Users_whenSearchForUserWithNonExistingEmail_thenNullIsReturned() {
//         //given
//         when(userRepository.findUserByEmail("carmen@gmail.be")).thenReturn(null);
//         //when
//         User foundUser = userService.getUserWithEmail("carmen@gmail.be");
//         //then
//         assertNull(foundUser);
//     }

//     @Test
//     void given4Users_whenRemoveNonExistingUser_thenUserIsNotRemovedAndNullValueIsReturned()
//     {
//         //given
//         //given
//         when(userRepository.deleteByEmail("stijn@ucll.be")).thenReturn(null);
//         //when
//         User removedUser = userService.removeUser("stijn@ucll.be");
//         //then
//         assertNull(removedUser);
//     }

//     private boolean containsUserWithName(List<User> users, String name) {
//         return users.stream().anyMatch(user -> user.getName().equals(name));
//     }
// }







// //     @Test
// //     void given4Users_whenNewUserWithNotAlreadyUsedEmailIsAdded_thenUserIsAdded() {
// //         //given
// //         assertEquals(4, serviceWithUsers.getAllUsers().size());

// //         //when
// //         boolean added = serviceWithUsers.addUser(stijn);

// //         //then
// //         assertTrue(added);
// //         assertEquals(5, serviceWithUsers.getAllUsers().size());
// //         assertTrue(serviceWithUsers.getAllUsers().contains(stijn));
// //         assertTrue(serviceWithUsers.getAllUsers().contains(elke));
// //     }

// //     @Test
// //     void given4Users_whenNewUserWithAlreadyUsedEmailIsAdded_thenUserIsNotAdded() {
// //         //given
// //         assertEquals(4, serviceWithUsers.getAllUsers().size());

// //         //when
// //         User otherElke = new User("Elke", 20, "elke@ucll.be", "elkeelke");
// //         boolean added = serviceWithUsers.addUser(otherElke);

// //         //then
// //         assertFalse(added);
// //         assertEquals(4, serviceWithUsers.getAllUsers().size());
// //         assertFalse(serviceWithUsers.getAllUsers().contains(otherElke));
// //         assertTrue(serviceWithUsers.getAllUsers().contains(elke));
// //     }

// //     @Test
// //     void given4UsersWhere2UsersWithAge44_whenSearchForUsersOlderThan43_then2UsersAreReturned() {
// //         //when
// //         List<User> usersAged44 = serviceWithUsers.getUsersWithAgeOlderThan(43);

// //         //then
// //         assertEquals(2, usersAged44.size());
// //         assertTrue(containsUserWithName(usersAged44, "Elke"));
// //         assertTrue(containsUserWithName(usersAged44, "Eric"));
// //         assertFalse(containsUserWithName(usersAged44, "Miyo"));
// //     }

// //     @Test
// //     void given4UsersWhere0UsersWithAge80_whenSearchForUsersOlderThan80_thenAnEmpyListIsReturned() {
// //         //when
// //         List<User> usersAged81 = serviceWithUsers.getUsersWithAgeOlderThan(80);

// //         //then
// //         assertEquals(0, usersAged81.size());
// //     }

// //     @Test
// //     void given4Users_whenSearchForOldestUser_thenOldestUserIsReturned() {
// //         //when
// //         User oldestUser = serviceWithUsers.getOldestUser();

// //         //then
// //         assertEquals(65, oldestUser.getAge());
// //         assertEquals("Eric", oldestUser.getName());
// //     }

// //     @Test
// //     void givenNoUsers_whenSearchForOldestUser_thenNullValueIsReturned() {
// //         //when
// //         User oldestUser = serviceWithoutUsers.getOldestUser();

// //         //then
// //         assertNull(oldestUser);
// //     }

// //     @Test
// //     void given4Users_whenSearchForUserWithExistingEmail_thenUserIsReturned() {
// //         //when
// //         User foundUser = serviceWithUsers.getUserWithEmail("miyo@ucll.be");

// //         //then
// //         assertEquals(15, foundUser.getAge());
// //         assertEquals("Miyo", foundUser.getName());
// //     }

// //     @Test
// //     void given4Users_whenSearchForUserWithNonExistingEmail_thenNullIsReturned() {
// //         //when
// //         User foundUser = serviceWithUsers.getUserWithEmail("carmen@gmail.be");

// //         //then
// //         assertNull(foundUser);
// //     }

// //     @Test
// //     void given4Users_whenRemoveExistingUser_thenUserIsRemovedAndRemovedUserIsReturned() {
// //         //given
// //         assertEquals(4, serviceWithUsers.getAllUsers().size());

// //         //when
// //         User removedUser = serviceWithUsers.removeUser("yuki@ucll.be");

// //         //then
// //         assertEquals(3, serviceWithUsers.getAllUsers().size());
// //         assertEquals(13, removedUser.getAge());
// //         assertEquals("Yuki", removedUser.getName());
// //     }

// //     @Test
// //     void given4Users_whenRemoveNonExistingUser_thenUserIsNotRemovedAndNullValueIsReturned() {
// //         //given
// //         assertEquals(4, serviceWithUsers.getAllUsers().size());

// //         //when
// //         User removedUser = serviceWithUsers.removeUser("stijn@ucll.be");

// //         //then
// //         assertEquals(4, serviceWithUsers.getAllUsers().size());
// //         assertNull(removedUser);
// //     }

// //     private boolean containsUserWithName(List<User> users, String name) {
// //         return users.stream().anyMatch(user -> user.getName().equals(name));
// //     }
// // }