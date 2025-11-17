/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controller;

import ec.edu.monster.model.CreditoEvaluacionResponse;
import ec.edu.monster.service.BanquitoService;

public class CreditoEstadoController {

    private final BanquitoService banquitoService = new BanquitoService();

    /**
     * Verifica si una persona es sujeto de crédito según su número de cédula.
     */
    public CreditoEvaluacionResponse verificarSujetoCredito(String cedula) throws Exception {
        return banquitoService.verificarSujetoCredito(cedula);
    }
}
