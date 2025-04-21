package lede;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class Main {
    private static final Lede lede = new Lede();
    // Cores atualizadas com melhor contraste
    private static final Color COR_PRINCIPAL = new Color(40, 107, 163);
    private static final Color COR_SECUNDARIA = new Color(245, 245, 245);
    private static final Color COR_BOTAO_ACAO = new Color(65, 142, 200);
    private static final Color COR_BOTAO_EXIBIR = new Color(30, 90, 150);
    private static final Color COR_TEXTO = new Color(60, 60, 60);
    private static final Color COR_BORDA = new Color(210, 210, 210);

    // Fontes atualizadas
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_BOTOES = new Font("Segoe UI", Font.BOLD, 12);

    private static JTextArea areaSaida;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Configurações para melhorar o contraste global
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextArea.background", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(Main::criarInterface);
    }

    private static void criarInterface() {
        JFrame frame = new JFrame("LEDE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600); // Tamanho um pouco maior
        frame.setMinimumSize(new Dimension(550, 400));
        frame.setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        painelPrincipal.setBackground(COR_SECUNDARIA);

        // Título com melhor destaque
        JLabel titulo = new JLabel("Lista Estática Duplamente Encadeada (LEDE)", JLabel.CENTER);
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_PRINCIPAL);
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Painel de operações reorganizado
        JPanel painelOperacoes = criarPainelOperacoes();

        // Área de saída com melhor contraste
        JPanel painelSaida = criarPainelSaida();

        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(painelOperacoes, BorderLayout.CENTER);
        painelPrincipal.add(painelSaida, BorderLayout.SOUTH);

        frame.setContentPane(painelPrincipal);
        frame.setVisible(true);
    }

    private static JPanel criarPainelOperacoes() {
        JPanel painelOperacoes = new JPanel();
        painelOperacoes.setLayout(new BoxLayout(painelOperacoes, BoxLayout.Y_AXIS));
        painelOperacoes.setBorder(criarBordaTitulada("Operações"));
        painelOperacoes.setOpaque(false);
        painelOperacoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel painelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        painelCampo.setOpaque(false);

        JLabel labelValor = new JLabel("Valor:");
        labelValor.setFont(FONTE_NORMAL);
        labelValor.setForeground(COR_TEXTO);

        JTextField campoValor = new JTextField(12);
        campoValor.setFont(FONTE_NORMAL);
        campoValor.setPreferredSize(new Dimension(120, 20));
        campoValor.setMaximumSize(campoValor.getPreferredSize());

        painelCampo.add(labelValor);
        painelCampo.add(campoValor);

        // Painel para os botões (organizados horizontalmente)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Espaço entre botões: 10px horizontal, 5px vertical
        painelBotoes.setOpaque(false);

        // Criar botões
        JButton btnIncluir = criarBotao("Incluir", 100, 30);
        JButton btnInicioFim = criarBotao("Exibir início → fim", 150, 30);
        JButton btnFimInicio = criarBotao("Exibir fim → início", 150, 30);
        JButton btnInverter = criarBotao("Inverter lista", 120, 30);

        // Adicionar botões ao painel horizontal
        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnInicioFim);
        painelBotoes.add(btnFimInicio);
        painelBotoes.add(btnInverter);

        // Adicionar componentes ao painel principal
        painelOperacoes.add(painelCampo);
        painelOperacoes.add(painelBotoes);

        // Configuração dos listeners (mantida igual)
        btnIncluir.addActionListener((ActionEvent e) -> {
            String texto = campoValor.getText().trim();
            if (texto.isEmpty()) {
                mostrarErro("Digite um valor para incluir na lista.");
                return;
            }

            try {
                int valor = Integer.parseInt(texto);
                lede.incluir(valor);
                campoValor.setText("");
                campoValor.requestFocus();
                mostrarInfo(valor + " incluído na lista.");
            } catch (NumberFormatException ex) {
                mostrarErro("Valor inválido. Use apenas números inteiros.");
            }
        });

        btnInicioFim.addActionListener(e -> {
            areaSaida.setText(lede.exibirInicioAoFim());
        });

        btnFimInicio.addActionListener(e -> {
            areaSaida.setText(lede.exibirFimAoInicio());
        });

        btnInverter.addActionListener(e -> {
            lede.inverterOrdem();
            mostrarInfo("Ordem da lista invertida com sucesso.");
        });

        return painelOperacoes;
    }

    private static JPanel criarPainelSaida() {
        JPanel painelSaida = new JPanel(new BorderLayout());
        painelSaida.setBorder(criarBordaTitulada("Resultado"));
        painelSaida.setOpaque(false);

        areaSaida = new JTextArea(12, 40);
        areaSaida.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaSaida.setEditable(false);
        areaSaida.setBackground(Color.WHITE);
        areaSaida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        areaSaida.setForeground(COR_TEXTO);

        JScrollPane scroll = new JScrollPane(areaSaida);
        scroll.setBorder(null);

        painelSaida.add(scroll, BorderLayout.CENTER);
        return painelSaida;
    }

    private static JButton criarBotao(String texto, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setFont(FONTE_BOTOES);
        botao.setForeground(COR_TEXTO);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(largura, altura));
        botao.setMaximumSize(new Dimension(largura, altura));

        return botao;
    }

    private static TitledBorder criarBordaTitulada(String titulo) {
        TitledBorder borda = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONTE_SUBTITULO,
                COR_PRINCIPAL);
        borda.setTitlePosition(TitledBorder.TOP);
        borda.setTitleJustification(TitledBorder.LEFT);
        return borda;
    }

    private static void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
}