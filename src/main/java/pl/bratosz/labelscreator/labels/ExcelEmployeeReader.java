package pl.bratosz.labelscreator.labels;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.model.Employee;

import java.util.LinkedList;
import java.util.List;

public class ExcelEmployeeReader {

    private final XSSFSheet sheet;
    private List<Employee> employees;
    private int firstNameIndex;
    private int lastNameIndex;
    private int lockerNumberIndex;
    private int boxNumberIndex;

    public ExcelEmployeeReader(XSSFWorkbook workbook) {
        sheet = getSheet(workbook);
        employees = new LinkedList<>();
    }

    public static ExcelEmployeeReader create(XSSFWorkbook workbook) {
        return new ExcelEmployeeReader(workbook);
    }

    public List<Employee> loadEmployees() {
        assignColumnsToDataType();
        loadEmployeesFromAllRows();
        return employees;
    }

    private void loadEmployeesFromAllRows() {
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if(isRowEmpty(row)){
                continue;
            }
            Employee employee = loadEmployeeFromRow(row);
            employees.add(employee);
        }

    }

    private Employee loadEmployeeFromRow(Row row) {
        return new Employee(
                row.getCell(firstNameIndex).getStringCellValue(),
                row.getCell(lastNameIndex).getStringCellValue(),
                (int) row.getCell(lockerNumberIndex).getNumericCellValue(),
                (int) row.getCell(boxNumberIndex).getNumericCellValue());
    }

    private boolean isRowEmpty(Row row) {
        if(null == row.getCell(firstNameIndex)) {
            return true;
        } else {
            return false;
        }
    }

    private void assignColumnsToDataType() {
        XSSFRow row = sheet.getRow(0);
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            if (row.getCell(i).getStringCellValue().equals(null)) break;
            String val = row.getCell(i).getStringCellValue().toUpperCase().trim();
            switch (val) {
                case "IMIĘ":
                case "IMIE":
                case "NAME":
                case "FIRSTNAME":
                    firstNameIndex = i;
                    break;
                case "NAZWISKO":
                case "LASTNAME":
                case "SURNAME":
                    lastNameIndex = i;
                    break;
                case "SZAFKA":
                case "SZAFA":
                case "LOCKER":
                    lockerNumberIndex = i;
                    break;
                case "BOX":
                case "SKRYTKA":
                    boxNumberIndex = i;
                    break;
                default:
                    break;
            }
        }

    }

    private XSSFSheet getSheet(XSSFWorkbook workbook) {
        return workbook.getSheetAt(0);
    }

    public int getFirstNameIndex() {
        return firstNameIndex;
    }

    public int getLastNameIndex() {
        return lastNameIndex;
    }

    public int getLockerNumberIndex() {
        return lockerNumberIndex;
    }

    public int getBoxNumberIndex() {
        return boxNumberIndex;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
