// -------------------------------------------------------------------------
//  TELA DA FUNCIONALIDADE EXTRA COM TRATAMENTO DE EXCECOES PARA:
//
//Valores nulos e Valores negativos
// -------------------------------------------------------------------------
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.matheus.sibov.view.investidor;


import java.math.BigDecimal;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.matheus.perfis.Usuario;
import org.matheus.sibov.dao.AtivoDAO;
import org.matheus.sibov.dao.CorretoraDAO;
import org.matheus.sibov.model.Ativo;
import org.matheus.sibov.model.Corretora;
import org.matheus.sibov.service.UsuarioService;

import org.matheus.sibov.view.TelaDeLogin;

/**
 *
 * @author mathe
 */
public class VisaoInvestidorCorretagem extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisaoInvestidorCorretagem.class.getName());
    private Usuario usuarioLogado;
    private UsuarioService usuarioService;

    /**
     * Creates new form VisaoInvestidor
     */
    public VisaoInvestidorCorretagem() {
        initComponents();

        this.usuarioService = new UsuarioService();
        carregarDadosUsuario();
        
        carregarComboBoxes();
    }

    // Novo construtor que recebe o usuário logado
    public VisaoInvestidorCorretagem(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService();
        initComponents();
        carregarDadosUsuario();
        
        carregarComboBoxes();
      
    }

    // -------------------------------------------------------------------------
    //  METODO PARA CARREGAR OS DADOS DO USUARIO LOGADO
    // -------------------------------------------------------------------------
    private void carregarDadosUsuario() {

        if (usuarioLogado != null) {
            VerUsuarioLogado.setText(usuarioLogado.getNome());

            txtSaldo.setText(usuarioService.obterSaldoFormatado(usuarioLogado.getId()));

            txtNumeroConta.setText(String.valueOf(usuarioLogado.getId()));

        } else {
            VerUsuarioLogado.setText("Usuário não identificado");
            txtSaldo.setText("R$ 0,00");
        }
    }

    public void atualizarSaldo() {
        if (usuarioLogado != null) {
            txtSaldo.setText(usuarioService.obterSaldoFormatado(usuarioLogado.getId()));
        }
    }
    
    
    
     // -------------------------------------------------------------------------
    //  METODO PARA CARREGAR OS COMBO BOX
    // -------------------------------------------------------------------------
    private void carregarComboBoxes() {
    try {
        // Carregar ativos
        AtivoDAO ativoDAO = new AtivoDAO();
        List<Ativo> ativos = ativoDAO.listarTodos();
        DefaultComboBoxModel<String> modelAtivos = new DefaultComboBoxModel<>();
        for (Ativo a : ativos) {
            modelAtivos.addElement(a.getTicker());
        }
        ComboBoxAtivo.setModel(modelAtivos);

        // Carregar corretoras
        CorretoraDAO corretoraDAO = new CorretoraDAO();
        List<Corretora> corretoras = corretoraDAO.listarTodas();
        DefaultComboBoxModel<String> modelCorretoras = new DefaultComboBoxModel<>();
        for (Corretora c : corretoras) {
            modelCorretoras.addElement(c.getNome());
        }
        ComboBoxCorretora.setModel(modelCorretoras);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao carregar combos: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

    
    
    
     // -------------------------------------------------------------------------
    //  METODO PARA FAZER AS SIMULACOES 
    // -------------------------------------------------------------------------
    
    /*
    private void simularTaxaOperacao() {
    try {
        String ticker = (String) ComboBoxAtivo.getSelectedItem();
        String nomeCorretora = (String) ComboBoxCorretora.getSelectedItem();
        int quantidade = Integer.parseInt(txtQuantidade.getText());

        // Busca os objetos no banco
        Ativo ativo = new AtivoDAO().buscarPorTicker(ticker);
        Corretora corretora = new CorretoraDAO().buscarPorNome(nomeCorretora);

        // Faz os cálculos
        BigDecimal valorTotal = ativo.getValorCota().multiply(BigDecimal.valueOf(quantidade));
        BigDecimal taxaFixa = corretora.getTaxaFixa();
        BigDecimal taxaPercentual = corretora.getTaxaPercentual();

        BigDecimal taxaPercentualCalculada = valorTotal.multiply(taxaPercentual).divide(BigDecimal.valueOf(100));
        BigDecimal taxaTotal = taxaFixa.add(taxaPercentualCalculada);

        // Monta a mensagem detalhada
        String mensagem = String.format(
            "A quantidade de %d ativos %s na corretora %s terá uma taxa de operação de R$ %.2f.\n\n" +
            "Detalhamento:\n" +
            "- Taxa fixa: R$ %.2f\n" +
            "- Taxa percentual (%.2f%% sobre R$ %.2f): R$ %.2f",
            quantidade,
            ativo.getTicker(),
            corretora.getNome(),
            taxaTotal,
            taxaFixa,
            taxaPercentual,
            valorTotal,
            taxaPercentualCalculada
        );

        JOptionPane.showMessageDialog(this, mensagem, "Resultado da Simulação", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro na simulação: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
*/
    
    
    private void simularTaxaOperacao() {
    try {
        String textoQuantidade = txtQuantidade.getText().trim();

        // Verifica se o campo está vazio
        if (textoQuantidade.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, informe uma quantidade.",
                    "Erro de entrada", JOptionPane.ERROR_MESSAGE);
            return; // sai do método
        }

        int quantidade = Integer.parseInt(textoQuantidade);

        // Verifica se a quantidade é negativa ou zero
        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(this,
                    "A quantidade deve ser maior que zero.",
                    "Erro de entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- resto da lógica de simulação ---
        String ticker = (String) ComboBoxAtivo.getSelectedItem();
        String nomeCorretora = (String) ComboBoxCorretora.getSelectedItem();

        Ativo ativo = new AtivoDAO().buscarPorTicker(ticker);
        Corretora corretora = new CorretoraDAO().buscarPorNome(nomeCorretora);

        BigDecimal valorTotal = ativo.getValorCota().multiply(BigDecimal.valueOf(quantidade));
        BigDecimal taxaFixa = corretora.getTaxaFixa();
        BigDecimal taxaPercentual = corretora.getTaxaPercentual();

        BigDecimal taxaPercentualCalculada = valorTotal.multiply(taxaPercentual).divide(BigDecimal.valueOf(100));
        BigDecimal taxaTotal = taxaFixa.add(taxaPercentualCalculada);

        String mensagem = String.format(
            "A quantidade de %d ativos %s na corretora %s terá uma taxa de operação de R$ %.2f.\n\n" +
            "Detalhamento:\n" +
            "- Taxa fixa: R$ %.2f\n" +
            "- Taxa percentual (%.2f%% sobre R$ %.2f): R$ %.2f",
            quantidade,
            ativo.getTicker(),
            corretora.getNome(),
            taxaTotal,
            taxaFixa,
            taxaPercentual,
            valorTotal,
            taxaPercentualCalculada
        );

        JOptionPane.showMessageDialog(this, mensagem, "Resultado da Simulação", JOptionPane.INFORMATION_MESSAGE);

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Quantidade inválida. Digite apenas números inteiros.",
                "Erro de entrada", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

    
    
    
    
    
     
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PainelVerdeSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        PainelVerdeInferior = new javax.swing.JPanel();
        Logado = new javax.swing.JLabel();
        VerUsuarioLogado = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNumeroConta = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ComboBoxAtivo = new javax.swing.JComboBox<>();
        ComboBoxCorretora = new javax.swing.JComboBox<>();
        txtQuantidade = new javax.swing.JTextField();
        BtnSimulador = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnCarteira = new javax.swing.JMenu();
        BtnComprarAtivo = new javax.swing.JMenuItem();
        BtnDepositarSacar = new javax.swing.JMenuItem();
        btnProventos = new javax.swing.JMenu();
        MenuItemProventosPagos = new javax.swing.JMenuItem();
        MenuItemProventosAgendados = new javax.swing.JMenuItem();
        btnRelatorios = new javax.swing.JMenu();
        MenuItemExtrato = new javax.swing.JMenuItem();
        BtnCorretoras = new javax.swing.JMenu();
        MenuItemSimulador = new javax.swing.JMenuItem();
        btnSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Área do Investidor - Sistema financeiro SIBOV");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        PainelVerdeSuperior.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Simulador  de Corretagem por corretora");

        javax.swing.GroupLayout PainelVerdeSuperiorLayout = new javax.swing.GroupLayout(PainelVerdeSuperior);
        PainelVerdeSuperior.setLayout(PainelVerdeSuperiorLayout);
        PainelVerdeSuperiorLayout.setHorizontalGroup(
            PainelVerdeSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1085, Short.MAX_VALUE)
        );
        PainelVerdeSuperiorLayout.setVerticalGroup(
            PainelVerdeSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelVerdeSuperiorLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        PainelVerdeInferior.setBackground(new java.awt.Color(0, 102, 102));

        Logado.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        Logado.setText("Usuário logado:");

        VerUsuarioLogado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        VerUsuarioLogado.setForeground(new java.awt.Color(255, 255, 255));
        VerUsuarioLogado.setText("Matheus Ribeiro");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel2.setText("Saldo:");

        txtSaldo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSaldo.setForeground(new java.awt.Color(255, 255, 255));
        txtSaldo.setText("R$ 0,00");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel3.setText("Conta:");

        txtNumeroConta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNumeroConta.setForeground(new java.awt.Color(255, 255, 255));
        txtNumeroConta.setText("01");

        javax.swing.GroupLayout PainelVerdeInferiorLayout = new javax.swing.GroupLayout(PainelVerdeInferior);
        PainelVerdeInferior.setLayout(PainelVerdeInferiorLayout);
        PainelVerdeInferiorLayout.setHorizontalGroup(
            PainelVerdeInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelVerdeInferiorLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(Logado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VerUsuarioLogado)
                .addGap(127, 127, 127)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSaldo)
                .addGap(119, 119, 119)
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(txtNumeroConta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PainelVerdeInferiorLayout.setVerticalGroup(
            PainelVerdeInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelVerdeInferiorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(PainelVerdeInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logado)
                    .addComponent(VerUsuarioLogado)
                    .addComponent(jLabel2)
                    .addComponent(txtSaldo)
                    .addComponent(jLabel3)
                    .addComponent(txtNumeroConta))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Selecione um ativo:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Selecione a Corretora: ");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Quantidade desejada:");

        ComboBoxAtivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ComboBoxCorretora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtQuantidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantidadeActionPerformed(evt);
            }
        });

        BtnSimulador.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        BtnSimulador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/buscar.png"))); // NOI18N
        BtnSimulador.setText("Simular");
        BtnSimulador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimuladorActionPerformed(evt);
            }
        });

        btnCarteira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carteira.png"))); // NOI18N
        btnCarteira.setText("Carteira");
        btnCarteira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarteiraActionPerformed(evt);
            }
        });

        BtnComprarAtivo.setText("Compra - Venda de Ativo");
        BtnComprarAtivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnComprarAtivoActionPerformed(evt);
            }
        });
        btnCarteira.add(BtnComprarAtivo);

        BtnDepositarSacar.setText("Depositar - Sacar");
        BtnDepositarSacar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDepositarSacarActionPerformed(evt);
            }
        });
        btnCarteira.add(BtnDepositarSacar);

        jMenuBar1.add(btnCarteira);

        btnProventos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pagamentos.png"))); // NOI18N
        btnProventos.setText("Proventos");

        MenuItemProventosPagos.setText("Proventos Pagos");
        MenuItemProventosPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemProventosPagosActionPerformed(evt);
            }
        });
        btnProventos.add(MenuItemProventosPagos);

        MenuItemProventosAgendados.setText("Proventos Agendados");
        MenuItemProventosAgendados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemProventosAgendadosActionPerformed(evt);
            }
        });
        btnProventos.add(MenuItemProventosAgendados);

        jMenuBar1.add(btnProventos);

        btnRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/impressora.png"))); // NOI18N
        btnRelatorios.setText("Relatórios");
        btnRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatoriosActionPerformed(evt);
            }
        });

        MenuItemExtrato.setText("Extrato de caixa");
        MenuItemExtrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemExtratoActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemExtrato);

        jMenuBar1.add(btnRelatorios);

        BtnCorretoras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/vendas.png"))); // NOI18N
        BtnCorretoras.setText("Corretagem");
        BtnCorretoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCorretorasActionPerformed(evt);
            }
        });

        MenuItemSimulador.setText("Simulador");
        MenuItemSimulador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemSimuladorActionPerformed(evt);
            }
        });
        BtnCorretoras.add(MenuItemSimulador);

        jMenuBar1.add(BtnCorretoras);

        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sair.png"))); // NOI18N
        btnSair.setText("Sair");
        btnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairMouseClicked(evt);
            }
        });
        jMenuBar1.add(btnSair);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PainelVerdeSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PainelVerdeInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnSimulador, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ComboBoxAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ComboBoxCorretora, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PainelVerdeSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(ComboBoxAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ComboBoxCorretora, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(79, 79, 79))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(BtnSimulador, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96)
                .addComponent(PainelVerdeInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("Taxas de Corretagem por corretora - Sistema financeiro SIBOV");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        // Para abrir a janela já maximizada, adicionado esse trecho de código:
        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.setVisible(true);

        carregarDadosUsuario();
    }//GEN-LAST:event_formWindowActivated

    private void btnSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairMouseClicked

        // Fechar a Janela atual
        this.dispose();

        // Abre a tela de login novamente
        new TelaDeLogin().setVisible(true);

    }//GEN-LAST:event_btnSairMouseClicked


    private void BtnDepositarSacarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDepositarSacarActionPerformed

        // Chama a tela de depositos e saques
        new VisaoInvestidorDepositos(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela principal

    }//GEN-LAST:event_BtnDepositarSacarActionPerformed


    private void BtnComprarAtivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnComprarAtivoActionPerformed

        // Vai para a tela de compra de Ativos :
        new VisaoInvestidorCompraAtivo(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela atual (opcional)

    }//GEN-LAST:event_BtnComprarAtivoActionPerformed

    private void MenuItemExtratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemExtratoActionPerformed
        new VisaoInvestidorExtrato(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemExtratoActionPerformed

    private void MenuItemProventosAgendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosAgendadosActionPerformed
         new VisaoInvestidorProventosAgendados(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosAgendadosActionPerformed

    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRelatoriosActionPerformed

    private void btnCarteiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarteiraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCarteiraActionPerformed

    private void MenuItemProventosPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosPagosActionPerformed
         new VisaoInvestidorProventosPagos(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosPagosActionPerformed

    private void BtnCorretorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCorretorasActionPerformed
        // Vai para a tela de compra de Ativos :
        new VisaoInvestidorCorretagem(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela atual (opcional)
    }//GEN-LAST:event_BtnCorretorasActionPerformed

    private void txtQuantidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeActionPerformed

    private void BtnSimuladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimuladorActionPerformed
      
      simularTaxaOperacao(); 
    }//GEN-LAST:event_BtnSimuladorActionPerformed

    private void MenuItemSimuladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemSimuladorActionPerformed
        new VisaoInvestidorCorretagem(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela atual (opcional)
    }//GEN-LAST:event_MenuItemSimuladorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VisaoInvestidorCorretagem().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BtnComprarAtivo;
    private javax.swing.JMenu BtnCorretoras;
    private javax.swing.JMenuItem BtnDepositarSacar;
    private javax.swing.JButton BtnSimulador;
    private javax.swing.JComboBox<String> ComboBoxAtivo;
    private javax.swing.JComboBox<String> ComboBoxCorretora;
    private javax.swing.JLabel Logado;
    private javax.swing.JMenuItem MenuItemExtrato;
    private javax.swing.JMenuItem MenuItemProventosAgendados;
    private javax.swing.JMenuItem MenuItemProventosPagos;
    private javax.swing.JMenuItem MenuItemSimulador;
    private javax.swing.JPanel PainelVerdeInferior;
    private javax.swing.JPanel PainelVerdeSuperior;
    private javax.swing.JLabel VerUsuarioLogado;
    private javax.swing.JMenu btnCarteira;
    private javax.swing.JMenu btnProventos;
    private javax.swing.JMenu btnRelatorios;
    private javax.swing.JMenu btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel txtNumeroConta;
    private javax.swing.JTextField txtQuantidade;
    private javax.swing.JLabel txtSaldo;
    // End of variables declaration//GEN-END:variables
}
