package pl.bratosz.labelscreator.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.exception.FileStorageException;
import pl.bratosz.labelscreator.exception.WrongFileFormatException;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.payload.UploadFileResponse;
import pl.bratosz.labelscreator.s3.S3Services;
import pl.bratosz.labelscreator.service.LabelsService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/labels")
public class LabelsController {
    private LabelsService labelsService;
    private FileController fileController;
    private S3Services s3Services;



    public LabelsController(
            LabelsService labelsService, FileController fileController, S3Services s3Services) {
        this.labelsService = labelsService;
        this.fileController = fileController;
        this.s3Services = s3Services;
    }

    @PostMapping("/create/{labelsFormat}/{editorSpreadSheetType}")
    public UploadFileResponse create(
            @PathVariable LabelsFormat labelsFormat, @PathVariable EditorSpreadSheetType editorSpreadSheetType,
            @RequestParam("file")MultipartFile file) {
        try {
            XSSFWorkbook workbook = extractWorkbookFromFile(file);
            return labelsService.create(workbook, labelsFormat, editorSpreadSheetType);

        } catch (WrongFileFormatException e) {
            String message = e.getMessage();
            throw new FileStorageException("Niewłaściwy format pliku: " + message);
        } catch (IOException e) {
            throw new FileStorageException("Coś nie pykło");
        }
    }

    @PostMapping("/generate_in_zpl2/{labelsFormat}")
    public String generateInZPL2(@PathVariable LabelsFormat labelsFormat,
                                 @RequestBody List<Employee> employees) {
        return labelsService.generateInZPL2(labelsFormat, employees);
    }


    @PostMapping("/create/from_table/{labelsFormat}/{editorSpreadSheetType}")
    public UploadFileResponse createFromList(
            @PathVariable LabelsFormat labelsFormat, @PathVariable EditorSpreadSheetType editorSpreadSheetType,
            @RequestBody List<Employee> employees) {
        return labelsService.createFromList(employees, labelsFormat, editorSpreadSheetType);
    }

    private XSSFWorkbook extractWorkbookFromFile(MultipartFile file) throws IOException {
        if(isFileFormatCorrect(file) && file.getSize() > 0) {
            return getXSSFWorkbook(file);
        } else {
            throw new WrongFileFormatException(getFileExtension(file));
        }
    }

    private XSSFWorkbook getXSSFWorkbook(MultipartFile file) throws IOException {
        if(getFileExtension(file).equals(".xlsx")) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            throw new WrongFileFormatException(getFileExtension(file));
        }
    }


    private boolean isFileFormatCorrect(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(isFileFormatXLSX(fileName) || isFileFormatXLS(fileName)){
            return true;
        }
        else return false;
    }

    private boolean isFileFormatXLS(String fileName) {
        return fileName.endsWith(".xls");
    }

    private boolean isFileFormatXLSX(String fileName) {
       return fileName.endsWith(".xlsx");
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int dotIndex = fileName.indexOf(".");
        return fileName.substring(dotIndex);
    }

}
