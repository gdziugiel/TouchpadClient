package com.example.student.touchpadclient;

import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class TouchpadActivityTest {

    private TouchpadActivity touchpadActivity = null;
    private String rozdzielczosc = "1366 x 768";
    private String host = "DESKTOP-5O6S7VO";
    private String port = "49152";
    private int esx = 1366;
    private int esy = 768;
    private int ekx = 0;
    private int eky = 0;
    private int ekx1 = 882;
    private int eky1 = 1449;
    private int ekx2 = 1297;
    private int eky2 = 720;

    @Rule
    public ActivityTestRule<TouchpadActivity> activityTestRule = new ActivityTestRule<>(TouchpadActivity.class);

    @Rule
    public IntentsTestRule<TouchpadActivity> mActivityRule = new IntentsTestRule<TouchpadActivity>(TouchpadActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("host", host);
            intent.putExtra("port", port);
            return intent;
        }
    };

    @Before
    public void setUp() throws Exception {
        touchpadActivity = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        touchpadActivity = null;
    }

    @Test
    public void wczytanieInterfejsu() {
        LinearLayout touchpadLayout = touchpadActivity.findViewById(R.id.tuchpadLayout);
        LinearLayout scrollLayout = touchpadActivity.findViewById(R.id.scrollLayout);
        Button lewy = touchpadActivity.findViewById(R.id.lewy);
        Button srodkowy = touchpadActivity.findViewById(R.id.srodkowy);
        Button prawy = touchpadActivity.findViewById(R.id.prawy);
        TextView ipVal = touchpadActivity.findViewById(R.id.ipVal);
        TextView rozdzielczoscVal = touchpadActivity.findViewById(R.id.rozdzielczoscVal);
        TextView pozycjaVal = touchpadActivity.findViewById(R.id.pozycjaVal);
        assertNotNull(touchpadLayout);
        assertNotNull(scrollLayout);
        assertNotNull(lewy);
        assertNotNull(srodkowy);
        assertNotNull(prawy);
        assertNotNull(ipVal);
        assertNotNull(rozdzielczoscVal);
        assertNotNull(pozycjaVal);
    }

    @Test
    public void pobranieRozdzielczosci() {
        onView(withId(R.id.rozdzielczoscVal)).check(matches(withText(rozdzielczosc)));
    }

    @Test
    public void skalowanie() {
        final LinearLayout touchpadLayout = touchpadActivity.findViewById(R.id.tuchpadLayout);
        touchpadLayout.post(new Runnable() {
            @Override
            public void run() {
                double wspolczynnikX = ((double) touchpadActivity.getEx() / touchpadLayout.getWidth());
                double wspolczynnikY = ((double) touchpadActivity.getEy() / touchpadLayout.getHeight());
                if (touchpadLayout.getWidth() < touchpadLayout.getHeight()) {
                    ekx = ekx1;
                    eky = eky1;
                } else {
                    ekx = ekx2;
                    eky = eky2;
                }
                assertEquals((double) esx / ekx, wspolczynnikX, 0.1);
                assertEquals((double) esy / eky, wspolczynnikY, 0.1);
            }
        });
    }

}