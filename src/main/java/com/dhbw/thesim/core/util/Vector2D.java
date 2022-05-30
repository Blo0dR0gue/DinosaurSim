package com.dhbw.thesim.core.util;

import java.util.Objects;

/**
 * Represents a 2d-vector for a position on our screen.
 *
 * @author Daniel Czeschner
 */
public class Vector2D {

    //region variables
    /**
     * The x coordinate
     */
    private double x;
    /**
     * The x coordinate
     */
    private double y;
    //endregion

    /**
     * Int constructor
     * @param xValue The value for the x coordinate
     * @param yValue The value for the y coordinate.
     */
    public Vector2D(int xValue, int yValue) {
        x = xValue;
        y = yValue;
    }

    /**
     * Double constructor
     * @param xValue The value for the x coordinate.
     * @param yValue The value for the y coordinate.
     */
    public Vector2D(double xValue, double yValue) {
        x = xValue;
        y = yValue;
    }

    /**
     * Gets the squared length of the 2d-vector
     * @return The square of the length
     */
    public double lengthSq() {
        return x * x + y * y;
    }

    /**
     * Gets the length of the 2d-vector
     * @return The length of this vector
     */
    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    /**
     * Normalizes the Vector
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
     * @param value The amount, which should be multiplied
     * @return This vector
     */
    public Vector2D multiply(double value) {
        x *= value;
        y *= value;
        return this;
    }

    /**
     * Adds another Vector to this vector
     * @param other The other Vector, which should be added
     * @return This vector
     */
    public Vector2D add(Vector2D other) {
        x += other.getX();
        y += other.getY();
        return this;
    }

    /**
     * Subtracts another Vector from this vector
     * @param other The other Vector, which is used to subtract
     * @return This vector
     */
    public Vector2D subtract(Vector2D other) {
        x -= other.getX();
        y -= other.getY();
        return this;
    }

    /**
     * Is this vector the zero-vector?
     * @return True, if it is the zero-vector.
     */
    public boolean isZero() {
        return x == 0.0 && y == 0;
    }

    /**
     * Creates a copy of a passed 2d-vector
     * @param vector The 2d-vector which should be cloned.
     * @return The cloned 2d-vector.
     */
    public static Vector2D copyOf(Vector2D vector) {
        return new Vector2D(vector.getX(), vector.getY());
    }

    /**
     * Gets the distance between two vectors.
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
     * @param v1 The first 2d-vector
     * @param v2 The second 2d-vector
     * @return The dot product
     */
    public static double dotProduct(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    //region getter & setter

    /**
     * Gets the current x coordinate
     * @return The x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the current x coordinate
     * @param x The new x coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the current y coordinate
     * @return The y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the current y coordinate
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
