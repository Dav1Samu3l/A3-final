package VIEW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Frame;

public class MenuPrincipal extends JFrame {

    public static void main(String[] args) {

        // NÃO REMOVER ! Garante modelo de threading do Swing.
        // Evita bugs dificeis de diagnosticar em produção.
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }

    public MenuPrincipal() {
        setTitle("Sistema de Controle de Estoque");
        setSize(400, 300);
        // metodo para feixar tod a aplicação
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// abilita botão close
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Painel principal com BorderLayout para centralização
        JPanel mainContainer = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(); // Layout será alterado dinamicamente
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnCategorias = new JButton("Gerenciar Categorias");
        JButton btnProdutos = new JButton("Gerenciar Produtos");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair");

        // efine um tamanho fixo para os botões
        Dimension buttonSize = new Dimension(180, 45);
        btnCategorias.setPreferredSize(buttonSize);
        btnProdutos.setPreferredSize(buttonSize);
        btnRelatorios.setPreferredSize(buttonSize);
        btnSair.setPreferredSize(buttonSize);

        // Listeners dos botões
        btnCategorias.addActionListener(e -> new CategoriaView("", "", "").setVisible(true));
        btnProdutos.addActionListener(e -> new ProdutoView().setVisible(true));
        btnSair.addActionListener(e -> System.exit(0));
        btnRelatorios.addActionListener(e -> new RelatorioView().setVisible(true));

        // Adiciona os botões ao painel
        panel.add(btnCategorias);
        panel.add(btnProdutos);
        panel.add(btnRelatorios);
        panel.add(btnSair);

        mainContainer.add(panel, BorderLayout.CENTER);
        add(mainContainer);

        // Listener para redimensionamento da janela
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if ((getExtendedState() & Frame.MAXIMIZED_BOTH) != 0) {
                    // Maximizado: 2x2 com GridBagLayout para controle preciso
                    panel.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(15, 15, 15, 15); // Espaçamento interno
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    panel.add(btnCategorias, gbc);
                    gbc.gridx = 1;
                    gbc.gridy = 0;
                    panel.add(btnProdutos, gbc);
                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    panel.add(btnRelatorios, gbc);
                    gbc.gridx = 1;
                    gbc.gridy = 1;
                    panel.add(btnSair, gbc);
                    panel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200)); // Margens largas
                } else {
                    // Normal: 4 linhas com GridLayout
                    panel.setLayout(new GridLayout(4, 1, 10, 10));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                }
                panel.revalidate();
            }
        });
    }
}
