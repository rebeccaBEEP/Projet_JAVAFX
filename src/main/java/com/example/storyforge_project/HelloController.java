package com.example.storyforge_project;

import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;
import com.example.storyforge_project.service.HistoireService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.example.storyforge_project.DAO.HistoireDAO;
import com.example.storyforge_project.DAO.PersonnageDAO;

public class HelloController {

    @FXML
    private ListView<Histoire> lvHistoires;
    @FXML
    private TextField txtTitre, txtAuteur, txtResume;
    @FXML
    private Label lblErreurHistoire;

    @FXML
    private ListView<Personnage> lvPersonnages;
    @FXML
    private TextField txtNom, txtRole, txtDescription;
    @FXML
    private Label lblErreurPersonnage;

    private HistoireService histoireService;
    private ObservableList<Histoire> histoiresObservables;
    private ObservableList<Personnage> personnagesObservables;

    private Histoire histoireSelectionnee;
    private Personnage personnageSelectionne;

    @FXML
    public void initialize() {
        histoireService = new HistoireService(new HistoireDAO(), new PersonnageDAO());
        histoiresObservables = FXCollections.observableArrayList();
        personnagesObservables = FXCollections.observableArrayList();

        lvHistoires.setItems(histoiresObservables);
        lvPersonnages.setItems(personnagesObservables);

        try {
            histoiresObservables.addAll(histoireService.getHistoires());
        } catch (RuntimeException e) {
            lblErreurHistoire.setText(e.getMessage());
        }

        lvHistoires.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            histoireSelectionnee = newVal;
            personnagesObservables.clear();
            viderFormulairePersonnage();
            lblErreurHistoire.setText("");

            if (newVal != null) {
                txtTitre.setText(newVal.getTitre());
                txtAuteur.setText(newVal.getAuteur());
                txtResume.setText(newVal.getResume());
                personnagesObservables.addAll(newVal.getPersonnages());
            } else {
                viderFormulaireHistoire();
            }
        });

        lvPersonnages.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            personnageSelectionne = newVal;
            lblErreurPersonnage.setText("");
            if (newVal != null) {
                txtNom.setText(newVal.getNom());
                txtRole.setText(newVal.getRole());
                txtDescription.setText(newVal.getDescription());
            } else {
                viderFormulairePersonnage();
            }
        });
    }

    @FXML
    protected void onEnregistrerHistoire() {
        lblErreurHistoire.setText("");
        try {
            if (histoireSelectionnee == null) {
                Histoire histoire = histoireService.creerHistoire(
                        txtTitre.getText(), txtAuteur.getText(), txtResume.getText());
                histoiresObservables.add(histoire);
                lvHistoires.getSelectionModel().select(histoire);
            } else {
                histoireService.modifierHistoire(histoireSelectionnee,
                        txtTitre.getText(), txtAuteur.getText(), txtResume.getText());
                lvHistoires.refresh();
            }
        } catch (RuntimeException e) {
            lblErreurHistoire.setText(e.getMessage());
        }
    }

    @FXML
    protected void onNouvelleHistoire() {
        lvHistoires.getSelectionModel().clearSelection();
        viderFormulaireHistoire();
        lblErreurHistoire.setText("");
    }

    @FXML
    protected void onSupprimerHistoire() {
        if (histoireSelectionnee != null) {
            try {
                histoireService.supprimerHistoire(histoireSelectionnee);
                histoiresObservables.remove(histoireSelectionnee);
                lvHistoires.getSelectionModel().clearSelection();
            } catch (RuntimeException e) {
                lblErreurHistoire.setText(e.getMessage());
            }
        }
    }

    private void viderFormulaireHistoire() {
        txtTitre.clear();
        txtAuteur.clear();
        txtResume.clear();
    }

    @FXML
    protected void onEnregistrerPersonnage() {
        lblErreurPersonnage.setText("");

        if (histoireSelectionnee == null) {
            lblErreurPersonnage.setText("Sélectionnez d'abord une histoire.");
            return;
        }

        try {
            if (personnageSelectionne == null) {
                Personnage personnage = histoireService.ajouterPersonnage(histoireSelectionnee,
                        txtNom.getText(), txtRole.getText(), txtDescription.getText());
                personnagesObservables.add(personnage);
                lvPersonnages.getSelectionModel().select(personnage);
            } else {
                histoireService.modifierPersonnage(histoireSelectionnee, personnageSelectionne,
                        txtNom.getText(), txtRole.getText(), txtDescription.getText());
                lvPersonnages.refresh();
            }
        } catch (RuntimeException e) {
            lblErreurPersonnage.setText(e.getMessage());
        }
    }

    @FXML
    protected void onNouveauPersonnage() {
        lvPersonnages.getSelectionModel().clearSelection();
        viderFormulairePersonnage();
        lblErreurPersonnage.setText("");
    }

    @FXML
    protected void onSupprimerPersonnage() {
        if (histoireSelectionnee != null && personnageSelectionne != null) {
            try {
                histoireService.supprimerPersonnage(histoireSelectionnee, personnageSelectionne);
                personnagesObservables.remove(personnageSelectionne);
                lvPersonnages.getSelectionModel().clearSelection();
            } catch (RuntimeException e) {
                lblErreurPersonnage.setText(e.getMessage());
            }
        }
    }

    private void viderFormulairePersonnage() {
        txtNom.clear();
        txtRole.clear();
        txtDescription.clear();
    }
}