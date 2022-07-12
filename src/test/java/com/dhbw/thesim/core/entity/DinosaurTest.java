package com.dhbw.thesim.core.entity;

import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Dinosaur} class.
 *
 * @author Daniel Czeschner
 */
class DinosaurTest {

    Dinosaur testDinosaur;

    @BeforeEach
    void setUp() {
        Image img = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testDinosaur.png")));
        testDinosaur = new Dinosaur("test", img, 1, 2,
                3, 4, 5, 6, 7, 8,
                true, false, 'a', 9,
                10, 'f');
    }

    @AfterEach
    void tearDown() {
        testDinosaur = null;
    }

    @DisplayName("Create an exact copy of a dinosaur.")
    @Test
    void copyOf() {
        //arrange

        //act
        Dinosaur copy = testDinosaur.copyOf();
        //assert
        assertAll("All stats need to be equal",
                () -> assertEquals(testDinosaur.getType(), copy.getType(), "Type needs to be equal"),
                () -> assertEquals(testDinosaur.getJavaFXObj().getImage(), copy.getJavaFXObj().getImage(), "Image needs to be equal"),
                () -> assertEquals(testDinosaur.getNutrition(), copy.getNutrition(), "Nutrition needs to be equal"),
                () -> assertEquals(testDinosaur.getHydration(), copy.getHydration(), "Hydration needs to be equal"),
                () -> assertEquals(testDinosaur.getStrength(), copy.getStrength(), "Strength needs to be equal"),
                () -> assertEquals(testDinosaur.getSpeed(), copy.getSpeed(), "Speed needs to be equal"),
                () -> assertEquals(testDinosaur.getReproductionRate(), copy.getReproductionRate(), "ReproductionRate needs to be equal"),
                () -> assertEquals(testDinosaur.getWeight(), copy.getWeight(), "Weight needs to be equal"),
                () -> assertEquals(testDinosaur.getLength(), copy.getLength(), "Length needs to be equal"),
                () -> assertEquals(testDinosaur.getHeight(), copy.getHeight(), "Height needs to be equal"),
                () -> assertEquals(testDinosaur.canSwim(), copy.canSwim(), "Swim stats needs to be equal"),
                () -> assertEquals(testDinosaur.canClimb(), copy.canClimb(), "Climb stats needs to be equal"),
                () -> assertEquals(testDinosaur.getDiet(), copy.getDiet(), "Diet type needs to be equal"),
                () -> assertEquals(testDinosaur.getCharDiet(), copy.getCharDiet(), "Diet type (as char) needs to be equal"),
                () -> assertEquals(testDinosaur.getViewRange(), copy.getViewRange(), "View range needs to be equal"),
                () -> assertEquals(testDinosaur.getInteractionRange(), copy.getInteractionRange(), "Interaction range needs to be equal"),
                () -> assertEquals(testDinosaur.getGender(), copy.getGender(), "Gender needs to be equal"),
                () -> assertEquals(testDinosaur.getTarget(), copy.getTarget(), "Targets need to be equal"),
                () -> assertEquals(testDinosaur.getPartner(), copy.getPartner(), "Partner need to be equal"),
                () -> assertEquals(testDinosaur.getMaxHydration(), copy.getMaxHydration(), "Max hydration need to be equal"),
                () -> assertEquals(testDinosaur.getMaxNutrition(), copy.getMaxNutrition(), "Max nutrition need to be equal"),
                () -> assertEquals(testDinosaur.getTimeOfBirth().getTime(), copy.getTimeOfBirth().getTime(), "Time of birth needs to be equal"),
                () -> assertEquals(testDinosaur.isChased(), copy.isChased(), "Chased state need to be equal"),
                () -> assertEquals(testDinosaur.isForcedToNoOp(), copy.isForcedToNoOp(), "Force NoOp state need to be equal")
        );

        //changes
        testDinosaur.setNutrition(77);
        testDinosaur.setHydration(77);
        testDinosaur.setSprite(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testDinosaur.png"))));
        testDinosaur.setIsChased(true);
        testDinosaur.setTimeOfBirth(7);
        testDinosaur.forceNoOp();

        //assert changes
        assertAll("Changed stats should not be equal",
                () -> assertEquals(testDinosaur.getType(), copy.getType(), "Type needs to be equal"),
                () -> assertNotEquals(testDinosaur.getJavaFXObj().getImage(), copy.getJavaFXObj().getImage(), "Image should not be equal"),
                () -> assertNotEquals(testDinosaur.getNutrition(), copy.getNutrition(), "Nutrition should not be equal"),
                () -> assertNotEquals(testDinosaur.getHydration(), copy.getHydration(), "Hydration should not be equal"),
                () -> assertEquals(testDinosaur.getStrength(), copy.getStrength(), "Strength needs to be equal"),
                () -> assertEquals(testDinosaur.getSpeed(), copy.getSpeed(), "Speed needs to be equal"),
                () -> assertEquals(testDinosaur.getReproductionRate(), copy.getReproductionRate(), "ReproductionRate needs to be equal"),
                () -> assertEquals(testDinosaur.getWeight(), copy.getWeight(), "Weight needs to be equal"),
                () -> assertEquals(testDinosaur.getLength(), copy.getLength(), "Length needs to be equal"),
                () -> assertEquals(testDinosaur.getHeight(), copy.getHeight(), "Height needs to be equal"),
                () -> assertEquals(testDinosaur.canSwim(), copy.canSwim(), "Swim stats needs to be equal"),
                () -> assertEquals(testDinosaur.canClimb(), copy.canClimb(), "Climb stats needs to be equal"),
                () -> assertEquals(testDinosaur.getDiet(), copy.getDiet(), "Diet type needs to be equal"),
                () -> assertEquals(testDinosaur.getCharDiet(), copy.getCharDiet(), "Diet type (as char) needs to be equal"),
                () -> assertEquals(testDinosaur.getViewRange(), copy.getViewRange(), "View range needs to be equal"),
                () -> assertEquals(testDinosaur.getInteractionRange(), copy.getInteractionRange(), "Interaction range needs to be equal"),
                () -> assertEquals(testDinosaur.getGender(), copy.getGender(), "Gender needs to be equal"),
                () -> assertEquals(testDinosaur.getTarget(), copy.getTarget(), "Targets need to be equal"),
                () -> assertEquals(testDinosaur.getPartner(), copy.getPartner(), "Partner need to be equal"),
                () -> assertEquals(testDinosaur.getMaxHydration(), copy.getMaxHydration(), "Max hydration need to be equal"),
                () -> assertEquals(testDinosaur.getMaxNutrition(), copy.getMaxNutrition(), "Max nutrition need to be equal"),
                () -> assertNotEquals(testDinosaur.getTimeOfBirth().getTime(), copy.getTimeOfBirth().getTime(), "Time of birth should not be equal"),
                () -> assertNotEquals(testDinosaur.isChased(), copy.isChased(), "Chased state should not be equal"),
                () -> assertNotEquals(testDinosaur.isForcedToNoOp(), copy.isForcedToNoOp(), "Force NoOp state should not be equal")
        );
    }

    @DisplayName("A dinosaur gets eaten.")
    @Test
    void eat() {
        //arrange

        //act
        testDinosaur.eat();
        //assert
       assertAll("Dinosaur should be dead",
               () -> assertEquals(0, testDinosaur.getNutrition(), "Nutrition should be 0."),
               () -> assertEquals(0, testDinosaur.getHydration(), "Hydration should be 0.")
       );
    }

    @DisplayName("Does a dinosaur can be eaten.")
    @Test
    void canBeEaten() {
        //arrange

        //act
        boolean strongerDinosaur = testDinosaur.canBeEaten(5);
        boolean weakerDinosaur = testDinosaur.canBeEaten(2);

        //Set the hydration to 0
        testDinosaur.setHydration(0);
        boolean dinosaurIsDead = testDinosaur.canBeEaten(5);

        //assert
        assertAll("Dinosaur should be dead",
                () -> assertTrue(strongerDinosaur, "A stronger dinosaur should be able to eat this dino."),
                () -> assertFalse(weakerDinosaur, "A weaker dinosaur should be able to eat this dino."),
                () -> assertFalse(dinosaurIsDead, "If the dinosaur is dead, no dinosaur should be able to eat this dinosaur.")
        );
    }

    @DisplayName("Check if a dinosaur is willing to mate.")
    @Test
    void isWillingToMate() {
        boolean reproductionValueNotFull = testDinosaur.isWillingToMate();

        testDinosaur.setReproductionValue(100);
        boolean reproductionValueIsFull = testDinosaur.isWillingToMate();

        testDinosaur.setIsChased(true);
        boolean isChased = testDinosaur.isWillingToMate();

        //assert
        assertAll("Dinosaur is willing to mate?",
                () -> assertFalse(reproductionValueNotFull, "Reproduction value is not full"),
                () -> assertTrue(reproductionValueIsFull, "Reproduction value is full"),
                () -> assertFalse(isChased, "If the dinosaur is chased it should be false")
        );
    }

    @DisplayName("Check if a dinosaur died of thirst")
    @Test
    void diedOfThirst() {
        boolean isNotDead = testDinosaur.diedOfThirst();

        testDinosaur.setNutrition(0);
        boolean didNotDiedOfThirst = testDinosaur.diedOfThirst();

        testDinosaur.setHydration(0);
        boolean diedOfThirst = testDinosaur.diedOfThirst();

        //assert
        assertAll("Dinosaur is willing to mate?",
                () -> assertFalse(isNotDead, "Hydration is not 0"),
                () -> assertFalse(didNotDiedOfThirst, "Nutrition is 0 but not hydration"),
                () -> assertTrue(diedOfThirst, "Hydration is 0")
        );
    }

    @DisplayName("Check if a dinosaur died of hunger")
    @Test
    void diedOfHunger() {
        boolean isNotDead = testDinosaur.diedOfThirst();

        testDinosaur.setNutrition(0);
        boolean didNotDiedOfHunger = testDinosaur.diedOfThirst();

        testDinosaur.setHydration(0);
        boolean diedOfHunger = testDinosaur.diedOfThirst();

        //assert
        assertAll("Dinosaur is willing to mate?",
                () -> assertFalse(isNotDead, "Nutrition is not 0"),
                () -> assertFalse(didNotDiedOfHunger, "Hydration is 0 but not nutrition"),
                () -> assertTrue(diedOfHunger, "Nutrition is 0")
        );
    }
}