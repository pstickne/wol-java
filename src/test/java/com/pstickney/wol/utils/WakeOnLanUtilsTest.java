package com.pstickney.wol.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WakeOnLanUtilsTest
{
    @Test
    public void btosTest()
    {
        assertEquals("00 ", WakeOnLanUtils.btos(new byte[]{0}));
        assertEquals("01 ", WakeOnLanUtils.btos(new byte[]{1}));
        assertEquals("02 ", WakeOnLanUtils.btos(new byte[]{2}));
        assertEquals("03 ", WakeOnLanUtils.btos(new byte[]{3}));
        assertEquals("04 ", WakeOnLanUtils.btos(new byte[]{4}));
        assertEquals("05 ", WakeOnLanUtils.btos(new byte[]{5}));
        assertEquals("06 ", WakeOnLanUtils.btos(new byte[]{6}));
        assertEquals("07 ", WakeOnLanUtils.btos(new byte[]{7}));
        assertEquals("08 ", WakeOnLanUtils.btos(new byte[]{8}));
        assertEquals("09 ", WakeOnLanUtils.btos(new byte[]{9}));
        assertEquals("0A ", WakeOnLanUtils.btos(new byte[]{10}));
        assertEquals("0B ", WakeOnLanUtils.btos(new byte[]{11}));
        assertEquals("0C ", WakeOnLanUtils.btos(new byte[]{12}));
        assertEquals("0D ", WakeOnLanUtils.btos(new byte[]{13}));
        assertEquals("0E ", WakeOnLanUtils.btos(new byte[]{14}));
        assertEquals("0F ", WakeOnLanUtils.btos(new byte[]{15}));
        assertEquals("10 ", WakeOnLanUtils.btos(new byte[]{16}));
    }

    @Test
    public void stobTest()
    {
        assertEquals((byte)0, WakeOnLanUtils.stob("00"));
        assertEquals((byte)1, WakeOnLanUtils.stob("01"));
        assertEquals((byte)2, WakeOnLanUtils.stob("02"));
        assertEquals((byte)3, WakeOnLanUtils.stob("03"));
        assertEquals((byte)4, WakeOnLanUtils.stob("04"));
        assertEquals((byte)5, WakeOnLanUtils.stob("05"));
        assertEquals((byte)6, WakeOnLanUtils.stob("06"));
        assertEquals((byte)7, WakeOnLanUtils.stob("07"));
        assertEquals((byte)8, WakeOnLanUtils.stob("08"));
        assertEquals((byte)9, WakeOnLanUtils.stob("09"));
        assertEquals((byte)10, WakeOnLanUtils.stob("0A"));
        assertEquals((byte)11, WakeOnLanUtils.stob("0B"));
        assertEquals((byte)12, WakeOnLanUtils.stob("0C"));
        assertEquals((byte)13, WakeOnLanUtils.stob("0D"));
        assertEquals((byte)14, WakeOnLanUtils.stob("0E"));
        assertEquals((byte)15, WakeOnLanUtils.stob("0F"));
        assertEquals((byte)16, WakeOnLanUtils.stob("10"));
    }
}
