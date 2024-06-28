package com.project.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.connection.Connector;
import com.project.modele.Detail_devis;
import com.project.modele.Detail_typetravaux;
import com.project.modele.Devis;
import com.project.modele.Finition;
import com.project.modele.Histogramme;
import com.project.modele.Liste_devis;
import com.project.modele.Typetravaux;
import com.project.modele.Unite;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/detail-devis")
    public String getDetailDevis(String iddevis, Model model) {
        Connection connection = null;

        try {
            connection = Connector.connect();
            LinkedList<Detail_devis> lists = new Detail_devis().select(connection, " WHERE iddevis='" + iddevis + "'");
            model.addAttribute("lists", lists);
            model.addAttribute("iddevis", iddevis);
            Devis devis = new Devis().selectById(connection, iddevis);
            model.addAttribute("lists", lists);
            model.addAttribute("iddevis", iddevis);
            model.addAttribute("totatlTravaux", devis.getSommeTravauxString());
            model.addAttribute("somme", devis.getMontantString());
            model.addAttribute("lieu", devis.getLieu());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.CloseConnection(connection);

        }

        model.addAttribute("page", "detail-devis");
        model.addAttribute("title", "");
        return "index";
    }

    @PostMapping("/admin/update-travaux")
    public String updateTravaux(String nom, String unite, double pu, String id) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            Typetravaux typetravaux = new Typetravaux(nom, unite, pu);
            typetravaux.setId(id);
            typetravaux.updateTravaux(connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Connector.Rollback(connection);
            return "redirect:/admin/update-form-travaux?id=" + id + "&message=" + e.getMessage();
        } finally {
            Connector.CloseConnection(connection);
        }
        return "redirect:/admin/list-travaux";
    }

    @PostMapping("/admin/update-finition")
    public String updateFinition(double pourcentage, String id) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            Finition finition = new Finition(pourcentage);
            finition.setId(id);
            finition.updateFinition(connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Connector.Rollback(connection);
            return "redirect:/admin/update-form-finition?id=" + id + "&message=" + e.getMessage();
        } finally {
            Connector.CloseConnection(connection);
        }
        return "redirect:/admin/list-finition";
    }

    @GetMapping("/admin/update-form-travaux")
    public String updateForm(String id, @RequestParam(defaultValue = "") String message, Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();

            LinkedList<Unite> unites = new Unite().select();
            Typetravaux typetravaux = (Typetravaux) new Typetravaux().select(connection, " WHERE id='" + id + "'")
                    .getFirst();
            model.addAttribute("travaux", typetravaux);
            model.addAttribute("unites", unites);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            Connector.CloseConnection(connection);
        }
        model.addAttribute("page", "update-travaux");
        model.addAttribute("message", message);
        model.addAttribute("title", "Modification travaux");
        return "index";
    }

    @GetMapping("/admin/update-form-finition")
    public String updateFormFinition(String id, @RequestParam(defaultValue = "") String message, Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            Finition finition = (Finition) new Finition().select(connection, "WHERE id='" + id + "'").getFirst();
            model.addAttribute("finition", finition);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            Connector.CloseConnection(connection);
        }

        model.addAttribute("page", "update-finition");
        model.addAttribute("message", message);
        model.addAttribute("title", "Modification pourcentage finition");
        return "index";
    }

    @GetMapping("/admin/list-finition")
    public String getListFinition(Model model) {
        try {
            LinkedList<Finition> finitions = new Finition().select();
            model.addAttribute("lists", finitions);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("title", "Liste des types de finition");
        model.addAttribute("page", "list-finition");
        return "index";
    }

    @GetMapping("/admin/list-travaux")
    public String getListTravaux(Model model) {
        try {
            LinkedList<Detail_typetravaux> lists = new Detail_typetravaux().select();
            model.addAttribute("lists", lists);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("title", "Liste des types de travaux");
        model.addAttribute("page", "list-travaux");
        return "index";
    }

    @GetMapping("/admin/reinitialisation")
    public String reinitPage(@RequestParam(defaultValue = "") String message, Model model) {
        model.addAttribute("page", "reinitialisation");
        model.addAttribute("title", "");
        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping("/admin/reinit")
    public String reinit(Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            LinkedList<String> listSql = new LinkedList<>();
            listSql.add("erreur_data");
            listSql.add("csv_paiement");
            listSql.add("csv_devis");
            listSql.add("csv_travaux_maison");
            listSql.add("travauxDevis");
            listSql.add("travauxmaison");
            listSql.add("typeTravaux");
            listSql.add("unite");
            listSql.add("travaux");
            listSql.add("paiement");
            listSql.add("detail_devis");
            listSql.add("devis");
            listSql.add("duree");
            listSql.add("finition");
            listSql.add("desc_maison");
            listSql.add("maison");
            listSql.add("client");

            for (String string : listSql) {
                String sql = "DELETE FROM " + string;
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                Connector.CloseStatement(statement);
            }
            connection.commit();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.Rollback(connection);
        } finally {

            Connector.CloseConnection(connection);
        }
        return "redirect:/admin/reinitialisation?message=success";
    }

    @GetMapping("/admin/dashboard")
    public String getDashboard(@RequestParam(defaultValue = "2023") String annee, Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            String totalMontant = new Liste_devis().getSumTotalString();
            String totalPaiement = new Liste_devis().getSumPayeString();
            model.addAttribute("totalMontant", totalMontant);
            model.addAttribute("totalPaiement", totalPaiement);

            Histogramme histogramme = new Histogramme();
            LinkedList<String> labels = histogramme.getLabels(annee, connection);
            LinkedList<Double> data = histogramme.getDatas(annee, connection);

            model.addAttribute("data", data);
            model.addAttribute("labels", labels);
            connection.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.CloseConnection(connection);
        }
        model.addAttribute("page", "dashboard");
        model.addAttribute("annee", annee);
        model.addAttribute("title", "Tableau de bord");
        return "index";
    }

    @GetMapping("/admin/list-devis-admin")
    public String getListDevis(Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            LinkedList<Liste_devis> lists = new Liste_devis().select(connection);
            connection.close();
            model.addAttribute("lists", lists);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.CloseConnection(connection);
        }
        model.addAttribute("page", "list-devis-admin");
        model.addAttribute("title", "Liste devis");
        return "index";
    }

}
