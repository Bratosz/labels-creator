package pl.bratosz.labelscreator.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.bratosz.labelscreator.exception.FileStorageException;
import pl.bratosz.labelscreator.exception.WrongFileFormatException;
import pl.bratosz.labelscreator.payload.UploadFileResponse;
import pl.bratosz.labelscreator.s3.S3Services;
import pl.bratosz.labelscreator.service.LabelsService;

import java.io.IOException;

@RestController
@RequestMapping("/labels")
public class LabelsController {
    private LabelsService labelsService;
    private FileController fileController;
    private S3Services s3Services;


    public LabelsController(
            LabelsService labelsService,
            FileController fileController,
            S3Services s3Services) {
        this.labelsService = labelsService;
        this.fileController = fileController;
        this.s3Services = s3Services;
    }

    @PostMapping("/create")
    public UploadFileResponse create(
            @RequestParam("file")MultipartFile file) {
        try {
            XSSFWorkbook workbook = extractWorkbookFromFile(file);
            return labelsService.create(workbook);
        } catch (IOException e) {
            throw new FileStorageException("Coś nie pykło");
        }
    }

    private XSSFWorkbook extractWorkbookFromFile(MultipartFile file) throws IOException {
        if(isFileFormatXLSX(file) && file.getSize() > 0) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            throw new WrongFileFormatException(getFileExtension(file));
        }
    }

    private boolean isFileFormatXLSX(MultipartFile file) {
       return file.getOriginalFilename().endsWith(".xlsx");
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int dotIndex = fileName.indexOf(".");
        return fileName.substring(dotIndex);
    }

}
