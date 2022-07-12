package com.dhbw.thesim.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Vector2D} class.
 *
 * @author Daniel Czeschner
 * @apiNote See <a href="https://stackoverflow.com/questions/10786587/java-double-precision-sum-trouble">Double precision loss</a>.
 */
class Vector2DTest {

    /**
     * Rounds a number to 4 decimal places. <br>
     * We use that function to fix the precision loss.
     *
     * @param value The number which should be rounded.
     * @return The rounded value.
     */
    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @DisplayName("The squared length of the vector should be calculated correct.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0", "1, 2, 5", "27, 9, 810", "2.16, 47.96, 2304.8272"})
    void calculateLengthSqOfAVector(double x, double y, double result) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        double vectorLengthSquare = round(testVector.lengthSq());
        //assert
        assertEquals(result, vectorLengthSquare, "The length of the vector (" + x + "," + y + ") squared should be calculated correct.");
    }

    @DisplayName("The length of a vector should be calculated correct.")
    @ParameterizedTest
    @CsvSource({"1, 2, 2.2361", "27, 9, 28.4605", "2.16, 47.96, 48.0086"})
    void calculateLengthOfAVector(double x, double y, double result) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        double vectorLengthSquare = round(testVector.length());
        //assert
        assertEquals(result, vectorLengthSquare, "The length of the vector (" + x + "," + y + ") should be calculated correct.");
    }

    @DisplayName("A vector should be normalized.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0", "1, 2, 0.4472, 0.8944", "27, 9, 0.9487, 0.3162", "2.16, 47.96, 0.0450, 0.9990"})
    void normalizeAVector(double x, double y, double resultX, double resultY) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        testVector.normalize();
        //assert
        assertAll("Normalize a vector",
                () -> assertEquals(resultX, round(testVector.getX()), "The x value should be correct."),
                () -> assertEquals(resultY, round(testVector.getY()), "The y value should be correct."),
                () -> assertEquals(x == 0 && y == 0 ? 0 : 1, round(testVector.length()), "The length of the vector should be 1 or 0.")
        );
    }

    @DisplayName("Multiplication on a vector should be correct.")
    @ParameterizedTest
    @CsvSource({"0, 0, 3, 0, 0", "4, 5, 8, 32, 40", "27.3, 16.4, 4.8, 131.04, 78.72"})
    void multiplyAValue(double x, double y, double value, double resultX, double resultY) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        Vector2D finalTestVector = testVector.multiply(value);
        //assert
        assertAll("Multiply a value to a vector",
                () -> assertEquals(resultX, round(finalTestVector.getX()), "The x value should be correct."),
                () -> assertEquals(resultY, round(finalTestVector.getY()), "The y value should be correct.")
        );
    }


    @DisplayName("Addition of a vector onto a vector should be correct.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0, 0", "7, 29, 4, 8, 11, 37", "23.4, 29, 77.9, 3.76, 101.3, 32.76"})
    void addAVector(double x1, double y1, double x2, double y2, double resultX, double resultY) {
        //arrange
        Vector2D testVector = new Vector2D(x1, y1);
        Vector2D additionVector = new Vector2D(x2, y2);
        //act
        Vector2D finalTestVector = testVector.add(additionVector);
        //assert
        assertAll("Multiply a value to a vector",
                () -> assertEquals(resultX, round(finalTestVector.getX()), "The x value should be correct."),
                () -> assertEquals(resultY, round(finalTestVector.getY()), "The y value should be correct.")
        );
    }

    @DisplayName("Addition of a value onto a vector should be correct.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0", "7, 29, 4, 11, 33", "23.4, 29, 3.76, 27.16, 32.76", "28, 250, -3, 25, 247"})
    void addAValue(double x, double y, double value, double resultX, double resultY) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        Vector2D finalTestVector = testVector.add(value);
        //assert
        assertAll("Multiply a value to a vector",
                () -> assertEquals(resultX, round(finalTestVector.getX()), "The x value should be correct."),
                () -> assertEquals(resultY, round(finalTestVector.getY()), "The y value should be correct.")
        );
    }

    @DisplayName("Subtraction of a vector from a vector should be correct.")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0, 0", "7, 29, 4, 8, 3, 21", "23.4, 29, 77.9, 3.76, -54.5, 25.24", "27, 187, -3, -28, 30, 215"})
    void subtract(double x1, double y1, double x2, double y2, double resultX, double resultY) {
        //arrange
        Vector2D testVector = new Vector2D(x1, y1);
        Vector2D subtractVector = new Vector2D(x2, y2);
        //act
        Vector2D finalTestVector = testVector.subtract(subtractVector);
        //assert
        assertAll("Subtract a vector from a vector",
                () -> assertEquals(resultX, round(finalTestVector.getX()), "The x value should be correct."),
                () -> assertEquals(resultY, round(finalTestVector.getY()), "The y value should be correct.")
        );
    }

    @DisplayName("A vector should only be identified as zero if he is zero.")
    @ParameterizedTest
    @CsvSource({"0, 0, true", "0, 0.1, false", "25, 29, false", "-2, 0, false"})
    void isZero(double x, double y, boolean result) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        boolean isVectorZero = testVector.isZero();
        //assert
        assertEquals(result, isVectorZero, "Vector zero status should be evaluated correct.");
    }

    @DisplayName("Distances between vectors")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0", "28, 48, 24.3, 0, 48.1424", "39.6, -4.5, 29, 30, 36.0917"})
    void distance(double x1, double y1, double x2, double y2, double result) {
        //arrange
        Vector2D testVector1 = new Vector2D(x1, y1);
        Vector2D testVector2 = new Vector2D(x2, y2);
        //act
        double distanceBetween = Vector2D.distance(testVector1, testVector2);
        //assert
        assertEquals(result, round(distanceBetween), "The distance should be correct.");
    }

    @DisplayName("Dot-Product of two vectors")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0", "28, 48, 24.3, 0, 680.4", "39.6, -4.5, 29, 30, 1013.4"})
    void dotProduct(double x1, double y1, double x2, double y2, double result) {
        //arrange
        Vector2D testVector1 = new Vector2D(x1, y1);
        Vector2D testVector2 = new Vector2D(x2, y2);
        //act
        double dotProduct = Vector2D.dotProduct(testVector1, testVector2);
        //assert
        assertEquals(result, round(dotProduct), "The dot product should be correct.");
    }

    @DisplayName("Cross-Product of two vectors")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0", "28, 48, 24.3, 0, -1166.4", "39.6, -4.5, 29, 30, 1318.5"})
    void crossProduct(double x1, double y1, double x2, double y2, double result) {
        //arrange
        Vector2D testVector1 = new Vector2D(x1, y1);
        Vector2D testVector2 = new Vector2D(x2, y2);
        //act
        double crossProduct = Vector2D.crossProduct(testVector1, testVector2);
        //assert
        assertEquals(result, round(crossProduct), "The cross product should be correct.");
    }


    @DisplayName("Is a vector near another vector")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 1, true", "28, 48, 24.3, 0, 27.4, false", "39.6, -4.5, 29, 30, 1000, true", "39.6, -4.5, 29, 30, 2, false"})
    void isInRangeOf(double x1, double y1, double x2, double y2, double maxDistance, boolean result) {
        //arrange
        Vector2D testVector1 = new Vector2D(x1, y1);
        Vector2D testVector2 = new Vector2D(x2, y2);
        //act
        boolean isClose = testVector1.isInRangeOf(testVector2, maxDistance);
        //assert
        assertEquals(result, isClose, "The close check should be correct.");
    }

    @DisplayName("Get the correct normalized direction vector to a target")
    @ParameterizedTest
    @CsvSource({"0, 0, 0, 0, 0, 0", "39, 120, 1500, 18, 0.9976, -0.0696", "1500, -30, 1560, 27, 0.725, 0.6887"})
    void directionToTarget(double x1, double y1, double x2, double y2, double resultX, double resultY) {
        //arrange
        Vector2D testVector1 = new Vector2D(x1, y1);
        Vector2D testVector2 = new Vector2D(x2, y2);
        //act
        Vector2D finalTestVector = testVector1.directionToTarget(testVector2);
        //assert
        assertAll("Direction vector to a target.",
                () -> assertEquals(resultX, round(finalTestVector.getX()), "The x value should be correct."),
                () -> assertEquals(resultY, round(finalTestVector.getY()), "The y value should be correct."),
                () -> assertEquals(resultX == 0 && resultY == 0 ? 0 : 1, round(finalTestVector.length()), "The length of the vector should be 1 or 0.")
        );
    }

    @DisplayName("Get the correct angle of a vector")
    @ParameterizedTest
    @CsvSource({"0, 0, 0", "27, 187, -1.4274", "1300, 18, -0.0138", "-60, -4, 3.0750"})
    void angleToVector(double x, double y, double result) {
        //arrange
        Vector2D testVector = new Vector2D(x, y);
        //act
        double angle = Vector2D.angleToVector(testVector);
        //assert
        assertEquals(result, round(angle), "The angle should be correct.");
    }
}