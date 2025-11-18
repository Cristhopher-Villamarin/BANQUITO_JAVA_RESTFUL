package ec.edu.monster.dto;

public class FotoUploadResponse {
    private String fotoUrl;

    public FotoUploadResponse() {}

    public FotoUploadResponse(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
