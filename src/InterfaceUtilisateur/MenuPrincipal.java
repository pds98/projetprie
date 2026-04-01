package InterfaceUtilisateur;

import javax.swing.*;

public class MenuPrincipal
{

    public class MenuPrincipal extends JFrame {

        private JPanel monPanel;

        private JLabel titleLabel;

        private JButton btnEtudiants;
        private JButton btnSalles;
        private JButton btnReservations;
        private JButton btnPrieres;
        private JButton btnEvenements;
        private JButton btnForums;
        private JButton btnGroupes;
        private JButton btnQuitter;

        private void initialiserForm() {
            monPanel = new JPanel();
            monPanel.setLayout(new GridLayout(9, 1, 8, 8));
            monPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            titleLabel = new JLabel("Système de Gestion de Prière et de Réservation", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

            btnEtudiants    = new JButton("👤  Gérer les Étudiants");
            btnSalles       = new JButton("🏫  Gérer les Salles");
            btnReservations = new JButton("📅  Gérer les Réservations");
            btnPrieres      = new JButton("🤲  Gérer les Prières");
            btnEvenements   = new JButton("📢  Gérer les Événements");
            btnForums       = new JButton("💬  Gérer les Forums");
            btnGroupes      = new JButton("👥  Gérer les Groupes");
            btnQuitter      = new JButton("❌  Quitter");

            monPanel.add(titleLabel);
            monPanel.add(btnEtudiants);
            monPanel.add(btnSalles);
            monPanel.add(btnReservations);
            monPanel.add(btnPrieres);
            monPanel.add(btnEvenements);
            monPanel.add(btnForums);
            monPanel.add(btnGroupes);
            monPanel.add(btnQuitter);

            // Actions
            btnEtudiants.addActionListener(e    -> new MenuEtudiant());
            btnSalles.addActionListener(e       -> new MenuSalle());
            btnReservations.addActionListener(e -> new MenuReservation());
            btnPrieres.addActionListener(e      -> new MenuPriere());
            btnEvenements.addActionListener(e   -> new MenuEvenement());
            btnForums.addActionListener(e       -> new MenuForum());
            btnGroupes.addActionListener(e      -> new MenuGroupe());
            btnQuitter.addActionListener(e      -> System.exit(0));

            setTitle("Menu Principal");
            setSize(500, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setContentPane(monPanel);
            setVisible(true);
        }

        public MenuPrincipal() {
            initialiserForm();
        }
    }

}
