package demo;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "Users_Tbl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    // private meaning that it can only be used and modified in this class
    @NotBlank(message = "name may not be empty")
    private String name;

    @Positive(message = "age may not be negative")
    private int age;

    @Transient
    private List<Integer> membershipYears = new ArrayList<Integer>();

    @Email(message = "no valid email")
    private String email;

    @Pattern(regexp = "(?=\\S+$)(?=.*[a-z]).{8,50}", message = "password must be minimum 8 characters and may not contain white spaces")
    @Pattern(regexp = "(?=.*[0-9])(?=\\S+$)(?=.*[a-z]).{8,50}", message = "password must contain a digit")
    private String password;

    // //validating password
    // public static boolean isValidPassword(String password){
    //     String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        
    //     Pattern pattern = Pattern.compile(regex);

    //     if (password == null){
    //         return false;
    //     }

    //     Matcher m = pattern.matcher(password);

    //     return m.matches();
    // }

    // 1: This is a constructor
    public User(){

    }

    public User(String name, int age){
        this.name = name;
        this.age = age;
    }

    public User(String name, int age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public long getId(){
        return this.id;
    }

    public int countMembershipYearsAfter1999 () {
        int result = 0;
        for(Integer year: membershipYears) {
            if (year > 1999)
                result++;
        }
        return result;
    }

    public int countYearsOfMembership () {
        return membershipYears.size();
    }

    public void addMembershipYear (int year) {
        membershipYears.add(year);
    }

    public int getFirstMembershipYear(){
        if (membershipYears.size() > 0){
            int firstMembershipYear = membershipYears.get(0);

            for (Integer i = 1; membershipYears.size() > i; i++){
                if (membershipYears.get(i) < firstMembershipYear){
                    return membershipYears.get(i);
                }
            }
            return firstMembershipYear;
        }
        return 0;
    }

    public int getNumberOfMembershipYearsIn2000(){
        int result = 0;
        
        for (Integer year: membershipYears){
            if  (year >= 2000 && year < 3000)
                result++;
        }
        return result;
    }

    public boolean isPasswordCorrect(String password){
        if (this.password == password)
            return true;
        else 
            return false;
    }

    public int getAge() {
        return this.age;
    }

    public String getName () {
        return name;
    }

    public String getEmail(){
        if(this.email.contains("@"))
            return this.email;
        return null;
    }

    // public String getPassword(){
    //     if(this.password.isBlank())
    //         return "@$-"+"t"+"&%#";
    //     return "@$-"+ this.password +"&%#";
    // }

    public String getPassword(){
        if (this.password.isBlank()){
            return "t";
        }
            
        return this.password;
        
    }

    public int getMembershipYears(int year){
        for (int i = 0; membershipYears.size() > i; i++){
            if (membershipYears.get(i) == year){
                return membershipYears.get(i);
            }
        }
        return 0;
    }
   
    @Override
    public boolean equals(Object arg0) {
        return super.equals(arg0);
    }

    @Override
    public String toString() {
        return this.name + " is " + this.age + " years old and has as email " + this.email;
        //Elke is 44 years old and has as email elke.steegmans@ucll.be
    }
}