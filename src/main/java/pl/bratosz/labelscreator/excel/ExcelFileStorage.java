package pl.bratosz.labelscreator.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bratosz.labelscreator.date.CurrentDate;
import pl.bratosz.labelscreator.payload.UploadFileResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ExcelFileStorage {
    private Path fileStorageLocation;
    private String fileName;
    private XSSFWorkbook workbook;
    private UploadFileResponse uploadFileResponse;

    public ExcelFileStorage() {
        try {
            fileStorageLocation = Files.createTempDirectory("temp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public UploadFileResponse storeFile (XSSFWorkbook workbook) {
        this.workbook = workbook;
        createResponse();
        try {
            saveWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadFileResponse;
    }

    private void saveWorkbook() throws IOException {
        Path targetLocation = fileStorageLocation.resolve(fileName);

        FileOutputStream fileOut =
                new FileOutputStream(targetLocation.toAbsolutePath().toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private void createResponse() {
        createFileName();
        String uri = createDownloadURI();
        uploadFileResponse =
                new UploadFileResponse(fileName, uri, ".xlsx", 0l);
    }

    private String createDownloadURI() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/download/")
                .path(fileName)
                .toUriString();
    }

    private void createFileName() {
        CurrentDate date = new CurrentDate();
        fileName = workbook.getSheetName(0) + "_" + date.getDate() + ".xlsx";
    }
}
