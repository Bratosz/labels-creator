package pl.bratosz.labelscreator.formater;

public class StringFormater {
    public String capitalizeFirstLetters(String s) {
        if(isNameDouble(s)){
            int dashIndex = s.indexOf("-");
            String firstPart = getFirstPartOfWord(s, dashIndex);
            String secondPart = getSecondPartOfWord(s, dashIndex + 1);
            firstPart = capitalizeFirstLetter(firstPart);
            secondPart = capitalizeFirstLetter(secondPart);
            return firstPart + "-" + secondPart;
        } else {
            return capitalizeFirstLetter(s);
        }

    }

    private String getSecondPartOfWord(String s, int beginIndex) {
        return s.substring(beginIndex);
    }

    private String getFirstPartOfWord(String s, int endIndex) {
        return s.substring(0, endIndex);
    }



    private String capitalizeFirstLetter(String s) {
        if (s.equals(null) || s.length() == 0) {
            return "";
        } else if (s.length() == 1) {
            return s.toUpperCase();
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }

    private boolean isNameDouble(String s) {
        if(s.contains("-")) {
            return true;
        } else {
            return false;
        }
    }

}
