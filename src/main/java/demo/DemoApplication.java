package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// User elke = new User("Elke", 44);
		// System.out.println(elke.getAge());
		// elke.addMembershipYear(2000);
		// elke.addMembershipYear(2010);
		// elke.addMembershipYear(1999);
		// System.out.println(elke.countMembershipYearsAfter1999());

		// User miyo = new User("Miyo", 14);
		// System.out.println(miyo);

		// User yuki = new User("Yuki", 12);
		// System.out.println("User with name " + yuki.getName() + " is " + yuki.getAge() + " years old");
		
		// User eric = new User("Eric", 65);
		// System.out.println(eric.getName());


		//Lab User 4
		// User user=null;
		// System.out.println(user.getName());
		User bert = new User("Bert", 20, "bert@ucll.be", "abc");
		bert.addMembershipYear(2000);
		User bertII = new User("Bert", 20, "bert@ucll.be", "abc");
		bertII.addMembershipYear(2001);
		User bertIII = new User("Bert", 20, "bert@ucll.be", "xyz");
		bertIII.addMembershipYear(2003);
		User chris = new User("Chris", 20, "bert@ucll.be", "abc");
		chris.addMembershipYear(2000);
		System.out.println(bert.equals(bertII)); // returns true
		System.out.println(bert.equals(bertIII)); // returns true
		System.out.println(bert.equals(chris)); // returns false

		SpringApplication.run(DemoApplication.class, args);
		/**
		 * 4: 
		 * 44
		 * 2
		 * [object] -> demo.User@....
		 * User with name Yuki is 12 years old
		 * Eric
		 *  */ 
	}

}