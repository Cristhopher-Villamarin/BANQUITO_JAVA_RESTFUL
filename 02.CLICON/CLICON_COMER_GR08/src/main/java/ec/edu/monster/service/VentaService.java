package ec.edu.monster.service;

import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.VentaRequest;
import ec.edu.monster.model.VentaResponse;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class VentaService {
    
    public VentaResponse procesarVenta(VentaRequest ventaRequest) throws IOException, ParseException {
        return HttpClientUtil.post("/ventas/procesar", ventaRequest, VentaResponse.class);
    }
}
