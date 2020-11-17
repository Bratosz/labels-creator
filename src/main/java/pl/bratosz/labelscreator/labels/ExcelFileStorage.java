package pl.bratosz.labelscreator.labels;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bratosz.labelscreator.date.CurrentDate;
import pl.bratosz.labelscreator.payload.UploadFileResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class ExcelFileStorage {
    private Path fileStorageLocation;
    private String xlsxFileName;
    private XSSFWorkbook workbook;
    private UploadFileResponse uploadFileResponse;
    private String path;

    public ExcelFileStorage() {
        try {
            fileStorageLocation = Files.createTempDirectory("temp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("java.io.tmpdir", fileStorageLocation.toAbsolutePath().toString());
        path = System.getProperty("java.io.tmpdir");
    }


    public UploadFileResponse storeFile(XSSFWorkbook workbook) {
        this.workbook = workbook;
        createResponse();
        try {
            saveWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadFileResponse;
    }

    private void saveWorkbook() throws Exception {
        Path targetLocation = fileStorageLocation.resolve(xlsxFileName);
        String xlsxFileLocation = targetLocation.toAbsolutePath().toString();


        FileOutputStream fileOut =
                new FileOutputStream(xlsxFileLocation);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

    }

    private void createResponse() {
        createXLSXFileName();
        String uri = createDownloadURI();
        uploadFileResponse =
                new UploadFileResponse(xlsxFileName, uri, ".xlsx", 0l);
    }

    private String createDownloadURI() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/download/")
                .path(xlsxFileName)
                .toUriString();
    }

    private void createXLSXFileName() {
        CurrentDate date = new CurrentDate();
        xlsxFileName = workbook.getSheetName(0) + "_" + date.getDate() + ".xlsx";
    }
}
