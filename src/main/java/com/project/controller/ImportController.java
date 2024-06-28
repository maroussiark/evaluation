package com.project.controller;

import java.sql.Connection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.project.connection.Connector;
import com.project.modele.Csv_devis;
import com.project.modele.Csv_paiement;
import com.project.modele.Csv_travaux_maison;
import com.project.modele.Erreur_data;

import java.util.LinkedList;

@Controller
public class ImportController {
    @GetMapping("/admin/erreur-csv")
    public String getMethodName(Model model) {
        try {
            LinkedList<Erreur_data> list = new Erreur_data().select();
            model.addAttribute("lists", list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        model.addAttribute("title", "Erreur import");
        model.addAttribute("page", "erreur-import");
        return "index";
    }

    @GetMapping("/admin/import-travaux")
    public String importTravaux(Model model) {

        model.addAttribute("title", "Importation données");
        model.addAttribute("page", "import-travaux");
        return "index";

    }

    @GetMapping("/admin/import-paiement")
    public String importPaiement(Model model) {
        model.addAttribute("title", "Importation données");
        model.addAttribute("page", "import-paiement");
        return "index";
    }

    @PostMapping("/admin/save-travaux")
    public String saveTravaux(MultipartFile travaux, MultipartFile devis) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            boolean erreurTravaux = new Csv_travaux_maison().save_data(travaux, connection);
            boolean erreurDevis = new Csv_devis().save_data(devis, connection);
            connection.commit();
            if (erreurTravaux == true || erreurDevis == true) {
                return "redirect:/admin/erreur-csv";
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.Rollback(connection);
        } finally {
            Connector.CloseConnection(connection);
        }

        return "redirect:/admin/list-devis-admin";
    }

    @PostMapping("/admin/save-paiement")
    public String savePaiement(MultipartFile paiement) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            boolean erreurPaiement = new Csv_paiement().save_data(paiement, connection);
            connection.commit();
            if (erreurPaiement == true) {
                return "redirect:/admin/erreur-csv";
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.Rollback(connection);
        } finally {
            Connector.CloseConnection(connection);
        }

        return "redirect:/admin/list-devis-admin";
    }

}
