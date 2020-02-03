package pl.bratosz.labelscreator.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import pl.bratosz.labelscreator.excel.ExcelEmployeeReader;
import pl.bratosz.labelscreator.excel.ExcelFileStorage;
import pl.bratosz.labelscreator.excel.LabelsCreator;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.payload.UploadFileResponse;


import java.util.List;

@Service
public class LabelsService {

    public UploadFileResponse create(XSSFWorkbook workbook) {
        ExcelEmployeeReader employeeReader = new ExcelEmployeeReader(workbook);
        List<Employee> loadedEmployees;
        LabelsCreator labelsCreator = new LabelsCreator();
        ExcelFileStorage fileStorage = new ExcelFileStorage();

        loadedEmployees = employeeReader.loadEmployees();
        XSSFWorkbook labels = labelsCreator.create(loadedEmployees);

        return fileStorage.storeFile(labels);
    }
}
