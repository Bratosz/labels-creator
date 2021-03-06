package pl.bratosz.labelscreator.labels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bratosz.labelscreator.labels.format.FontName;
import static org.assertj.core.api.Assertions.*;


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

    @Test
    void whenFontIsCorrectShouldSetThatValue(){
        //given
        FontName fontName = FontName.TIMES_NEW_ROMAN;
        String expectedFont = "Times New Roman";
        String actualFont;
        //when
        labelsSheetParameters.setFontName(fontName);
        actualFont = labelsSheetParameters.getFontName();
        //then
        assertThat(actualFont).isEqualTo(expectedFont);
    }
}