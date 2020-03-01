package pl.bratosz.labelscreator.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bratosz.labelscreator.excel.format.Font;
import pl.bratosz.labelscreator.model.Employee;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExcelEmployeeReaderTest {
    private TestLabelsWorkbook tlb;
    private ExcelEmployeeReader employeeReader;

    @BeforeEach
    public void eachInit(){
        tlb = TestLabelsWorkbook.builder()
                .workbook()
                .headerRow("Imię", "Nazwisko", "Szafa", "Box")
                .build();
    }

    @Test
    void whenAllHeadersAreCorrectShouldAssignTheirIndexesToFields(){
        //given

        //when
        ExcelEmployeeReader employeeReader = new ExcelEmployeeReader(tlb.getWorkbook());
        employeeReader.loadEmployees();
        //then
        assertThat(employeeReader.getFirstNameIndex()).isEqualTo(0);
        assertThat(employeeReader.getLastNameIndex()).isEqualTo(1);
        assertThat(employeeReader.getLockerNumberIndex()).isEqualTo(2);
        assertThat(employeeReader.getBoxNumberIndex()).isEqualTo(3);
    }

    @Test
    void whenEmployeeDataIsCorrectShouldAssignAllEmployees() {
        //given
        tlb.addEmployee("Jan", "Nowak", 1,1);
        tlb.addEmployee("Maria", "Nowak", 1,2);
        tlb.addEmployee("Mieczysław", "Nowak", 1,3);
        //when
        employeeReader = ExcelEmployeeReader.create(tlb.getWorkbook());
        employeeReader.loadEmployees();
        //then
        List<Employee> employees = employeeReader.getEmployees();
        assertThat(employees.size()).isEqualTo(3);
    }

    @Test
    void whenRowWithinIsEmptyShouldAssignAllEmployees() {
        //given
        tlb.addEmployee("Jan", "Nowak", 1,1);
        tlb.addNullEmployee();
        tlb.addEmployee("Maria", "Nowak", 1,2);
        tlb.addEmployee("Mieczysław", "Nowak", 1,3);
        //when
        employeeReader = ExcelEmployeeReader.create(tlb.getWorkbook());
        employeeReader.loadEmployees();
        //then
        List<Employee> employees = employeeReader.getEmployees();
        assertThat(employees.size()).isEqualTo(3);
    }
}