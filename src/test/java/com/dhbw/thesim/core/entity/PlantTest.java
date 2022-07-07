package com.dhbw.thesim.core.entity;

import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the {@link Plant} class.
 *
 * @author Daniel Czeschner
 */
class PlantTest {

    Plant testPlant;

    @BeforeEach
    void setUp() {
        Image img = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("testPlant.png")));
        testPlant = new Plant("test", img, 1, 2);
        testPlant.setGrowth(Plant.MAX_GROWTH);  //Plant should be grown by default
    }

    @AfterEach
    void tearDown() {
        testPlant = null;
    }

    @DisplayName("Plant gets eaten")
    @Test
    void eat() {
        //act
        testPlant.eat();
        //assert
        assertEquals(0, testPlant.getGrowth(), "Plant should be eaten.");

    }

    @DisplayName("Can this plant be eaten")
    @Test
    void canBeEaten() {
        //act
        boolean dinosaurWantsToEatPlant = testPlant.canBeEaten(0);

        testPlant.setGrowth(0);
        boolean plantIsNotGrown = testPlant.canBeEaten(0);

        //assert
        assertAll("Can the plant be eaten?",
                () -> assertTrue(dinosaurWantsToEatPlant, "A dinosaur wants to eat this plant and plant is grown"),
                () -> assertFalse(plantIsNotGrown, "A dinosaur wants to eat this plant but the plant is not grown")
        );
    }

    @DisplayName("Check if the plant is grown")
    @Test
    void isGrown() {
        boolean plantIsGrown = testPlant.isGrown();
        testPlant.setGrowth(Plant.MAX_GROWTH-1);
        boolean plantIsNotGrown = testPlant.isGrown();

        //assert
        assertAll("Can the plant be eaten?",
                () -> assertTrue(plantIsGrown, "Plant is grown"),
                () -> assertFalse(plantIsNotGrown, "Plant is not grown")
        );
    }
}