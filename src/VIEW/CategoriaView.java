package VIEW;

import DAO.CategoriaDAO;
import Model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CategoriaView extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome;
    private CategoriaDAO categoriaDAO;

    // Radio buttons de tamanho
    private ButtonGroup grupoTamanho;
    private JRadioButton rbPequeno, rbMedio, rbGrande;
    private JRadioButton[] radioTamanhos;

    // Radio buttons de embalagem
    private ButtonGroup grupoEmbalagem;
    private JRadioButton rbLata, rbVidro, rbPlastico;
    private JRadioButton[] radioEmbalagens;

    public CategoriaView() {
        categoriaDAO = new CategoriaDAO();
        initComponents();
        setMinimumSize(new Dimension(500, 300));
        carregarDados();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gerenciamento de Categorias");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== FORMULÁRIO COM GRIDBAGLAYOUT ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        txtNome = new JTextField(20);

        // Tamanho (Pequeno, Médio, Grande)
        grupoTamanho = new ButtonGroup();
        JPanel tamanhoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbPequeno = new JRadioButton("Pequeno");
        rbMedio = new JRadioButton("Médio");
        rbGrande = new JRadioButton("Grande");

        radioTamanhos = new JRadioButton[]{rbPequeno, rbMedio, rbGrande};
        grupoTamanho.add(rbPequeno);
        grupoTamanho.add(rbMedio);
        grupoTamanho.add(rbGrande);
        tamanhoPanel.add(rbPequeno);
        tamanhoPanel.add(rbMedio);
        tamanhoPanel.add(rbGrande);

        // Embalagem (Lata, Vidro, Plástico)
        grupoEmbalagem = new ButtonGroup();
        JPanel embalagemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbLata = new JRadioButton("Lata");
        rbVidro = new JRadioButton("Vidro");
        rbPlastico = new JRadioButton("Plástico");

        radioEmbalagens = new JRadioButton[]{rbLata, rbVidro, rbPlastico};
        grupoEmbalagem.add(rbLata);
        grupoEmbalagem.add(rbVidro);
        grupoEmbalagem.add(rbPlastico);
        embalagemPanel.add(rbLata);
        embalagemPanel.add(rbVidro);
        embalagemPanel.add(rbPlastico);

        // ========== ADICIONANDO COMPONENTES AO FORMULÁRIO ==========
        // Linha 1: Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(txtNome, gbc);

        // Linha 2: Tamanho
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Tamanho:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(tamanhoPanel, gbc);

        // Linha 3: Embalagem
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Embalagem:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(embalagemPanel, gbc);

        // ========== BOTÕES ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);

        // ========== TABELA ==========
        // Modelo de tabela não editável
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Tamanho", "Embalagem"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas as células NÃO editáveis
            }
        };

        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 150));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ========== LISTENERS ==========
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
        List<Categoria> categorias = categoriaDAO.listarTodos();
        for (Categoria cat : categorias) {
            modeloTabela.addRow(new Object[]{cat.getId(), cat.getNome(), cat.getTamanho(), cat.getEmbalagem()});
        }
    }

    private void adicionarCategoria(ActionEvent e) {
        String nome = txtNome.getText();
        String tamanho = getTamanhoSelecionado();
        String embalagem = getEmbalagemSelecionada();

        if (nome.isEmpty() || tamanho == null || embalagem == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (categoriaDAO.inserir(new Categoria(nome, tamanho, embalagem))) {
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
        String tamanho = getTamanhoSelecionado();
        String embalagem = getEmbalagemSelecionada();

        if (nome.isEmpty() || tamanho == null || embalagem == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categoria categoria = new Categoria(id, nome, tamanho, embalagem);
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
        String nomeCategoria = tabela.getValueAt(selectedRow, 1).toString();

        if (categoriaDAO.deletar(id)) {
            JOptionPane.showMessageDialog(this, "Categoria removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarDados();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Não é possível excluir '" + nomeCategoria + "'!\n\n"
                    + "Motivo: Existem produtos associados a esta categoria.\n"
                    + "Remova primeiro os produtos ou altere sua categoria.",
                    "Erro: Categoria com Produtos Associados",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        int selectedRow = tabela.getSelectedRow();
        txtNome.setText(tabela.getValueAt(selectedRow, 1).toString());

        String tamanhoSelecionado = tabela.getValueAt(selectedRow, 2).toString();
        for (JRadioButton radio : radioTamanhos) {
            radio.setSelected(radio.getText().equalsIgnoreCase(tamanhoSelecionado));
        }

        String embalagemSelecionada = tabela.getValueAt(selectedRow, 3).toString();
        for (JRadioButton radio : radioEmbalagens) {
            radio.setSelected(radio.getText().equalsIgnoreCase(embalagemSelecionada));
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        grupoTamanho.clearSelection();
        grupoEmbalagem.clearSelection();
        tabela.clearSelection();
    }

    private String getTamanhoSelecionado() {
        for (JRadioButton radio : radioTamanhos) {
            if (radio.isSelected()) {
                return radio.getText();
            }
        }
        return null;
    }

    private String getEmbalagemSelecionada() {
        for (JRadioButton radio : radioEmbalagens) {
            if (radio.isSelected()) {
                return radio.getText();
            }
        }
        return null;
    }
}
