package com.example.storyforge_project;

import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;
import com.example.storyforge_project.model.Scene;
import com.example.storyforge_project.model.StatistiquesScenes;
import com.example.storyforge_project.model.StatutScene;
import com.example.storyforge_project.service.SceneService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.List;

public class SceneController {

    @FXML
    private Label lblHistoire;
    @FXML
    private Label lblStatistiques;
    @FXML
    private ComboBox<StatutScene> cbFiltreStatut;
    @FXML
    private ComboBox<Personnage> cbFiltrePersonnage;
    @FXML
    private TextField txtRecherche;

    @FXML
    private ListView<Scene> lvScenes;

    @FXML
    private TextField txtTitreScene, txtLieu, txtMoment, txtPosition;
    @FXML
    private ComboBox<StatutScene> cbStatut;
    @FXML
    private TextArea txtContenu;
    @FXML
    private ListView<Personnage> lvPersonnagesDisponibles;

    @FXML
    private Label lblErreurScene;

    private SceneService sceneService;
    private Histoire histoire;

    private ObservableList<Scene> scenesObservables;
    private Scene sceneSelectionnee;

    @FXML
    public void initialize() {
        cbStatut.setItems(FXCollections.observableArrayList(StatutScene.values()));
        lvPersonnagesDisponibles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        lvScenes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            sceneSelectionnee = newVal;
            lblErreurScene.setText("");
            if (newVal != null) {
                afficherScene(newVal);
            } else {
                viderFormulaire();
            }
        });
    }

    public void setHistoire(Histoire histoire, SceneService sceneService) {
        this.histoire = histoire;
        this.sceneService = sceneService;

        lblHistoire.setText("Scènes de : " + histoire.getTitre());

        scenesObservables = FXCollections.observableArrayList();
        lvScenes.setItems(scenesObservables);

        lvPersonnagesDisponibles.setItems(FXCollections.observableArrayList(histoire.getPersonnages()));

        initFiltres();
        appliquerFiltres();
        afficherStatistiques();
    }

    private void initFiltres() {
        ObservableList<StatutScene> statuts = FXCollections.observableArrayList();
        statuts.add(null);
        statuts.addAll(StatutScene.values());
        cbFiltreStatut.setItems(statuts);
        cbFiltreStatut.setConverter(new StringConverter<StatutScene>() {
            public String toString(StatutScene s) { return s == null ? "Tous les statuts" : s.getLibelle(); }
            public StatutScene fromString(String s) { return null; }
        });
        cbFiltreStatut.setValue(null);

        ObservableList<Personnage> persos = FXCollections.observableArrayList();
        persos.add(null);
        persos.addAll(histoire.getPersonnages());
        cbFiltrePersonnage.setItems(persos);
        cbFiltrePersonnage.setConverter(new StringConverter<Personnage>() {
            public String toString(Personnage p) { return p == null ? "Tous les personnages" : p.getNom(); }
            public Personnage fromString(String s) { return null; }
        });
        cbFiltrePersonnage.setValue(null);

        cbFiltreStatut.valueProperty().addListener((obs, o, n) -> appliquerFiltres());
        cbFiltrePersonnage.valueProperty().addListener((obs, o, n) -> appliquerFiltres());
        txtRecherche.textProperty().addListener((obs, o, n) -> appliquerFiltres());
    }

    private void appliquerFiltres() {
        lblErreurScene.setText("");
        try {
            List<Scene> resultat = sceneService.filtrerScenes(histoire,
                    cbFiltreStatut.getValue(), cbFiltrePersonnage.getValue(), txtRecherche.getText());
            scenesObservables.setAll(resultat);
        } catch (RuntimeException e) {
            lblErreurScene.setText(e.getMessage());
        }
    }

    private void afficherStatistiques() {
        StatistiquesScenes stats = sceneService.getStatistiques(histoire);
        StringBuilder sb = new StringBuilder();
        sb.append("Personnages : ").append(stats.getNombrePersonnages());
        sb.append("   |   Scènes : ").append(stats.getNombreScenes());
        for (StatutScene s : StatutScene.values()) {
            sb.append("   |   ").append(s.getLibelle()).append(" : ").append(stats.getNombreScenes(s));
        }
        lblStatistiques.setText(sb.toString());
    }

    @FXML
    protected void onReinitialiserFiltres() {
        cbFiltreStatut.setValue(null);
        cbFiltrePersonnage.setValue(null);
        txtRecherche.clear();
        appliquerFiltres();
    }

    private void afficherScene(Scene scene) {
        txtTitreScene.setText(scene.getTitre());
        txtLieu.setText(scene.getLieu());
        txtMoment.setText(scene.getMoment());
        txtPosition.setText(String.valueOf(scene.getPosition()));
        txtContenu.setText(scene.getContenu());
        cbStatut.setValue(scene.getStatut());

        lvPersonnagesDisponibles.getSelectionModel().clearSelection();
        for (Personnage personnage : scene.getPersonnages()) {
            int index = lvPersonnagesDisponibles.getItems().indexOf(personnage);
            if (index >= 0) {
                lvPersonnagesDisponibles.getSelectionModel().select(index);
            }
        }
    }

    @FXML
    protected void onEnregistrerScene() {
        lblErreurScene.setText("");

        int position;
        try {
            position = Integer.parseInt(txtPosition.getText().trim());
        } catch (NumberFormatException e) {
            lblErreurScene.setText("La position doit être un nombre entier.");
            return;
        }

        List<Personnage> personnagesSelectionnes = lvPersonnagesDisponibles.getSelectionModel().getSelectedItems();

        try {
            if (sceneSelectionnee == null) {
                sceneService.creerScene(histoire,
                        txtTitreScene.getText(), txtLieu.getText(), txtMoment.getText(),
                        txtContenu.getText(), position, cbStatut.getValue(), personnagesSelectionnes);
            } else {
                sceneService.modifierScene(histoire, sceneSelectionnee,
                        txtTitreScene.getText(), txtLieu.getText(), txtMoment.getText(),
                        txtContenu.getText(), position, cbStatut.getValue(), personnagesSelectionnes);
            }
            appliquerFiltres();
            afficherStatistiques();
        } catch (RuntimeException e) {
            lblErreurScene.setText(e.getMessage());
        }
    }

    @FXML
    protected void onNouvelleScene() {
        lvScenes.getSelectionModel().clearSelection();
        viderFormulaire();
        lblErreurScene.setText("");
    }

    @FXML
    protected void onSupprimerScene() {
        if (sceneSelectionnee != null) {
            try {
                sceneService.supprimerScene(sceneSelectionnee);
                appliquerFiltres();
                afficherStatistiques();
                lvScenes.getSelectionModel().clearSelection();
            } catch (RuntimeException e) {
                lblErreurScene.setText(e.getMessage());
            }
        }
    }

    private void viderFormulaire() {
        txtTitreScene.clear();
        txtLieu.clear();
        txtMoment.clear();
        txtPosition.clear();
        txtContenu.clear();
        cbStatut.setValue(null);
        lvPersonnagesDisponibles.getSelectionModel().clearSelection();
    }
}