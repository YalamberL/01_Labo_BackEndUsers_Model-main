package demo;

import java.util.ArrayList;
import java.util.List;

public class User {

    // private meaning that it can only be used and modified in this class
    private String name;
    private int age;
    private List<Integer> membershipYears = new ArrayList<Integer>();
    private String email;
    private String password;

    // 1: This is a constructor
    public User(){

    }

    public User(String name, int age){
        this.name = name;
        if (age >= 0)
            this.age = age;
    }

    public User(String name, int age, String email, String password) {
        this.name = name;
        if (age >= 0) 
            this.age = age;
        this.email = email;
        this.password = password;
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

    public String getPassword(){
        if(this.password.isBlank())
            return "@$-"+"t"+"&%#";
        return "@$-"+ this.password +"&%#";
    }

    public int getMembershipYears(int year){
        for (int i = 0; membershipYears.size() > i; i++){
            if (membershipYears.get(i) == year){
                return membershipYears.get(i);
            }
        }
        return 0;

        // List<Integer> years = new ArrayList<Integer>();
        // for (int i = 0; membershipYears.size() > i; i++){
        //     if (getName())
        //     years.add(membershipYears.get(i));
        // }
        // return years;
    }

    @Override
    public String toString() {
        return this.name + " is " + this.age + " years old and has as email " + this.email;
        //Elke is 44 years old and has as email elke.steegmans@ucll.be
    }
}