package ec.edu.monster.model;

public class FotoUploadRequest {
    private String base64;
    private String fileName;

    public FotoUploadRequest() {
    }

    public FotoUploadRequest(String base64, String fileName) {
        this.base64 = base64;
        this.fileName = fileName;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
