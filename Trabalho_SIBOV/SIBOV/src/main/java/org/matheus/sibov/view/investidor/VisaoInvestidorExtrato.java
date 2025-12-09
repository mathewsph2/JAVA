/* ---------------------------------------------------------------------------- */
// Extrato do Usuário Logado:
// 
/* ---------------------------------------------------------------------------- */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.matheus.sibov.view.investidor;

import java.util.List;
import org.matheus.perfis.Usuario;
import org.matheus.sibov.dao.CarteiraOperacoesDAO;
import org.matheus.sibov.service.UsuarioService;
import org.matheus.sibov.view.TelaDeLogin;

/**
 *
 * @author mathe
 */
public class VisaoInvestidorExtrato extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisaoInvestidorExtrato.class.getName());

    /**
     * Creates new form VisaoAdministrador
     */
    private Usuario usuarioLogado;
    private UsuarioService usuarioService;
    
    
   
    
    

    public VisaoInvestidorExtrato() {
        initComponents();
    }

    
   // Construtor correto que recebe o usuário logado
    public VisaoInvestidorExtrato(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService();
        initComponents();
        carregarDadosUsuario();
        
        carregarComboAtivos();
    carregarComboTipos();

    // listeners do filtro
    comboAtivo.addActionListener(e -> carregarTabelaOperacoes());
    comboTipoOperacao.addActionListener(e -> carregarTabelaOperacoes());
        
        
    }
    
    
    
    // -------------------------------------------------------------------------
    //  Chamada do filtro aplicado no DAO para o combobox Ativos 
    // -------------------------------------------------------------------------
    private void carregarComboAtivos() {
    CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();
    List<String> ativos = dao.listarAtivosPorUsuario(usuarioLogado.getId());

    comboAtivo.removeAllItems();
    comboAtivo.addItem("Todos");

    for (String ativo : ativos) {
        comboAtivo.addItem(ativo);
    }
}
    
    
    
    
    // -------------------------------------------------------------------------
    //  Chamada do filtro aplicado no DAO para o combobox Tipos
    // -------------------------------------------------------------------------
    private void carregarComboTipos() {
    CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();
    List<String> tipos = dao.listarTiposOperacaoPorUsuario(usuarioLogado.getId());

    comboTipoOperacao.removeAllItems();
    comboTipoOperacao.addItem("Todos");

    for (String tipo : tipos) {
        comboTipoOperacao.addItem(tipo);
    }
}
   
    
   
   
    // -------------------------------------------------------------------------
    //  MÉTODO PARA CARREGAR OS DADOS DO USUÁRIO LOGADO
    // -------------------------------------------------------------------------
    private void carregarDadosUsuario() {
        if (usuarioLogado != null) {
            txtUsuarioLogado.setText(usuarioLogado.getNome());
            txtSaldo.setText(usuarioService.obterSaldoFormatado(usuarioLogado.getId()));
            txtNumeroConta.setText(String.valueOf(usuarioLogado.getId()));
        } else {
            txtUsuarioLogado.setText("Usuário não identificado");
            txtSaldo.setText("R$ 0,00");
            txtNumeroConta.setText("-");
        }
    }

    public void atualizarSaldo() {
        if (usuarioLogado != null) {
            txtSaldo.setText(usuarioService.obterSaldoFormatado(usuarioLogado.getId()));
        }
    }
    
    
    
    
    
/* ----------------------------------------------------------------------------
 * Método para carregar a tabela com filtros por Ativo e Tipo de Operação
 * ---------------------------------------------------------------------------- */
