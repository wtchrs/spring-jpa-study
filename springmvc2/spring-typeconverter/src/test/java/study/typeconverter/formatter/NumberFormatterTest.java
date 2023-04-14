package study.typeconverter.formatter;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class NumberFormatterTest {

    NumberFormatter numberFormatter = new NumberFormatter();

    @Test
    void parse() throws ParseException {
        Number parse = numberFormatter.parse("1,000", Locale.US);
        assertThat(parse).isEqualTo(1_000L);
    }

    @Test
    void print() {
        String print = numberFormatter.print(1000, Locale.US);
        assertThat(print).isEqualTo("1,000");
    }
}