package pl.bratosz.labelscreator.excel;





import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;



@SpringBootTest
class LabelsSheetParametersTest {
    LabelsSheetParameters labelsSheetParameters;

    @BeforeEach
    public void initEach() {
        labelsSheetParameters = new LabelsSheetParameters();
    }


    @Test
    void whenCorrectValueIsGivenShouldSetThisValue() {
        //given
        int fontSize = 20;
        int expectedFontSize = 20;
        int actualFontSize;
        //when
        labelsSheetParameters.setFontSize(fontSize);
        actualFontSize = labelsSheetParameters.getFontSize();
        //then
        assertThat(actualFontSize).isEqualTo(expectedFontSize);
    }

    @Test
    void whenZeroValueIsGivenShouldSetDefaultFontSize() {
        //given
        int fontSize = 0;
        int expectedFontSize = 16;
        int actualFontSize;
        //when
        labelsSheetParameters.setFontSize(fontSize);
        actualFontSize = labelsSheetParameters.getFontSize();
        //then
        assertThat(actualFontSize).isEqualTo(expectedFontSize);
    }

    @Test
    void whenNegativeValueIsGivenShouldConvertToPositive() {
        //given
        int fontSize = -25;
        int expectedFontSize = 25;
        int actualFontSize;
        //when
        labelsSheetParameters.setFontSize(fontSize);
        actualFontSize = labelsSheetParameters.getFontSize();
        //then
        assertThat(actualFontSize).isEqualTo(expectedFontSize);
    }
}