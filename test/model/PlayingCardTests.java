package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the PlayingCard class behavior.
 */
public class PlayingCardTests {
  private PlayingCard card;
  private PlayingCard holeCard;

  @Before
  public void setup() {
    card = new PlayingCard("TestCard", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR);
    holeCard = new PlayingCard(true);
  }

  /**
   * Tests that card returns the correct name.
   */
  @Test
  public void testGetName() {
    Assert.assertEquals("TestCard", card.getName());
  }

  /**
   * Tests that hole card returns the correct name.
   */
  @Test
  public void testIsHole() {
    Assert.assertFalse(card.isHole());
    Assert.assertTrue(holeCard.isHole());
  }

  /**
   * Test that setting color mutates the color of the card.
   */
  @Test
  public void testSetColor() {
    card.setColor(Color.RED);
    Assert.assertEquals(Color.RED, card.getColor());
  }

  /**
   * Test that getting color returns the correct color.
   */
  @Test
  public void testGetColor() {
    card.setColor(Color.BLUE);
    Assert.assertEquals(Color.BLUE, card.getColor());
  }

  /**
   * Test that getting attacks returns the correct attacks.
   */
  @Test
  public void testGetAttacks() {
    Assert.assertEquals(1, card.getAttacks().get(Direction.NORTH).getValue());
    Assert.assertEquals(2, card.getAttacks().get(Direction.SOUTH).getValue());
    Assert.assertEquals(3, card.getAttacks().get(Direction.EAST).getValue());
    Assert.assertEquals(4, card.getAttacks().get(Direction.WEST).getValue());
  }
}