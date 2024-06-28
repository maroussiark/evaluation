package com.project.controller;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.config.User;
import com.project.connection.Connector;
import com.project.modele.Detail_devis;
import com.project.modele.Detail_travaux_devis;
import com.project.modele.Devis;
import com.project.modele.Finition;
import com.project.modele.Liste_devis;
import com.project.modele.Maison;
import com.project.modele.Paiement;
import com.project.tools.Formatter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DevisController {

    @GetMapping("/client/detail-devis")
    public String listDetail(String iddevis, Model model) {
        Connection connection = null;

        try {
            connection = Connector.connect();
            connection = Connector.connect();
            LinkedList<Detail_devis> lists = new Detail_devis().select(connection, " WHERE iddevis='" + iddevis + "'");
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
        model.addAttribute("page", "detail-devis-client");
        model.addAttribute("title", "");
        return "index";
    }

    @GetMapping("/client/type-maison")
    public String getTypeMaison(@RequestParam(defaultValue = "") String message, Model model) {
        try {
            Connection connection = Connector.connect();
            LinkedList<Maison> maisons = new Maison().select(connection);
            new Maison().setDescription_List(maisons, connection);
            LinkedList<Finition> finitions = new Finition().select(connection);
            connection.close();

            model.addAttribute("maisons", maisons);
            model.addAttribute("finitions", finitions);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("message", message);
        model.addAttribute("page", "type-maison");
        model.addAttribute("title", "Nouveau devis");
        return "index";
    }

    @PostMapping("/client/cree-devis")
    public String createDevis(String maison, String finition, String date, String lieu, HttpServletRequest request) {
        Connection connection = null;

        try {
            connection = Connector.connect();
            User user = (User) request.getSession().getAttribute("user");
            String idClient = user.getId();
            Devis devis = new Devis(idClient, maison, finition);
            devis.setLieu(lieu);
            devis.create(date, connection);

            connection.commit();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.Rollback(connection);
            String message = e.getMessage();
            if (maison == null) {
                message = "Choisissez une maison";
            }
            return "redirect:/client/type-maison?message=" + message;

        } finally {
            Connector.CloseConnection(connection);
        }
        return "redirect:/client/list-devis";

    }

    @GetMapping("/client/list-devis")
    public String getListDevis(Model model, HttpServletRequest request) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            User user = (User) request.getSession().getAttribute("user");
            LinkedList<Liste_devis> lists = new Liste_devis().select(connection,
                    "WHERE idclient='" + user.getId() + "'");
            connection.close();
            model.addAttribute("lists", lists);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.CloseConnection(connection);
        }
        model.addAttribute("page", "list-devis");
        model.addAttribute("title", "Liste devis");
        return "index";
    }

    @GetMapping("/client/paiement")
    public String paiementForm(String id, @RequestParam(defaultValue = "") String message,
            @RequestParam(defaultValue = "") String type, Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();

            LinkedList<Paiement> paiements = new Paiement().select(connection, " WHERE iddevis='" + id + "'");
            Liste_devis devis = (Liste_devis) new Liste_devis().select(connection, " WHERE id='" + id + "'").getFirst();

            Connector.CloseConnection(connection);
            model.addAttribute("lists", paiements);
            model.addAttribute("reste", devis.getResteString());
            model.addAttribute("total", devis.getPayeString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.CloseConnection(connection);
        }
        model.addAttribute("page", "paiement-form");
        model.addAttribute("id", id);
        model.addAttribute("title", "Paiement");
        model.addAttribute("message", message);
        String typemessage = " alert-success bg-success ";
        if (type.equals("error")) {
            typemessage = " alert-danger bg-danger";
        }
        model.addAttribute("type", typemessage);
        return "index";
    }

    @PostMapping("/client/payer")
    @ResponseBody
    public ResponseEntity<String> payer(double montant, String date, String devis) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            Paiement paiement = new Paiement(devis, montant, date);
            paiement.payer(connection);
            connection.commit();
            return ResponseEntity.ok("Ajouter");
        } catch (Exception e) {
            e.printStackTrace();
            Connector.Rollback(connection);
            // return "redirect:/client/paiement?id=" + devis + "&message=" + e.getMessage()
            // + "&type=error";
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
            Connector.CloseConnection(connection);
        }

        // return "redirect:/client/paiement?id=" + devis + "&message=Paiement
        // effectuer&type=message";
    }

    @GetMapping("/client/export-pdf")
    public String exportPDF(String iddevis, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + iddevis + ".pdf");

        Document document = new Document();

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("RENOVATION & CONSTRUCTION\n"));

        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        table.setWidthPercentage(90);

        Devis devis = new Devis().selectById(iddevis);
        LinkedList<Detail_devis> detail_devis = new Detail_devis().select("WHERE iddevis='" + iddevis + "'");

        document.add(new Paragraph("REF: " + iddevis + "\n\n"));
        document.add(new Paragraph("Client : " + detail_devis.getFirst().getClient() + "\n"));
        document.add(new Paragraph("Lieu : " + devis.getLieu() + "\n\n\n"));
        for (Detail_devis detail : detail_devis) {
            table.addCell(detail.getCode_travaux());
            table.addCell(detail.getTravaux());
            table.addCell(detail.getUnite());

            PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(detail.getQuantite())));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            PdfPCell cell1 = new PdfPCell(new Phrase(detail.getPuString()));
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase(detail.getMontantString()));
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell2);
        }

        document.add(table);
        document.add(new Paragraph("\n\n"));
        PdfPTable table2 = new PdfPTable(2);
        table2.addCell("TRAVAUX");

        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(devis.getSommeTravauxString())));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table2.addCell(cell);

        table2.addCell("TAUX");
        PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(detail_devis.getFirst().getTaux().toString())));
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table2.addCell(cell2);

        table2.addCell("TOTAL");
        PdfPCell cell3 = new PdfPCell(new Phrase(devis.getMontantString()));
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table2.addCell(cell3);
        document.add(table2);

        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("Liste Paiement :\n\n"));
        LinkedList<Paiement> paiements = new Paiement().select(" WHERE iddevis='" + iddevis + "'");
        Liste_devis Detaildevis = (Liste_devis) new Liste_devis().select(" WHERE id='" + iddevis + "'").getFirst();
        PdfPTable pdfPTable = new PdfPTable(2);
        addTableHeader2(pdfPTable);
        for (Paiement paiement : paiements) {
            // pdfPTable.addCell(Formatter.formatDate(paiement.getDate()));
            pdfPTable.addCell(paiement.getDate().toString());
            PdfPCell cellule = new PdfPCell(new Paragraph(paiement.getMontantString()));
            cellule.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable.addCell(cellule);
        }
        pdfPTable.addCell("TOTAL");
        PdfPCell cellule = new PdfPCell(new Phrase(Detaildevis.getPayeString()));
        cellule.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(cellule);
        // pdfPTable.addCell(Detaildevis.getPayeString());
        // document.add(new Paragraph("\n\nTRAVAUX :" + devis.getSommeTravauxString()));
        // document.add(new Paragraph("TAUX :" +
        // detail_devis.getFirst().getTaux().toString()));
        // document.add(new Paragraph("TOTAL :" + devis.getMontantString()));
        document.add(pdfPTable);
        document.close();

        return "redirect:/detail-travaux";
    }

    private void addTableHeader2(PdfPTable table) {
        Stream.of("Date", "Montant")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    // header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    // header.setBorderWidth(2);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setFixedHeight(30);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("#", "Travaux", "Unite", "Quantite", "Prix Unitaire", "Montant")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    // header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    // header.setBorderWidth(2);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setFixedHeight(30);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