private void carregarTabelaOperacoes() {
    CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();
    List<Object[]> lista = dao.listarOperacoes();

    String[] colunas = {
        "ID Operação", "ID Usuário", "ID Ativo", "Usuário", "Ativo", "Corretora",
        "Tipo Operação", "Valor Operação", "Quantidade", "Valor Unitário",
        "Valor Total", "Data Operação", "Observações", "Saldo Após"
    };

    javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(colunas, 0);

    String ativoSelecionado = (String) comboAtivo.getSelectedItem();
    String tipoSelecionado = (String) comboTipoOperacao.getSelectedItem();

    for (Object[] linha : lista) {
        int idUsuario = (Integer) linha[1];
        String ativo = (String) linha[4];
        String tipo = (String) linha[6];

        if (idUsuario == usuarioLogado.getId()) {
            boolean ativoOk = (ativoSelecionado == null || ativoSelecionado.equals("Todos"))
                              || (ativo != null && ativo.equalsIgnoreCase(ativoSelecionado));

            boolean tipoOk = (tipoSelecionado == null || tipoSelecionado.equals("Todos"))
                              || (tipo != null && tipo.equalsIgnoreCase(tipoSelecionado));

            if (ativoOk && tipoOk) {
                modelo.addRow(linha);
            }
        }
    }

    TabelaExtratoDoUsuario.setModel(modelo);

    // Renderer da coluna "Valor Operação" (índice 7) cores vermelho para saque e compra e verde para proventos
    TabelaExtratoDoUsuario.getColumnModel()
        .getColumn(7)
        .setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Pega o tipo da operação na mesma linha (coluna 6)
                Object tipoObj = table.getValueAt(row, 6);
                if (tipoObj != null) {
                    String tipoOperacao = tipoObj.toString();

                    if ("Provento".equalsIgnoreCase(tipoOperacao)) {
                        c.setForeground(java.awt.Color.GREEN);
                    } else if ("Compra".equalsIgnoreCase(tipoOperacao) || "Saque".equalsIgnoreCase(tipoOperacao)) {
                        c.setForeground(java.awt.Color.RED);
                    } else {
                        c.setForeground(java.awt.Color.BLACK);
                    }
                } else {
                    c.setForeground(java.awt.Color.BLACK);
                }

                return c;
            }
        });
   // Final do render 
}
/* ----------------------------------------------------------------------------
 * Final do método para carregar a tabela com filtros por Ativo e Tipo de Operação
 * ---------------------------------------------------------------------------- */


    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Logado = new javax.swing.JLabel();
        txtUsuarioLogado = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JLabel();
        txtNumeroConta = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaExtratoDoUsuario = new javax.swing.JTable();
        comboAtivo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        comboTipoOperacao = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnRelatorios = new javax.swing.JMenu();
        MenuItemCompraVenda = new javax.swing.JMenuItem();
        MenuItemDepositosSaques = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MenuItemPagos = new javax.swing.JMenuItem();
        MenuItemProventosAgendados = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        MenuItemExtrato = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        MenuItemSimulador = new javax.swing.JMenuItem();
        btnSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Painel administrativo do sistema financeiro SIBOV");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EXTRATO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(19, 19, 19))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        Logado.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        Logado.setText("Usuário logado:");

        txtUsuarioLogado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUsuarioLogado.setForeground(new java.awt.Color(204, 204, 204));
        txtUsuarioLogado.setText("Matheus Ribeiro");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel2.setText("Saldo:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel3.setText("Conta:");

        txtSaldo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSaldo.setForeground(new java.awt.Color(255, 255, 255));
        txtSaldo.setText("0,00");

        txtNumeroConta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNumeroConta.setForeground(new java.awt.Color(255, 255, 255));
        txtNumeroConta.setText("01");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(Logado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuarioLogado)
                .addGap(134, 134, 134)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSaldo)
                .addGap(97, 97, 97)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtNumeroConta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logado)
                    .addComponent(txtUsuarioLogado)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtSaldo)
                    .addComponent(txtNumeroConta))
                .addGap(34, 34, 34))
        );

        TabelaExtratoDoUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID da Operacação", "ID do Usuário", "ID do Ativo", "Usuário", "Ativo", "Corretora", "Tipo de Operação", "Valor da Operação ", "Quantidade", "Valor Unitário", "Valor Total", "Data da Operação", "Observações", "Saldo Após"
            }
        ));
        jScrollPane1.setViewportView(TabelaExtratoDoUsuario);

        comboAtivo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboAtivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Filtrar por Ativo:");

        comboTipoOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboTipoOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Filtrar por Tipo:");

        jMenuBar1.setBackground(new java.awt.Color(153, 153, 153));

        btnRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/matheus/images/carteira.png"))); // NOI18N
        btnRelatorios.setText("Carteira");

        MenuItemCompraVenda.setText("Compra - Venda de Ativo");
        MenuItemCompraVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemCompraVendaActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemCompraVenda);

        MenuItemDepositosSaques.setText("Depositar - Sacar");
        MenuItemDepositosSaques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemDepositosSaquesActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemDepositosSaques);

        jMenuBar1.add(btnRelatorios);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/pagamentos.png"))); // NOI18N
        jMenu2.setText("Proventos");

        MenuItemPagos.setText("Proventos Pagos");
        MenuItemPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemPagosActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItemPagos);

        MenuItemProventosAgendados.setText("Proventos Agendados");
        MenuItemProventosAgendados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemProventosAgendadosActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItemProventosAgendados);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/impressora.png"))); // NOI18N
        jMenu3.setText("Relatórios");

        MenuItemExtrato.setText("Extrato de Caixa");
        MenuItemExtrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemExtratoActionPerformed(evt);
            }
        });
        jMenu3.add(MenuItemExtrato);

        jMenuBar1.add(jMenu3);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/vendas.png"))); // NOI18N
        jMenu1.setText("Corretagem");

        MenuItemSimulador.setText("Simulador");
        MenuItemSimulador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemSimuladorActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemSimulador);

        jMenuBar1.add(jMenu1);

        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/matheus/images/sair.png"))); // NOI18N
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1207, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(comboTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(comboTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // Abrir jalenas já maximizadas:
        this.setExtendedState(this.MAXIMIZED_BOTH);
        carregarTabelaOperacoes(); // carrega os dados na tabela
    }//GEN-LAST:event_formWindowActivated



    private void btnSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairMouseClicked

        // Fechar a Janela atual
        this.dispose();

        // Abre a tela de login novamente
        new TelaDeLogin().setVisible(true);

    }//GEN-LAST:event_btnSairMouseClicked

    private void MenuItemCompraVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemCompraVendaActionPerformed
         // Vai para a tela de compra de Ativos :
        new VisaoInvestidorCompraAtivo(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela atual (opcional)
    }//GEN-LAST:event_MenuItemCompraVendaActionPerformed

    private void MenuItemDepositosSaquesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemDepositosSaquesActionPerformed
       
          // Chama a tela de depositos e saques
        new VisaoInvestidorDepositos(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela principal
        
    }//GEN-LAST:event_MenuItemDepositosSaquesActionPerformed

    private void MenuItemExtratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemExtratoActionPerformed
        
         new VisaoInvestidorExtrato(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
        
        
    }//GEN-LAST:event_MenuItemExtratoActionPerformed

    private void MenuItemPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemPagosActionPerformed
       new VisaoInvestidorProventosPagos(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemPagosActionPerformed

    private void MenuItemProventosAgendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosAgendadosActionPerformed
        new VisaoInvestidorProventosAgendados(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosAgendadosActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VisaoInvestidorExtrato().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Logado;
    private javax.swing.JMenuItem MenuItemCompraVenda;
    private javax.swing.JMenuItem MenuItemDepositosSaques;
    private javax.swing.JMenuItem MenuItemExtrato;
    private javax.swing.JMenuItem MenuItemPagos;
    private javax.swing.JMenuItem MenuItemProventosAgendados;
    private javax.swing.JMenuItem MenuItemSimulador;
    private javax.swing.JTable TabelaExtratoDoUsuario;
    private javax.swing.JMenu btnRelatorios;
    private javax.swing.JMenu btnSair;
    private javax.swing.JComboBox<String> comboAtivo;
    private javax.swing.JComboBox<String> comboTipoOperacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel txtNumeroConta;
    private javax.swing.JLabel txtSaldo;
    private javax.swing.JLabel txtUsuarioLogado;
    // End of variables declaration//GEN-END:variables

}
