package pl.bratosz.labelscreator.labels;

public class CornerContentGenerator {
    public static String generateForLockerAndBox(int cornerContentLockerCapacity, int actualMainBoxNumber) {
        int lockerNumber;
        int boxNumber;
        if(actualMainBoxNumber <= cornerContentLockerCapacity) {
            lockerNumber = 1;
            boxNumber = actualMainBoxNumber;
        } else {
            lockerNumber = calculateLockerNumber(cornerContentLockerCapacity, actualMainBoxNumber);
            boxNumber = calculateBoxNumber(cornerContentLockerCapacity, actualMainBoxNumber, lockerNumber);
        }
        return lockerNumber + "/" + boxNumber;
    }

    private static int calculateBoxNumber(int lockerCapacity, int actualMainBoxNumber, int lockerNumber) {
        return actualMainBoxNumber - ((lockerNumber - 1) * lockerCapacity);
    }

    private static int calculateLockerNumber(int lockerCapacity, int actualMainBoxNumber) {
        return ((actualMainBoxNumber - 1) / lockerCapacity) + 1;
    }
}
