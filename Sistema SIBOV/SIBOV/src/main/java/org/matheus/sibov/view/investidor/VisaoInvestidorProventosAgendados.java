// -------------------------------------------------------------------------
//  TELA MODELO PARA VISÃO DO INVESTIDOR 
//
// -------------------------------------------------------------------------
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.matheus.sibov.view.investidor;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.matheus.perfis.Usuario;
import org.matheus.sibov.dao.ProventoDAO;
import org.matheus.sibov.service.UsuarioService;

import org.matheus.sibov.view.TelaDeLogin;

/**
 *
 * @author mathe
 */
public class VisaoInvestidorProventosAgendados extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisaoInvestidorProventosAgendados.class.getName());
    private Usuario usuarioLogado;
    private UsuarioService usuarioService;
    private ProventoDAO proventoDAO;
    
    
    /**
     * Creates new form VisaoInvestidor
     */
    public VisaoInvestidorProventosAgendados() {
        initComponents();

        this.usuarioService = new UsuarioService();
        carregarDadosUsuario();
    }

    
    // Novo construtor que recebe o usuário logado
    public VisaoInvestidorProventosAgendados(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService();
         this.proventoDAO = new ProventoDAO();
        initComponents();
        
        
        // aqui você define os itens do combobox de período
    ComboBoxFiltraPorPeriodo.setModel(new javax.swing.DefaultComboBoxModel<>(
        new String[] { "Todos", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                       "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }
    ));
        
        
        carregarDadosUsuario();
        
        
        
        
        
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
    
    
    
    
    private void carregarTabelaProventosAgendados() {
    DefaultTableModel modelo = (DefaultTableModel) TabelaProventosAgendados.getModel();
    modelo.setRowCount(0); // Limpa a tabela

    List<Object[]> dados = proventoDAO.buscarProventosAgendadosPorUsuario(usuarioLogado.getId());

    String ativoSelecionado = (String) ComboBoxFiltraPorAtivo.getSelectedItem();
    String periodoSelecionado = (String) ComboBoxFiltraPorPeriodo.getSelectedItem();

    for (Object[] linha : dados) {
        String ticker = (String) linha[1]; // índice 1 = Ticket
        Date dataPagamento = (Date) linha[8]; // índice 8 = Data de Pagamento

        boolean ativoOk = ativoSelecionado == null 
                          || ativoSelecionado.equals("Todos") 
                          || ticker.equalsIgnoreCase(ativoSelecionado);

        boolean periodoOk = true;
        if (periodoSelecionado != null && !"Todos".equals(periodoSelecionado) && dataPagamento != null) {
            int mesPagamento = dataPagamento.toLocalDate().getMonthValue(); // 1 = Janeiro, 12 = Dezembro

            int mesSelecionado = switch (periodoSelecionado) {
                case "Janeiro" -> 1;
                case "Fevereiro" -> 2;
                case "Março" -> 3;
                case "Abril" -> 4;
                case "Maio" -> 5;
                case "Junho" -> 6;
                case "Julho" -> 7;
                case "Agosto" -> 8;
                case "Setembro" -> 9;
                case "Outubro" -> 10;
                case "Novembro" -> 11;
                case "Dezembro" -> 12;
                default -> 0;
            };

            periodoOk = (mesSelecionado == mesPagamento);
        }

        if (ativoOk && periodoOk) {
            modelo.addRow(linha);
        }
    }
}
    
    
    
    
    
    private void carregarComboAtivos() {
    List<String> tickers = proventoDAO.listarTickersProventosAgendadosPorUsuario(usuarioLogado.getId());

    ComboBoxFiltraPorAtivo.removeAllItems();
    ComboBoxFiltraPorAtivo.addItem("Todos"); // opção padrão

    for (String ticker : tickers) {
        ComboBoxFiltraPorAtivo.addItem(ticker);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaProventosAgendados = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        ComboBoxFiltraPorAtivo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        ComboBoxFiltraPorPeriodo = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnCarteira = new javax.swing.JMenu();
        BtnComprarAtivo = new javax.swing.JMenuItem();
        BtnDepositarSacar = new javax.swing.JMenuItem();
        btnProventos = new javax.swing.JMenu();
        MenuItemPago = new javax.swing.JMenuItem();
        MenuItemProventosAgendados = new javax.swing.JMenuItem();
        btnRelatorios = new javax.swing.JMenu();
        MenuSuperiorExtratoDoUsuario = new javax.swing.JMenuItem();
        BtnCorretagem = new javax.swing.JMenu();
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
        jLabel1.setText("PROVENTOS AGENDADOS");

        javax.swing.GroupLayout PainelVerdeSuperiorLayout = new javax.swing.GroupLayout(PainelVerdeSuperior);
        PainelVerdeSuperior.setLayout(PainelVerdeSuperiorLayout);
        PainelVerdeSuperiorLayout.setHorizontalGroup(
            PainelVerdeSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelVerdeSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
                .addContainerGap())
        );
        PainelVerdeSuperiorLayout.setVerticalGroup(
            PainelVerdeSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelVerdeSuperiorLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addContainerGap(37, Short.MAX_VALUE))
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

        TabelaProventosAgendados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID do Ativo", "Ticket", "Tipo", "Valor da Cota", "Quantidade Total", "Valor Investido", "Tipo", "Valor por Cota", "Data de Pagamento", "Status"
            }
        ));
        jScrollPane1.setViewportView(TabelaProventosAgendados);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Filtrar por Ativo:");

        ComboBoxFiltraPorAtivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Filtrar por Período: ");

        ComboBoxFiltraPorPeriodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnCarteira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carteira.png"))); // NOI18N
        btnCarteira.setText("Carteira");

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

        MenuItemPago.setText("Proventos Pagos");
        MenuItemPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemPagoActionPerformed(evt);
            }
        });
        btnProventos.add(MenuItemPago);

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

        MenuSuperiorExtratoDoUsuario.setText("Extrato de caixa");
        MenuSuperiorExtratoDoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSuperiorExtratoDoUsuarioActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuSuperiorExtratoDoUsuario);

        jMenuBar1.add(btnRelatorios);

        BtnCorretagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/vendas.png"))); // NOI18N
        BtnCorretagem.setText("Corretagem");

        MenuItemSimulador.setText("Simulador");
        MenuItemSimulador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemSimuladorActionPerformed(evt);
            }
        });
        BtnCorretagem.add(MenuItemSimulador);

        jMenuBar1.add(BtnCorretagem);

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
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(ComboBoxFiltraPorAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addComponent(jLabel5)
                .addGap(29, 29, 29)
                .addComponent(ComboBoxFiltraPorPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PainelVerdeSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ComboBoxFiltraPorAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(ComboBoxFiltraPorPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PainelVerdeInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("Proventos Agendados - Sistema financeiro SIBOV");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

      this.setExtendedState(this.MAXIMIZED_BOTH);
    this.setVisible(true);

    carregarDadosUsuario();
    carregarComboAtivos();

     carregarTabelaProventosAgendados();

    // Listener para atualizar tabela ao mudar filtro
    ComboBoxFiltraPorAtivo.addActionListener(e -> carregarTabelaProventosAgendados());
    ComboBoxFiltraPorPeriodo.addActionListener(e -> carregarTabelaProventosAgendados());
    

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

    private void MenuSuperiorExtratoDoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSuperiorExtratoDoUsuarioActionPerformed
        new VisaoInvestidorExtrato(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuSuperiorExtratoDoUsuarioActionPerformed

    private void MenuItemProventosAgendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosAgendadosActionPerformed
         new VisaoInvestidorProventosAgendados(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosAgendadosActionPerformed

    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRelatoriosActionPerformed

    private void MenuItemPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemPagoActionPerformed
        new VisaoInvestidorProventosPagos(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemPagoActionPerformed

    private void MenuItemSimuladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemSimuladorActionPerformed
       new VisaoInvestidorCorretagem(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
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
        java.awt.EventQueue.invokeLater(() -> new VisaoInvestidorProventosAgendados().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BtnComprarAtivo;
    private javax.swing.JMenu BtnCorretagem;
    private javax.swing.JMenuItem BtnDepositarSacar;
    private javax.swing.JComboBox<String> ComboBoxFiltraPorAtivo;
    private javax.swing.JComboBox<String> ComboBoxFiltraPorPeriodo;
    private javax.swing.JLabel Logado;
    private javax.swing.JMenuItem MenuItemPago;
    private javax.swing.JMenuItem MenuItemProventosAgendados;
    private javax.swing.JMenuItem MenuItemSimulador;
    private javax.swing.JMenuItem MenuSuperiorExtratoDoUsuario;
    private javax.swing.JPanel PainelVerdeInferior;
    private javax.swing.JPanel PainelVerdeSuperior;
    private javax.swing.JTable TabelaProventosAgendados;
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
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel txtNumeroConta;
    private javax.swing.JLabel txtSaldo;
    // End of variables declaration//GEN-END:variables
}
