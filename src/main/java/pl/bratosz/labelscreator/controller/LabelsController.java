package pl.bratosz.labelscreator.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bratosz.labelscreator.labels.format.CornerContentType;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.LabelsOrientation;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
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

    @PostMapping("/create-from-custom-content/zpl2/{customString}/{labelsAmount}")
    public String createLabelsFromCustomString(
            @PathVariable String customString,
            @PathVariable int labelsAmount) {
        return labelsService.createLabelsFromCustomString(customString, labelsAmount);
    }

    @PostMapping("/create-from-table/zpl2/{labelsFormat}/{plantNumber}")
    public String createLabelsInZPL2(
            @PathVariable LabelsFormat labelsFormat,
            @PathVariable String plantNumber,
            @RequestBody List<Employee> employees) {
        return labelsService.createLabelsAsZPL2(labelsFormat, employees, plantNumber);
    }

    @PostMapping("/create-from-range/zpl2" +
            "/{beginNumber}/{endNumber}/{capacity}/{labelsFormat}/{labelsOrientation}")
    public String createNumericLabelsInZPL2(
            @PathVariable int beginNumber,
            @PathVariable int endNumber,
            @PathVariable int capacity,
            @PathVariable LabelsFormat labelsFormat,
            @PathVariable LabelsOrientation labelsOrientation) {
        return labelsService.createNumericLabelsAsZPL2(
                beginNumber, endNumber, capacity, labelsFormat, labelsOrientation);
    }

    @PostMapping("/create-with-custom-boxes-range-and-custom-corner-content/zpl2" +
            "/{lockerNumber}/{startingBoxNumber}/{endBoxNumber}/{labelsOrientation}/{cornerContentType}/{cornerContent}")
    public String createWithCustomBoxesRangeInZPL2(
            @PathVariable int lockerNumber,
            @PathVariable int startingBoxNumber,
            @PathVariable int endBoxNumber,
            @PathVariable LabelsOrientation labelsOrientation,
            @PathVariable CornerContentType cornerContentType,
            @PathVariable int cornerContent) {
        return labelsService.createNumericLabelsWithCustomBoxesRangeAsZPL2(
                lockerNumber, startingBoxNumber, endBoxNumber, labelsOrientation, cornerContentType, cornerContent);
    }


    @PostMapping("/create_from_table/spread_sheet/{labelsFormat}/{editorSpreadSheetType}/{plantNumber}")
    public UploadFileResponse createSpreadSheet(
            @PathVariable LabelsFormat labelsFormat,
            @PathVariable EditorSpreadSheetType editorSpreadSheetType,
            @PathVariable int plantNumber,
            @RequestBody List<Employee> employees) {
        return labelsService.createSpreadSheet(employees, labelsFormat, editorSpreadSheetType);
    }

    private XSSFWorkbook extractWorkbookFromFile(MultipartFile file) throws IOException {
        if (isFileFormatCorrect(file) && file.getSize() > 0) {
            return getXSSFWorkbook(file);
        } else {
            throw new WrongFileFormatException(getFileExtension(file));
        }
    }

    private XSSFWorkbook getXSSFWorkbook(MultipartFile file) throws IOException {
        if (getFileExtension(file).equals(".xlsx")) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            throw new WrongFileFormatException(getFileExtension(file));
        }
    }


    private boolean isFileFormatCorrect(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (isFileFormatXLSX(fileName) || isFileFormatXLS(fileName)) {
            return true;
        } else return false;
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
