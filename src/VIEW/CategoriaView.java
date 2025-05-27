package VIEW;

import DAO.CategoriaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Categoria extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome, txtTamanho, txtEmbalagem;
    private CategoriaDAO categoriaDAO;

    public Categoria(String nome, String tamanho, String embalagem) {
        categoriaDAO = new CategoriaDAO();
        initComponents();
        setMinimumSize(new Dimension(500, 300));
        carregarDados();
    }

    private Categoria(int id, String nome, String tamanho, String embalagem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initComponents() {
        setTitle("Gerenciamento de Categorias");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com BorderLayout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        txtNome = new JTextField();
        txtTamanho = new JTextField();
        txtEmbalagem = new JTextField();

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtNome);
        formPanel.add(new JLabel("Tamanho:"));
        formPanel.add(txtTamanho);
        formPanel.add(new JLabel("Embalagem:"));
        formPanel.add(txtEmbalagem);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);

        // Tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Tamanho", "Embalagem"}, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 150)); // Altura fixa, largura flexível

        // Painel superior (formulário + botões)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        // Adicionando componentes ao painel principal
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Listeners
        btnAdicionar.addActionListener(this::adicionarCategoria);
        btnEditar.addActionListener(this::editarCategoria);
        btnRemover.addActionListener(this::removerCategoria);
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                preencherCampos();
            }
        });

        add(panel);
    }

    private void carregarDados() {
        modeloTabela.setRowCount(0);
        List<Model.Categoria> categorias = categoriaDAO.listarTodos();
        for (Model.Categoria cat : categorias) {
            modeloTabela.addRow(new Object[]{cat.getId(), cat.getNome(), cat.getTamanho(), cat.getEmbalagem()});
        }
    }

    private void adicionarCategoria(ActionEvent e) {
        String nome = txtNome.getText();
        String tamanho = txtTamanho.getText();
        String embalagem = txtEmbalagem.getText();

        if (nome.isEmpty() || tamanho.isEmpty() || embalagem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (categoriaDAO.inserir(new Model.Categoria(nome, tamanho, embalagem))) {
            JOptionPane.showMessageDialog(this, "Categoria adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarDados();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar categoria!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCategoria(ActionEvent e) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tabela.getValueAt(selectedRow, 0);
        String nome = txtNome.getText();
        String tamanho = txtTamanho.getText();
        String embalagem = txtEmbalagem.getText();

        if (nome.isEmpty() || tamanho.isEmpty() || embalagem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Model.Categoria categoria = new Model.Categoria(id, nome, tamanho, embalagem);
        if (categoriaDAO.atualizar(categoria)) {
            JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarDados();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar categoria!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerCategoria(ActionEvent e) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tabela.getValueAt(selectedRow, 0);
        if (categoriaDAO.deletar(id)) {
            JOptionPane.showMessageDialog(this, "Categoria removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarDados();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao remover categoria!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        int selectedRow = tabela.getSelectedRow();
        txtNome.setText(tabela.getValueAt(selectedRow, 1).toString());
        txtTamanho.setText(tabela.getValueAt(selectedRow, 2).toString());
        txtEmbalagem.setText(tabela.getValueAt(selectedRow, 3).toString());
    }

    private void limparCampos() {
        txtNome.setText("");
        txtTamanho.setText("");
        txtEmbalagem.setText("");
        tabela.clearSelection();
    }
}