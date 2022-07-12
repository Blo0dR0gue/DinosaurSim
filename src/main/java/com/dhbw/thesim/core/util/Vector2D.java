package com.dhbw.thesim.core.util;

import java.util.Objects;

/**
 * Represents a 2d-vector for a position on the screen.
 *
 * @author Daniel Czeschner
 * @apiNote Notice <a href="https://stackoverflow.com/questions/10786587/java-double-precision-sum-trouble">Double precision loss</a>. We did not converted to {@link java.math.BigDecimal} because we do not need this precision.
 * Also, this "mistake" was only found at the end of the implementation phase and would require a lot of effort to fix that.
 */
public class Vector2D {

    //region variables

    /**
     * The x coordinate
     */
    private double x;

    /**
     * The y coordinate
     */
    private double y;

    //endregion

    /**
     * Int constructor
     *
     * @param xValue The value for the x coordinate
     * @param yValue The value for the y coordinate.
     */
    public Vector2D(int xValue, int yValue) {
        x = xValue;
        y = yValue;
    }

    /**
     * Double constructor
     *
     * @param xValue The value for the x coordinate.
     * @param yValue The value for the y coordinate.
     */
    public Vector2D(double xValue, double yValue) {
        x = xValue;
        y = yValue;
    }

    /**
     * Gets the squared length of the 2d-vector
     *
     * @return The square of the length
     */
    public double lengthSq() {
        return (x * x) + (y * y);
    }

    /**
     * Gets the length of the 2d-vector
     *
     * @return The length of this vector
     */
    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    /**
     * Normalizes the Vector
     *
     * @return This vector but normalized
     */
    public Vector2D normalize() {
        double length = this.length();
        x = x == 0 ? 0 : x / length;
        y = y == 0 ? 0 : y / length;
        return this;
    }

    /**
     * Multiplies a value onto this vector
     *
     * @param value The amount, which should be multiplied
     * @return A new {@link Vector2D} with the updated values.
     */
    public Vector2D multiply(double value) {
        return new Vector2D(x * value, y * value);
    }

    /**
     * Adds another vector to this vector
     *
     * @param other The other Vector, which should be added
     * @return A new {@link Vector2D} with the updated values.
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.getX(), y + other.getY());
    }

    /**
     * Adds another Vector to this vector
     *
     * @param value The amount, which should be added.
     * @return A new {@link Vector2D} with the updated values.
     */
    public Vector2D add(double value) {
        return new Vector2D(x + value, y + value);
    }

    /**
     * Subtracts another Vector from this vector
     *
     * @param other The other Vector, which is used to subtract
     * @return A new {@link Vector2D} with the updated values.
     */
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.getX(), y - other.getY());
    }

    /**
     * Is this vector the zero-vector?
     *
     * @return True, if it is the zero-vector.
     */
    public boolean isZero() {
        return x == 0.0 && y == 0;
    }

    /**
     * Checks, if this vector is in range of a target vector.
     *
     * @param target         The other target vector.
     * @param proximityRange The range, in which this vector needs to be.
     * @return true, if we are in range.
     */
    public boolean isInRangeOf(Vector2D target, double proximityRange) {
        return Vector2D.distance(this, target) < proximityRange;
    }

    /**
     * Gets the direction vector to a target. <br>
     * <b>Remember:</b> Y increases from the top to the bottom.
     *
     * @param target The {@link Vector2D} target
     * @return A {@link Vector2D} direction vector.
     */
    public Vector2D directionToTarget(Vector2D target) {
        return new Vector2D(target.getX() - x, target.getY() - y).normalize();
    }

    /**
     * Gets the distance between two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The distance between the first and the second vector.
     */
    public static double distance(Vector2D v1, Vector2D v2) {
        Vector2D helper = new Vector2D(v1.getX() - v2.getX(), v1.getY() - v2.getY());
        return helper.length();
    }

    /**
     * Calculates the dot product for two 2d-vectors
     *
     * @param v1 The first 2d-vector
     * @param v2 The second 2d-vector
     * @return The dot product
     */
    public static double dotProduct(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    /**
     * Calculates the cross product.
     *
     * @param v1 The first 2d-vector.
     * @param v2 The second 2d-vector.
     * @return The cross product.
     */
    public static double crossProduct(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getY() - v1.getY() * v2.getX();
    }

    /**
     * Gets the angle of a direction vector. <br>
     * <b>Remember:</b> Y increases from the top to the bottom.
     *
     * @param direction Used for calculationi the angle
     * @return Angle to vector
     */
    public static double angleToVector(Vector2D direction) {
        //we need to negate y here, because y increases from the top to the bottom inside the simulation (JavaFX).
        return Math.atan2(-direction.getY(), direction.getX());
    }

    //region getter & setter

    /**
     * Gets the current x coordinate
     *
     * @return The x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the current x coordinate
     *
     * @param x The new x coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the current y coordinate
     *
     * @return The y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the current y coordinate
     *
     * @param y The new y coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }
    //endregion

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vector2D other)) {
            return false;
        }
        return x == other.x && y == other.y;
    }
}