package com.kodilla.clinic.http;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpRequestsTest {
    @Test
    public void testArrayStringFormat_ForHttpBody() {
        List<Integer> integers = new ArrayList<>();
        integers.add(233);
        integers.add(561);
        System.out.println(integers);

        int[] ints = new int[2];
        ints[0] = 100;
        ints[1] = 200;
        System.out.println(Arrays.toString(ints));

    }
}
