package edu.ncsu.csc.ase.dristi.test;



import org.junit.Assert;

import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.test.Main;
import junit.framework.TestCase;

public class MainTest extends TestCase
{
	private Main parser;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		this.parser = Main.getInstance();
	}
	
	public void testAbbrev() 
	{
		String text = "The British Broadcasting Corporation (BBC).";
		Tuple t = parser.parse(text);
		/*
		 is
			British Broadcasting Corporation
			BBC
		 */
		Assert.assertEquals(t.getRelation().getName(), "is");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "British Broadcasting Corporation");
		Assert.assertEquals(t.getRight().getEntity().getName(), "BBC");
	}
	
	public void testAcomp()
	{
		String text = "She looks very beautiful.";
		Tuple t = parser.parse(text);
		/*
		 looks
			She
			very beautiful
		 */
		System.err.println(t);
		Assert.assertEquals(t.getRelation().getName(), "looks");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "She");
		Assert.assertEquals(t.getRight().getEntity().getName(), "very beautiful");
	}
	
	public void testNpadvmod()
	{
		String text = "The patient was 65 years old.";
		Tuple t = parser.parse(text);
		/*
			was
				65 years old
				patient
		 */
		System.err.println(t);
		Assert.assertEquals(t.getRelation().getName(), "was");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "65 years old");
		Assert.assertEquals(t.getRight().getEntity().getName(), "patient");
	}
	
	public void testAdvcl()
	{
		String text = "The accident happened as the night was falling.";
		Tuple t = parser.parse(text);
		/*
		 as
			happened
				accident
			was falling
				night
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "as");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "happened");
		
		Assert.assertNull(t.getLeft().getLeft());
		
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getEntity().getName(), "accident");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "was falling");
		
		Assert.assertNull(t.getRight().getLeft());
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getEntity().getName(), "night");
		
	}
	
	public void testAdvmod()
	{
		String text = "Banana-X is genetically modified food.";
		Tuple t = parser.parse(text);
		/*
		 genetically modified
			food
			Banana-X
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "genetically modified");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "food");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "Banana-X");
		
	}
	
	public void testAgent()
	{
		String text = "The man has been killed by the police.";
		Tuple t = parser.parse(text);
		/*
		 by
			has been killed
				man
			police
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "by");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "has been killed");
		
		Assert.assertNull(t.getLeft().getLeft());
		
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getEntity().getName(), "man");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "police");
		
	}
	
	public void testPrep()
	{
		String text = "I saw a cat in a hat.";
		Tuple t = parser.parse(text);
		/*
			in
				saw
					I
					cat
				hat
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "in");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "saw");
		
		Assert.assertEquals(t.getLeft().getLeft().isTerminal(),true);
		Assert.assertEquals(t.getLeft().getLeft().getEntity().getName(), "I");
		
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getEntity().getName(), "cat");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "hat");
		
	}
	
	public void testCC()
	{
		String text = "Bill is big and honest.";
		Tuple t = parser.parse(text);
		System.err.println(t);
		/*
			is
				and
					big
					is
						honest
						Bill
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "is");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "and");
		
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getEntity().getName(), "big");
		
		
		Assert.assertEquals(t.getRight().getLeft().isTerminal(), false);
		Assert.assertEquals(t.getRight().getLeft().getRelation().getName(), "is");
		
		Assert.assertEquals(t.getRight().getLeft().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().getLeft().getLeft().getEntity().getName(), "honest");
		
		Assert.assertEquals(t.getRight().getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getLeft().getRight().getEntity().getName(), "Bill");
		
		//---------------------------------------------------
		//Assert.assertEquals(t.getRight().isTerminal(), true);
		//Assert.assertEquals(t.getRight().getEntity().getName(), "Bill");
		
	}
	
	
	public void testCcomp()
	{
		String text = "He says that you like to swim.";
		Tuple t = parser.parse(text);
		/*
			says
				He
				to swim
					like
						you

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "says");
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "He");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "to swim");
		
		
		Assert.assertNull(t.getRight().getLeft());
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRight().getRelation().getName(), "like");
		
		Assert.assertNull(t.getRight().getRight().getLeft());
		
		Assert.assertEquals(t.getRight().getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getRight().getEntity().getName(), "you");
	}
	
	public void testAmod() 
	{
		String text = "Sam eats red meat.";
		Tuple t = parser.parse(text);
		/*
		 eats
			Sam
			red meat
		 */
		Assert.assertEquals(t.getRelation().getName(), "eats");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "Sam");
		Assert.assertEquals(t.getRight().getEntity().getName(), "red meat");
	}
	
	public void testNsubj() 
	{
		String text = "The baby is cute.";
		Tuple t = parser.parse(text);
		/*
		 is
			cute
			baby
		 */
		Assert.assertEquals(t.getRelation().getName(), "is");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "cute");
		Assert.assertEquals(t.getRight().getEntity().getName(), "baby");
	}
	
	public void testPrt() 
	{
		String text = "They shut down the station.";
		Tuple t = parser.parse(text);
		/*
			shut down
				They
				station
		 */
		Assert.assertEquals(t.getRelation().getName(), "shut down");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "They");
		Assert.assertEquals(t.getRight().getEntity().getName(), "station");
	}
	
	public void testAux() 
	{
		String text = "Sam has died.";
		Tuple t = parser.parse(text);
		/*
		 has died
			Sam
		 */
		Assert.assertEquals(t.getRelation().getName(), "has died");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertNull(t.getLeft());
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "Sam");
	}
	
	public void testAuxPass() 
	{
		String text = "Kennedy has been killed.";
		Tuple t = parser.parse(text);
		/*
			has been killed
				Kennedy
		 */
		Assert.assertEquals(t.getRelation().getName(), "has been killed");
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertNull(t.getLeft());
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "Kennedy");
	}
	
	public void testCsubj()
	{
		String text = "What she said is not true.";
		Tuple t = parser.parse(text);
		/*
			not
				is
					true
					said
						What
						she

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "not");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "is");
		
		
		Assert.assertNull(t.getLeft());
		
		Assert.assertEquals(t.getRight().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().getLeft().getEntity().getName(), "true");
		
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRight().getRelation().getName(), "said");
		
		
		Assert.assertEquals(t.getRight().getRight().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getLeft().getEntity().getName(), "What");
		
		Assert.assertEquals(t.getRight().getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getRight().getEntity().getName(), "she");
		
	}
	
	
	public void testRcmod()
	{
		String text = "I saw the man you love.";
		Tuple t = parser.parse(text);
		/*
			saw
				I
				love
					man
					you

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "saw");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "I");
		
		
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "love");
		
		
		Assert.assertEquals(t.getRight().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().getLeft().getEntity().getName(), "man");
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getEntity().getName(), "you");
		
	}
	
	public void testPartmod()
	{
		String text = "Truffles picked during the spring are tasty.";
		Tuple t = parser.parse(text);
		/*
			are
				picked
					Truffles
					during
						spring
				tasty

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "are");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "picked");
		
		
		Assert.assertEquals(t.getLeft().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getLeft().getEntity().getName(), "Truffles");
		
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRight().getRelation().getName(), "during");
		
		Assert.assertNull(t.getLeft().getRight().getLeft());
		
		Assert.assertEquals(t.getLeft().getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getRight().getEntity().getName(), "spring");
		
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "tasty");
		
	}
	
	public void testCsubjpass()
	{
		String text = "She lied was suspected by everyone.";
		Tuple t = parser.parse(text);
		/*
			lied
				she
				was suspected by
					everyone

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "lied");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getEntity().getName(), "She");
		
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "was suspected by");
		
		
		Assert.assertNull(t.getRight().getLeft());
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getEntity().getName(), "everyone");
		
	}
	
	public void testNsubjpass()
	{
		String text = "Dole was defeated by Clinton.";
		Tuple t = parser.parse(text);
		/*
			by
				was defeated
					Dole
				Clinton
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "by");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "Clinton");
		
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "was defeated");
		
		
		Assert.assertNull(t.getLeft().getLeft());
		
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getEntity().getName(), "Dole");
		
	}

	
	public void testQuantMod()
	{
		String text = "About 200 people came to the party.";
		Tuple t = parser.parse(text);
		/*
			to
				came
					About 200 people
				party
		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "to");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "party");
		
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "came");
		
		
		Assert.assertNull(t.getLeft().getLeft());
		
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getEntity().getName(), "About 200 people");
		
	}	
	public void testDobj()
	{
		String text = "She gave him a raise.";
		Tuple t = parser.parse(text);
		/*
			to
				gave
					She
					raise
				him

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "to");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getEntity().getName(), "him");
		
		//---------------------------------------------------
		Assert.assertEquals(t.getLeft().isTerminal(), false);
		Assert.assertEquals(t.getLeft().getRelation().getName(), "gave");
		
		Assert.assertEquals(t.getLeft().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getLeft().getEntity().getName(), "She");
		
		Assert.assertEquals(t.getLeft().getRight().isTerminal(), true);
		Assert.assertEquals(t.getLeft().getRight().getEntity().getName(), "raise");
		
	}
	
	
	public void testExpl()
	{
		String text = "There is a ghost in the room.";
		Tuple t = parser.parse(text);
		/*
			There is
				in
					room
					ghost

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "There is");
		
		//---------------------------------------------------
		Assert.assertNull(t.getLeft());
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "in");
		
		Assert.assertEquals(t.getRight().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().getLeft().getEntity().getName(), "ghost");
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getEntity().getName(), "room");
		
	}
	
	public void testInfmod()
	{
		String text = "I don't have anything to say.";
		Tuple t = parser.parse(text);
		/*
			to say
				n't
					do have
						I
						anything

		 */
		
		Assert.assertEquals(t.isTerminal(), false);
		Assert.assertEquals(t.getRelation().getName(), "to say");
		
		//---------------------------------------------------
		Assert.assertNull(t.getLeft());
		
		//---------------------------------------------------
		Assert.assertEquals(t.getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRelation().getName(), "n't");
		
		Assert.assertNull(t.getRight().getLeft());
		
		Assert.assertEquals(t.getRight().getRight().isTerminal(), false);
		Assert.assertEquals(t.getRight().getRight().getRelation().getName(), "do have");
		
		Assert.assertEquals(t.getRight().getRight().getLeft().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getLeft().getEntity().getName(), "I");
		
		Assert.assertEquals(t.getRight().getRight().getRight().isTerminal(), true);
		Assert.assertEquals(t.getRight().getRight().getRight().getEntity().getName(), "anything");
		
	}
}

