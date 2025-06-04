package VIEW;

import DAO.ProdutoDAO;
import Model.Produto;
import Model.Relatorio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RelatorioView extends JFrame {

    private JTabbedPane abas;
    private ProdutoDAO produtoDAO;

    public RelatorioView(String string, int aDouble, double aDouble1, double aDouble2) {
        produtoDAO = new ProdutoDAO();
        initComponents();
        setLocationRelativeTo(null);
    }

    public RelatorioView() {
        produtoDAO = new ProdutoDAO();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Relatórios");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        abas = new JTabbedPane();

        // Aba Lista de Preços
        abas.addTab("Lista de Preços", criarPainelListaPrecos());

        // Aba Balanço
        abas.addTab("Balanço", criarPainelBalanco());

        // Aba Produtos por Categoria
        abas.addTab("Por Categoria", criarPainelCategoria());

        // Aba Produtos abaixo do mínimo
        abas.addTab("Abaixo do Mínimo", criarPainelMinimo());

        // Aba Produtos acima do máximo
        abas.addTab("Acima do Máximo", criarPainelMaximo());

        add(abas);
    }

    private JScrollPane criarPainelListaPrecos() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Produto", "Preço Unitário", "Unidade", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // adicionar dados ao modelo
        for (Relatorio r : produtoDAO.gerarListaPrecos()) {
            modelo.addRow(new Object[]{
                r.getNome(),
                String.format("R$ %.2f", r.getPrecoUnitario()),
                r.getUnidade(),
                r.getCategoria()
            });
        }

        JTable tabela = new JTable(modelo);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        return new JScrollPane(tabela);
    } // 

    private JScrollPane criarPainelBalanco() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Produto", "Quantidade", "Preço Unitário", "Valor Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Relatorio> relatorios = produtoDAO.gerarBalanco();

        int quantidadeTotal = 0;
        double valorTotalGeral = 0.0;

        for (Relatorio r : relatorios) {
            modelo.addRow(new Object[]{
                r.getNome(),
                r.getQuantidade(),
                String.format("R$ %.2f", r.getPrecoUnitario()),
                String.format("R$ %.2f", r.getValorTotal())
            });

            quantidadeTotal += r.getQuantidade();
            valorTotalGeral += r.getValorTotal();
        }

        // Linha de total geral
        modelo.addRow(new Object[]{
            "TOTAL GERAL",
            quantidadeTotal,
            "", // ?????? Coluna de Preço Unitário não é necessária na linha total ?????????
            String.format("R$ %.2f", valorTotalGeral)
        });

        JTable tabela = new JTable(modelo);
        return new JScrollPane(tabela);
    }

    private JScrollPane criarPainelCategoria() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Categoria", "Quantidade de Produtos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Relatorio rs : produtoDAO.gerarProdutosPorCategoria()) {
            modelo.addRow(new Object[]{
                rs.getNomeCategoria(),
                rs.getQuantidadeProdutos()
            });
        }

        return new JScrollPane(new JTable(modelo));
    }

    private JScrollPane criarPainelMinimo() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Produto", "Quantidade", "Mínimo", "Máximo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Produto r : produtoDAO.listarAbaixoDoMinimo()) {
            modelo.addRow(new Object[]{
                r.getNome(),
                r.getQuantidade(),
                r.getQuantidadeMinima(),
                r.getQuantidadeMaxima()
            });
        }

        return new JScrollPane(new JTable(modelo));
    }

    private JScrollPane criarPainelMaximo() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Produto", "Quantidade", "Mínimo", "Máximo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Produto r : produtoDAO.listarAcimaDoMaximo()) {
            modelo.addRow(new Object[]{
                r.getNome(),
                r.getQuantidade(),
                r.getQuantidadeMinima(),
                r.getQuantidadeMaxima()
            });
        }

        return new JScrollPane(new JTable(modelo));
    }

}
