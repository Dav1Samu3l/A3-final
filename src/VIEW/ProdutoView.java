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
    private JTextField txtNome, txtPreco, txtQuantidade, txtMin, txtMax;
    private JComboBox<Categoria> comboCategoria;
    private final ProdutoDAO produtoDAO;
    private final CategoriaDAO categoriaDAO;
    private JRadioButton radioUnidade, radioKg, radioLitro, radioMetro, radioCaixa, radioPacote;
    private ButtonGroup grupoUnidades;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== PAINEL SUPERIOR (Formulário + Botões) ==========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Formulário com GridBagLayout para alinhamento preciso
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes do formulário
        txtNome = new JTextField(20);
        txtPreco = new JTextField(10);
        txtQuantidade = new JTextField(10);
        txtMin = new JTextField(10);
        txtMax = new JTextField(10);
        comboCategoria = new JComboBox<>();

        // Painel para unidades de medida (radio buttons)
        JPanel painelUnidades = new JPanel(new FlowLayout(FlowLayout.LEFT));
        grupoUnidades = new ButtonGroup();

        radioUnidade = new JRadioButton("Unidade", true);
        radioKg = new JRadioButton("Kg");
        radioLitro = new JRadioButton("Litro");
        radioMetro = new JRadioButton("Metro");
        radioCaixa = new JRadioButton("Caixa");
        radioPacote = new JRadioButton("Pacote");

        // Agrupa os botões
        grupoUnidades.add(radioUnidade);
        grupoUnidades.add(radioKg);
        grupoUnidades.add(radioLitro);
        grupoUnidades.add(radioMetro);
        grupoUnidades.add(radioCaixa);
        grupoUnidades.add(radioPacote);

        // Adiciona ao painel
        painelUnidades.add(radioUnidade);
        painelUnidades.add(radioKg);
        painelUnidades.add(radioLitro);
        painelUnidades.add(radioMetro);
        painelUnidades.add(radioCaixa);
        painelUnidades.add(radioPacote);

        // ========== ADICIONANDO COMPONENTES AO FORMULÁRIO ==========
        // Linha 1: Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(txtNome, gbc);

        // Linha 2: Preço
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Preço Unitário:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(txtPreco, gbc);

        // Linha 3: Unidade
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Unidade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(painelUnidades, gbc);

        // Linha 4: Quantidade
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(txtQuantidade, gbc);

        // Linha 5: Quantidade Mínima
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Quantidade Mínima:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(txtMin, gbc);

        // Linha 6: Quantidade Máxima
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Quantidade Máxima:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(txtMax, gbc);

        // Linha 7: Categoria
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.1;
        formPanel.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        formPanel.add(comboCategoria, gbc);

        // ========== BOTÕES ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnEntrada = new JButton("Entrada");
        JButton btnSaida = new JButton("Saída");

        btnEntrada.addActionListener(_ -> atualizarQuantidade(1));
        btnSaida.addActionListener(_ -> atualizarQuantidade(-1));
        JButton btnReajustar = new JButton("Reajustar Preços");

        buttonPanel.add(btnEntrada);
        buttonPanel.add(btnSaida);
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnReajustar);

        // Adiciona formulário e botões ao topPanel
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ========== TABELA ==========
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Preço", "Unidade", "Quantidade", "Quantidade Mínima", "Quantidade Máxima", "Categoria"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };

        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 250));

        // Adicionando componentes ao painel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Listeners
        btnReajustar.addActionListener(this::reajustarPrecos);
        btnAdicionar.addActionListener(this::adicionarProduto);
        btnEditar.addActionListener(this::editarProduto);
        btnRemover.addActionListener(this::removerProduto);
        btnLimpar.addActionListener(_ -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                preencherCampos();
            }
        });

        add(mainPanel);
    }

    // ========== MÉTODOS ==========
    private void atualizarQuantidade(int operacao) { // 1 para entrada, -1 para saída
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Solicita a quantidade ao usuário
        String input = JOptionPane.showInputDialog(
                this,
                "Quantidade:",
                (operacao == 1) ? "Entrada" : "Saída",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int quantidade = Integer.parseInt(input);
            int id = (int) tabela.getValueAt(selectedRow, 0);
            int quantidadeAtual = (int) tabela.getValueAt(selectedRow, 4);
            int quantidadeMinima = (int) tabela.getValueAt(selectedRow, 5);
            int quantidadeMaxima = (int) tabela.getValueAt(selectedRow, 6);

            // VALIDAÇÃO SIMPLES PARA SAÍDAS - IMPEDIR QUANTIDADE NEGATIVA
            if (operacao == -1 && quantidade > quantidadeAtual) {
                JOptionPane.showMessageDialog(this,
                        "Estoque insuficiente!\nQuantidade disponível: " + quantidadeAtual,
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Atualiza o estoque
            if (produtoDAO.atualizarQuantidade(id, operacao * quantidade)) {
                carregarDados();

                // Verifica alertas
                String mensagem = "";
                int novaQuantidade = quantidadeAtual + (operacao * quantidade);
                if (operacao == 1 && novaQuantidade > quantidadeMaxima) {
                    mensagem = "Atenção: Quantidade acima do máximo!";
                } else if (operacao == -1 && novaQuantidade < quantidadeMinima) {
                    mensagem = "Atenção: Quantidade abaixo do mínimo!";
                }

                if (!mensagem.isEmpty()) {
                    JOptionPane.showMessageDialog(this, mensagem, "Alerta", JOptionPane.WARNING_MESSAGE);
                }

                JOptionPane.showMessageDialog(this, "Operação concluída!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro na operação!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Insira um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
                p.getQuantidadeMinima(),
                p.getQuantidadeMaxima(),
                p.getCategoria()
            });
        }
    }

    private void carregarCategorias() {
        comboCategoria.removeAllItems();
        List<Categoria> categorias = categoriaDAO.listarTodos();
        for (Categoria cat : categorias) {
            comboCategoria.addItem(cat);
        }
    }

    private String getUnidadeSelecionada() {
        if (radioUnidade.isSelected()) {
            return "Unidade";
        }
        if (radioKg.isSelected()) {
            return "Kg";
        }
        if (radioLitro.isSelected()) {
            return "Litro";
        }
        if (radioMetro.isSelected()) {
            return "Metro";
        }
        if (radioCaixa.isSelected()) {
            return "Caixa";
        }
        if (radioPacote.isSelected()) {
            return "Pacote";
        }
        return "Unidade"; // Padrão
    }

    private void adicionarProduto(ActionEvent e) {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            String unidade = getUnidadeSelecionada(); // Obtém do radio button
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            int min = Integer.parseInt(txtMin.getText());
            int max = Integer.parseInt(txtMax.getText());
            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (comboCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Cadastre categorias primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
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
            String unidade = getUnidadeSelecionada(); // Obtém do radio button
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            int min = Integer.parseInt(txtMin.getText());
            int max = Integer.parseInt(txtMax.getText());
            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

            if (nome.isEmpty()) {
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
        txtNome.setText(tabela.getValueAt(selectedRow, 1).toString()); // Nome (coluna 1)
        txtPreco.setText(tabela.getValueAt(selectedRow, 2).toString()); // Preço (coluna 2)
        txtQuantidade.setText(tabela.getValueAt(selectedRow, 4).toString()); // Quantidade (coluna 4)
        txtMin.setText(tabela.getValueAt(selectedRow, 5).toString()); // Quantidade Mínima (coluna 5)
        txtMax.setText(tabela.getValueAt(selectedRow, 6).toString()); // Quantidade Máxima (coluna 6)

        // Atualiza a unidade via radio buttons
        String unidade = tabela.getValueAt(selectedRow, 3).toString(); // Unidade (coluna 3)
        if (unidade.equals("Unidade")) {
            radioUnidade.setSelected(true);
        } else if (unidade.equals("Kg")) {
            radioKg.setSelected(true);
        } else if (unidade.equals("Litro")) {
            radioLitro.setSelected(true);
        } else if (unidade.equals("Metro")) {
            radioMetro.setSelected(true);
        } else if (unidade.equals("Caixa")) {
            radioCaixa.setSelected(true);
        } else if (unidade.equals("Pacote")) {
            radioPacote.setSelected(true);
        } else {
            radioUnidade.setSelected(true); // Padrão
        }
        // Atualiza a categoria
        String nomeCategoria = tabela.getValueAt(selectedRow, 7).toString();
        for (int i = 0; i < comboCategoria.getItemCount(); i++) {
            Categoria cat = comboCategoria.getItemAt(i);
            if (cat.toString().equals(nomeCategoria)) {
                comboCategoria.setSelectedItem(cat);
                break;
            }
        }
    }

    private void reajustarPrecos(ActionEvent e) {
        try {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Percentual de reajuste (%):",
                    "Reajuste de Preços",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null || input.trim().isEmpty()) {
                return;
            }

            double percentual = Double.parseDouble(input);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja aplicar " + percentual + "% de reajuste em TODOS os produtos?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (produtoDAO.reajustarPrecos(percentual)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Reajuste aplicado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    carregarDados(); // Atualiza a tabela
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao aplicar reajuste",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Valor inválido! Use números decimais (ex: 10.5)",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");
        txtMin.setText("");
        txtMax.setText("");
        radioUnidade.setSelected(true);
        comboCategoria.setSelectedIndex(0);
        tabela.clearSelection();
    }
}
