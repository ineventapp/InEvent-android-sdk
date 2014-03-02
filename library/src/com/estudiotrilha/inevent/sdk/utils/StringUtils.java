package com.estudiotrilha.inevent.sdk.utils;


public class StringUtils
{
    /**
     * Checks if <b>value</b> is null.
     * 
     * @param value
     *            Original value, where the null check will be performed
     * @param fallback
     *            If the value passed is null, this will be returned
     * 
     * @return <b>fallback</b> if <b>value</b> is null and <b>value</b>
     *         otherwise.
     */
    public static String nullGuard(String value, String fallback)
    {
        if (value == null) return fallback;
        return value;
    }

    /**
     * Checks if <b>value</b> is null.
     * 
     * @param value
     *            Original value, where the null check will be performed
     * @param message
     *            If the value passed is null, an
     *            {@link IllegalArgumentException} will be thrown with this
     *            message
     * 
     * @return <b>value</b> if it is not null, an exception will be thrown
     *         otherwise.
     * 
     * @exception IllegalArgumentException
     *                if <b>value</b> is null
     */
    public static String argumentNullCheck(String value)
    {
        if (value == null) throw new IllegalArgumentException(value+" is a required argument!");
        return value;
    }
}
