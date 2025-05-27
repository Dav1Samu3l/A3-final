package VIEW;

import Model.Categoria;
import Model.Produto;
import DAO.CategoriaDAO;
import DAO.ProdutoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProdutoView extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome, txtPreco, txtUnidade, txtQuantidade, txtMin, txtMax;
    private JComboBox<Categoria> comboCategoria;
    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;

    public ProdutoView() {
        produtoDAO = new ProdutoDAO();
        categoriaDAO = new CategoriaDAO();
        initComponents();
        carregarDados();
        carregarCategorias();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gerenciamento de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== PAINEL SUPERIOR (Formulário + Botões) ==========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Formulário (GridBagLayout para maior controle)
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes do formulário
        txtNome = new JTextField(20);
        txtPreco = new JTextField(10);
        txtUnidade = new JTextField(10);
        txtQuantidade = new JTextField(10);
        txtMin = new JTextField(10);
        txtMax = new JTextField(10);
        comboCategoria = new JComboBox<>();

        // Adicionando componentes ao formulário
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Preço Unitário:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPreco, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Unidade:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtUnidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtQuantidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Quantidade Mínima:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Quantidade Máxima:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMax, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboCategoria, gbc);

        // Botões (FlowLayout com alinhamento à direita)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);

        // Adicionando formulário e botões ao topPanel
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ========== TABELA (Com ScrollPane e tamanho preferencial) ==========
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Unidade", "Quantidade", "Categoria"}, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 250));

        // Adicionando componentes ao painel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Listeners
        btnAdicionar.addActionListener(this::adicionarProduto);
        btnEditar.addActionListener(this::editarProduto);
        btnRemover.addActionListener(this::removerProduto);
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                preencherCampos();
            }
        });

        add(mainPanel);
    }

    // ========== MÉTODOS ==========
    private void carregarDados() {
        modeloTabela.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getPrecoUnitario(),
                p.getUnidade(),
                p.getQuantidade(),
                p.getCategoria() != null ? p.getCategoria().getNome() : ""
            });
        }
    }

    private void carregarCategorias() {
        comboCategoria.removeAllItems();
        List<Model.Categoria> categorias = categoriaDAO.listarTodos();
        for (Model.Categoria cat : categorias) {
            comboCategoria.addItem(cat);
        }
    }

    private void adicionarProduto(ActionEvent e) {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            String unidade = txtUnidade.getText();
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            int min = Integer.parseInt(txtMin.getText());
            int max = Integer.parseInt(txtMax.getText());
            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

            if (nome.isEmpty() || unidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto produto = new Produto(nome, preco, unidade, quantidade, min, max, categoria);
            if (produtoDAO.inserir(produto)) {
                JOptionPane.showMessageDialog(this, "Produto adicionado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProduto(ActionEvent e) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tabela.getValueAt(selectedRow, 0);
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            String unidade = txtUnidade.getText();
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            int min = Integer.parseInt(txtMin.getText());
            int max = Integer.parseInt(txtMax.getText());
            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

            if (nome.isEmpty() || unidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto produto = new Produto(nome, preco, unidade, quantidade, min, max, categoria);
            produto.setId(id);
            if (produtoDAO.atualizar(produto)) {
                JOptionPane.showMessageDialog(this, "Produto atualizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerProduto(ActionEvent e) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tabela.getValueAt(selectedRow, 0);
        if (produtoDAO.deletar(id)) {
            JOptionPane.showMessageDialog(this, "Produto removido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarDados();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        int selectedRow = tabela.getSelectedRow();
        txtNome.setText(tabela.getValueAt(selectedRow, 1).toString());
        txtPreco.setText(tabela.getValueAt(selectedRow, 2).toString());
        txtUnidade.setText(tabela.getValueAt(selectedRow, 3).toString());
        txtQuantidade.setText(tabela.getValueAt(selectedRow, 4).toString());
        txtMin.setText(tabela.getValueAt(selectedRow, 5).toString());
        
        Categoria categoria = (Categoria) comboCategoria.getSelectedItem();
        if (categoria != null) {
            comboCategoria.setSelectedItem(categoria);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtPreco.setText("");
        txtUnidade.setText("");
        txtQuantidade.setText("");
        txtMin.setText("");
        txtMax.setText("");
        comboCategoria.setSelectedIndex(0);
        tabela.clearSelection();
    }
} 