/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controller;

import ec.edu.monster.model.LoginResponse;

public class UserMenuController {

    /**
     * Valida si el usuario autenticado puede acceder al menú USER.
     * Retorna true si es válido, false si no.
     */
    public boolean puedeAcceder(LoginResponse usuario) {

        if (usuario == null) {
            return false;
        }

        if (!usuario.isAutenticado()) {
            return false;
        }

        return "USER".equalsIgnoreCase(usuario.getRol());
    }
}
