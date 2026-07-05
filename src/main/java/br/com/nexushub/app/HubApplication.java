package br.com.nexushub.app;

import br.com.nexushub.view.MenuPrincipal;

public class HubApplication {

    public static void main(String[] args) {
        System.out.println("A iniciar Nexus Hub...");

        MenuPrincipal menu = new MenuPrincipal();
        menu.exibirMenu();

        System.out.println("Aplicação encerrada.");
    }
}