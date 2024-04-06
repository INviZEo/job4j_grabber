package ru.job4j.grabber.utils;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.job4j.grabber.HabrCareerParse;

class HabrCareerDateTimeParserTest {

    @Test
    void timeParse() {
        String date = "2024-02-21T18:21:56+03:00";
        HabrCareerDateTimeParser hb = new HabrCareerDateTimeParser();
        Assertions.assertEquals("2024-02-21T18:21:56", hb.parse(date).toString());
    }
}