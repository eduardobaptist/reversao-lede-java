package lede;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static java.lang.String.format;

public class Main {
    private static final Lede lede = new Lede();

    private static final Color COR_PRINCIPAL = new Color(40, 107, 163);
    private static final Color COR_SECUNDARIA = new Color(245, 245, 245);
    private static final Color COR_TEXTO = new Color(60, 60, 60);
    private static final Color COR_BORDA = new Color(210, 210, 210);

    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_BOTOES = new Font("Segoe UI", Font.BOLD, 16);

    private static JTextArea areaSaida;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        frame.setSize(800, 650);
        frame.setMinimumSize(new Dimension(600, 450));
        frame.setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelPrincipal.setBackground(COR_SECUNDARIA);

        JLabel titulo = new JLabel("Lista Estática Duplamente Encadeada (LEDE)", JLabel.CENTER);
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_PRINCIPAL);
        titulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel painelOperacoes = criarPainelOperacoes();
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
        painelOperacoes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelCampo = new JPanel();
        painelCampo.setLayout(new BoxLayout(painelCampo, BoxLayout.X_AXIS));
        painelCampo.setOpaque(false);
        painelCampo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelValor = new JLabel("Valor:");
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelValor.setForeground(COR_TEXTO);

        JTextField campoValor = new JTextField(12);
        campoValor.setFont(new Font("Arial", Font.PLAIN, 18));
        campoValor.setPreferredSize(new Dimension(200, 32));
        campoValor.setMaximumSize(campoValor.getPreferredSize());

        painelCampo.add(labelValor);
        painelCampo.add(Box.createHorizontalStrut(10));
        painelCampo.add(campoValor);

        JPanel painelBotoes = new JPanel(new GridLayout(0, 2, 10, 10));
        painelBotoes.setOpaque(false);
        painelBotoes.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnIncluir = criarBotao("Incluir");
        JButton btnInicioFim = criarBotao("Exibir início → fim");
        JButton btnFimInicio = criarBotao("Exibir fim → início");
        JButton btnInverter = criarBotao("Inverter lista");
        JButton btnRemover = criarBotao("Remover por índice");

        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnInicioFim);
        painelBotoes.add(btnFimInicio);
        painelBotoes.add(btnInverter);

        painelOperacoes.add(painelCampo);
        painelOperacoes.add(Box.createVerticalStrut(20));
        painelOperacoes.add(painelBotoes);

        btnIncluir.addActionListener(_ -> {
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
            } catch (IndexOutOfBoundsException exception) {
                mostrarErro("Lista já está cheia (10 elementos).");
            }
        });

        btnInicioFim.addActionListener(_ -> areaSaida.setText(lede.exibirInicioAoFim()));
        btnFimInicio.addActionListener(_ -> areaSaida.setText(lede.exibirFimAoInicio()));
        btnInverter.addActionListener(_ -> {
            lede.inverterOrdem();
            mostrarInfo("Ordem da lista invertida com sucesso.");
        });

        btnRemover.addActionListener(_ -> {
            String texto = campoValor.getText().trim();
            if (texto.isEmpty()) {
                mostrarErro("Digite o índice a ser removido da lista.");
                return;
            }
            try {
                int valor = Integer.parseInt(texto);
                lede.remover(valor);
                campoValor.setText("");
                campoValor.requestFocus();
                mostrarInfo(format("Índice %d removido da lista.", valor));
            } catch (NumberFormatException ex) {
                mostrarErro("Valor inválido. Use apenas números inteiros.");
            } catch (IndexOutOfBoundsException ex) {
                mostrarErro("Índice não existe na lista.");
            }
        });

        return painelOperacoes;
    }

    private static JPanel criarPainelSaida() {
        JPanel painelSaida = new JPanel(new BorderLayout());
        painelSaida.setBorder(criarBordaTitulada("Resultado"));
        painelSaida.setOpaque(false);

        areaSaida = new JTextArea(5, 40);
        areaSaida.setFont(new Font("Monospaced", Font.PLAIN, 18));
        areaSaida.setEditable(false);
        areaSaida.setBackground(Color.WHITE);
        areaSaida.setForeground(COR_TEXTO);
        areaSaida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JScrollPane scroll = new JScrollPane(areaSaida);
        scroll.setBorder(null);

        painelSaida.add(scroll, BorderLayout.CENTER);
        return painelSaida;
    }

    private static JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(FONTE_BOTOES);
        botao.setForeground(COR_TEXTO);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(180, 40));
        botao.setMaximumSize(new Dimension(180, 40));
        return botao;
    }

    private static TitledBorder criarBordaTitulada(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONTE_SUBTITULO,
                COR_PRINCIPAL);
    }

    private static void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
}
