package VIEW;

import Model.Produto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        setTitle("Sistema de Controle de Estoque");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton btnCategorias = new JButton("Gerenciar Categorias");
        JButton btnProdutos = new JButton("Gerenciar Produtos");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair");
        
        btnCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CategoriaView("", "", "").setVisible(true); // Passa strings vazias como parâmetros
                // new Categoria().setVisible(true);
            }
        });
        
        btnProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Produto().setVisible(true);
            }
        });
        
        btnRelatorios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Relatorio().setVisible(true);
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        panel.add(btnCategorias);
        panel.add(btnProdutos);
        panel.add(btnRelatorios);
        panel.add(btnSair);
        
        add(panel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }
}