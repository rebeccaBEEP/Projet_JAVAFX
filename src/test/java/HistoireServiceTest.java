import com.example.storyforge_project.model.Histoire;
import com.example.storyforge_project.model.Personnage;
import com.example.storyforge_project.service.HistoireService;
import com.example.storyforge_project.DAO.IHistoireDAO;
import com.example.storyforge_project.DAO.IPersonnageDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoireServiceTest {

    private HistoireService service;

    // Faux DAO en mémoire — pas de vraie base de données
    @BeforeEach
    public void setUp() {
        service = new HistoireService(new FakeHistoireDAO(), new FakePersonnageDAO());
    }

    // --- Tests unitaires ---

    @Test
    public void creerHistoire_titrePasVide_deveLever() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.creerHistoire("", "Auteur", "Résumé");
        });
    }

    @Test
    public void creerHistoire_auteurPasVide_doitLever() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.creerHistoire("Titre", "", "Résumé");
        });
    }

    @Test
    public void creerHistoire_valide_doitReussir() {
        Histoire h = service.creerHistoire("Titre", "Auteur", "Résumé");
        assertNotNull(h);
        assertEquals("Titre", h.getTitre());
    }

    @Test
    public void ajouterPersonnage_nomVide_doitLever() {
        Histoire h = service.creerHistoire("Titre", "Auteur", "");
        assertThrows(IllegalArgumentException.class, () -> {
            service.ajouterPersonnage(h, "", "Rôle", "Description");
        });
    }

    @Test
    public void ajouterPersonnage_nomDuplique_doitLever() {
        Histoire h = service.creerHistoire("Titre", "Auteur", "");
        service.ajouterPersonnage(h, "Emma", "Etudiante", "");
        assertThrows(IllegalArgumentException.class, () -> {
            service.ajouterPersonnage(h, "Emma", "Journaliste", "");
        });
    }

    @Test
    public void ajouterPersonnage_valide_doitReussir() {
        Histoire h = service.creerHistoire("Titre", "Auteur", "");
        Personnage p = service.ajouterPersonnage(h, "Emma", "Etudiante", "");
        assertNotNull(p);
        assertEquals("Emma", p.getNom());
        assertEquals(1, h.getPersonnages().size());
    }

    // --- Faux DAO (simulent la base en mémoire) ---

    static class FakeHistoireDAO implements IHistoireDAO {
        private List<Histoire> histoires = new ArrayList<>();
        private int nextId = 1;

        @Override
        public void save(Histoire h) throws SQLException {
            h.setId(nextId++);
            histoires.add(h);
        }

        @Override
        public List<Histoire> findAll() throws SQLException {
            return histoires;
        }

        @Override
        public void update(Histoire h) throws SQLException {
            // rien à faire pour les tests unitaires
        }

        @Override
        public void delete(int id) throws SQLException {
            histoires.removeIf(h -> h.getId() == id);
        }
    }

    static class FakePersonnageDAO implements IPersonnageDAO {
        @Override
        public void save(Personnage p, int histoireId) throws SQLException {
            p.setId(1);
        }

        @Override
        public List<Personnage> findByHistoireId(int histoireId) throws SQLException {
            return new ArrayList<>();
        }

        @Override
        public void update(Personnage p) throws SQLException {}

        @Override
        public void delete(int id) throws SQLException {}
    }
}