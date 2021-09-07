package com.group17.teddysbrochure;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group17.teddysbrochure.ui.util.CustomUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UtilTest {

    static CustomUtil util;
    static String fileName = "test.txt";

    @Before
    public void instantiateUtilObject() {
        util = CustomUtil.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }


    private String getSaltString() {
        String Salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * Salt.length());
            salt.append(Salt.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @Test
    public void internetConnection() {
        assertEquals(true, util.isNetworkAvailable());
    }

    @Test
    public void inputStreamToString_Test() throws IOException {
        String randomText = getSaltString() + "\n";

        InputStream tempStream = new ByteArrayInputStream(randomText.getBytes());
        String tempRandomText = util.convertInputStreamToString(tempStream);
        assertEquals(randomText, tempRandomText);
    }

    @Test
    public void inputStreamToString_TestTrim() throws IOException {
        String randomText = getSaltString();

        InputStream tempStream = new ByteArrayInputStream(randomText.getBytes());
        String tempRandomText = util.convertInputStreamToString(tempStream);
        assertEquals(randomText, tempRandomText.trim());
    }

    @Test
    public void inputStreamToString_Test2() throws IOException {
        String randomText = "";
        InputStream tempStream = new ByteArrayInputStream(randomText.getBytes());
        String tempRandomText = util.convertInputStreamToString(tempStream);
        assertEquals(randomText, tempRandomText);
    }

    @Test
    public void writeAndReadInternalStorage() {
        util.writeToInternalStorage(fileName, "writeIntoIt");
        String data = util.readFromInternalStorage(fileName);
        assertEquals("writeIntoIt\n", data);
    }

    @Test
    public void writeAndReadInternalStorage2() {
        util.writeToInternalStorage(fileName, "s");
        String data = util.readFromInternalStorage(fileName);
        assertEquals("s", data.trim());
    }

    @Test
    public void writeAndReadInternalStorageLong() {
        util.writeToInternalStorage(fileName, "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAcFBQYFBAcGBgYIBwcICxELCgkJCxUPEA0RGBUaGRgWGBcbHiYhHB0lHhcZIi4jJSgrLS0tHCMxN");
        String data = util.readFromInternalStorage(fileName);
        assertEquals("/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAcFBQYFBAcGBgYIBwcICxELCgkJCxUPEA0RGBUaGRgWGBcbHiYhHB0lHhcZIi4jJSgrLS0tHCMxN", data.trim());
    }

    @Test
    public void urlTo64() throws IOException{
        String base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAcFBQYFBAcGBgYIBwcICxELCgkJCxUPEA0RGBUaGRgWGBcbHiYhHB0lHhcZIi4jJSgrLS0tHCMxN";
        String convertedBase64 = util.urlToBase64("http://45.79.221.157:3000/images/deathvalley/DeathValleyImage1.png");
        if (convertedBase64.startsWith(base64))
            assertEquals(true, true);
        else
            assertEquals(false, false);
    }

    @Test
    public void urlTo64End() throws IOException{
        String base64 = "Lgy0mVGTMcdI6O2PlT8YJZ64Z7PQ+9kb4hPJDS8kjJuUYoxyZWjHF3sTPKlRl25FPkJmKFjtCtMhUOBujx10LLGjRcDJcLRC4lSMbVnsi2dGmtnmoYtE7Fi5ZDjsnKGSUSipG6IRNkOj7pfQljD7ElPb/8A4Q4Fi4PZ0TMEOBaLaPHCOzOl0OIboTU+jFISbSJtiWyEs0VLsz0lTJZNon7c9kDWMjWiyfpwv4Hhl4vohQ9nkpPuLFSfZ9s5bMcocbHpsoWeNdE2QxZOE6FgpS/R91EryRCbPKU+jHBwkeVoWGcIeV49n/U+08XDLoW1snZ0RrY077E2l6Llj8JxMsmNxgtsmvRbUbLXj/JpLsSckv7SaJUChpnXEqSztHobzUFFE4w+/wD8DgWSlEUxRI2o7NoqybFkipITbG3syebfoSsuDxxYssfInJJ9cdo0iL48YF/DJE92JKETiLKmJOEKaE7Qk65limGPo9noc0SdEuejpEFyWXxB/9k=";
        String convertedBase64 = util.urlToBase64("http://45.79.221.157:3000/images/deathvalley/DeathValleyImage1.png");
        if (convertedBase64.endsWith(base64))
            assertEquals(true, true);
        else
            assertEquals(false, false);
    }

    @Test
    public void urlTo64Contain() throws IOException{
        String base64 = "EOOetmO71CququrqchmzBiI5fNFr1S61uNB1Ocznttlt1Vh53NzY4jmUxO1BQCKkBIAQAwaTQOJwJ10r3XLJbZjCTjPNMlRA6bdMqZdKW3dsh2NSyRJOmAKnermXmMzkqkUxuhEjmIHTTB";
        String convertedBase64 = util.urlToBase64("http://45.79.221.157:3000/images/deathvalley/DeathValleyImage1.png");
        if (convertedBase64.contains(base64))
            assertEquals(true, true);
        else
            assertEquals(false, false);
    }
}
