/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librasys;

import librasys.model.Librarian;
import librasys.model.Member;

/**
 *
 * @author AmmarPasifiky
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Member member = new Member(
                "USR001",
                "Ammar",
                "ammar@mail.com",
                "12345",
                "MBR001",
                true
        );

        Librarian librarian = new Librarian(
                "USR002",
                "Siti",
                "siti@mail.com",
                "admin123",
                "EMP001"
        );

        System.out.println("=== MEMBER INFO ===");
        member.displayInfo();
        System.out.println("Role: " + member.getRole());
        System.out.println("Login success: "
                + member.login("ammar@mail.com", "12345"));
        System.out.println();

        System.out.println("=== LIBRARIAN INFO ===");
        librarian.displayInfo();
        System.out.println("Role: " + librarian.getRole());
        System.out.println("Login success: "
                + librarian.login("siti@mail.com", "admin123"));
    }
    
}
