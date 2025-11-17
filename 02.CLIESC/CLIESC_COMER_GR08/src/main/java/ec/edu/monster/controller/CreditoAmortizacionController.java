/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controller;

import ec.edu.monster.model.Credito;
import ec.edu.monster.model.TablaAmortizacion;
import ec.edu.monster.service.BanquitoService;

import java.util.List;

public class CreditoAmortizacionController {

    private final BanquitoService banquitoService = new BanquitoService();

    /**
     * Obtiene la tabla de amortización de un crédito por su ID.
     */
    public TablaAmortizacion obtenerTablaAmortizacion(Integer idCredito) throws Exception {
        return banquitoService.obtenerTablaAmortizacion(idCredito);
    }

    /**
     * Obtiene la lista de créditos activos de un cliente dado su número de cédula.
     */
    public List<Credito> obtenerCreditosActivos(String cedula) throws Exception {
        return banquitoService.obtenerCreditosActivos(cedula);
    }
}
