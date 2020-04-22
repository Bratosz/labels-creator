package pl.bratosz.labelscreator.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pl.bratosz.labelscreator.excel.ExcelEmployeeReader;
import pl.bratosz.labelscreator.excel.ExcelFileStorage;
import pl.bratosz.labelscreator.excel.LabelsCreator;
import pl.bratosz.labelscreator.excel.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.notification.MailNotificator;
import pl.bratosz.labelscreator.notification.SSLMailNotificator;
import pl.bratosz.labelscreator.payload.UploadFileResponse;


import java.util.List;

@Service
public class LabelsService {

    public UploadFileResponse create(XSSFWorkbook workbook, LabelsFormat labelsFormat) {
        ExcelEmployeeReader employeeReader = new ExcelEmployeeReader(workbook);
        List<Employee> loadedEmployees;
        LabelsCreator labelsCreator = new LabelsCreator(labelsFormat);
        ExcelFileStorage fileStorage = new ExcelFileStorage();

        SSLMailNotificator ssl = new SSLMailNotificator();
        ssl.message();

        loadedEmployees = employeeReader.loadEmployees();
        XSSFWorkbook labels = labelsCreator.create(loadedEmployees);



        return fileStorage.storeFile(labels);
    }
}
