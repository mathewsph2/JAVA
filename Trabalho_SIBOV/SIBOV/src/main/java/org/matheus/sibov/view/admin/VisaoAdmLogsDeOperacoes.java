/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.matheus.sibov.view.admin;

import java.util.List;
import org.matheus.perfis.Usuario;
import org.matheus.sibov.dao.CarteiraOperacoesDAO;
import org.matheus.sibov.view.TelaDeLogin;



/**
 *
 * @author mathe
 */
public class VisaoAdmLogsDeOperacoes extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisaoAdmLogsDeOperacoes.class.getName());

    /**
     * Creates new form VisaoAdministrador
     */
    private Usuario usuarioLogado;

    public VisaoAdmLogsDeOperacoes() {
        initComponents();
        
        carregarUsuariosNoComboBox();
        adicionarListenerComboBox();
    }

    public VisaoAdmLogsDeOperacoes(Usuario user) {
        this.usuarioLogado = user;
        initComponents();
        
        carregarDadosUsuario();
         carregarUsuariosNoComboBox(); // população o combo
         adicionarListenerComboBox();  //  O listener para o Combo
    }

    private void carregarDadosUsuario() {
        if (usuarioLogado != null) {
            txtUsuarioLogado.setText(usuarioLogado.getNome());
        }
    }
    
    
 
    /* ------------------------------------------------------------------------
 * Método do Listener 
 * ------------------------------------------------------------------------ */
    
  private void adicionarListenerComboBox() {
    ComboBoxUsuarios.addActionListener(evt -> {
        String usuarioSelecionado = (String) ComboBoxUsuarios.getSelectedItem();
        if (usuarioSelecionado != null) {
            if ("Todos".equals(usuarioSelecionado)) {
                carregarTabelaOperacoes(); // mostra todas
            } else {
                carregarTabelaOperacoesPorUsuario(usuarioSelecionado); // mostra filtrado
            }
        }
    });
}
  
     
    
 /* ------------------------------------------------------------------------
 * Popular o ComboBox na tela
 * ------------------------------------------------------------------------ */
    
    private void carregarUsuariosNoComboBox() {
    CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();
    List<String> usuarios = dao.listarUsuarios();

    ComboBoxUsuarios.removeAllItems();
    ComboBoxUsuarios.addItem("Todos"); // opção especial

    for (String usuario : usuarios) {
        ComboBoxUsuarios.addItem(usuario);
    }
}
    
    

    /* ------------------------------------------------------------------------
 * Método para carregar tabela filtrada
 * ------------------------------------------------------------------------ */
    
  private void carregarTabelaOperacoesPorUsuario(String nomeUsuario) {
    CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();
    List<Object[]> lista = dao.listarOperacoesPorNomeUsuario(nomeUsuario);

    String[] colunas = {
        "ID Operação", "ID Usuário", "ID Ativo", "Usuário", "Ativo", "Corretora",
        "Tipo Operação", "Valor Operação", "Quantidade", "Valor Unitário",
        "Valor Total", "Data Operação", "Observações", "Saldo Após"
    };

    javax.swing.table.DefaultTableModel modelo =
        new javax.swing.table.DefaultTableModel(colunas, 0);

    for (Object[] linha : lista) {
        modelo.addRow(linha);
    }

    TabelaOperacoesLogs.setModel(modelo);
}


    
    
    
    
/* ---------------------------------------------------------------------------- */
// método para carregar a tabela
/* ---------------------------------------------------------------------------- */
    private void carregarTabelaOperacoes() {
    CarteiraOperacoesDAO dao = new CarteiraOperacoesDAO();
    List<Object[]> lista = dao.listarOperacoes();

    // Definir colunas da tabela com nomes legíveis
    String[] colunas = {
        "ID Operação", "ID Usuário", "ID Ativo", "Usuário", "Ativo", "Corretora",
    "Tipo Operação", "Valor Operação", "Quantidade", "Valor Unitário",
    "Valor Total", "Data Operação", "Observações", "Saldo Após"
    };

    // Criar modelo e preencher com os dados
    javax.swing.table.DefaultTableModel modelo =
        new javax.swing.table.DefaultTableModel(colunas, 0);

    for (Object[] linha : lista) {
        modelo.addRow(linha);
    }

    TabelaOperacoesLogs.setModel(modelo);
    
    
    // Renderer da coluna "Valor Operação" (índice 7)  ------ Mudar com do Valor das operações 
    TabelaOperacoesLogs.getColumnModel()
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
    
    // -------------  Final do Render -------------------------------------------------------------------------
    
}

    
    

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
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaOperacoesLogs = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        ComboBoxUsuarios = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        BtnCadastro = new javax.swing.JMenu();
        btnCadastrarAtivos = new javax.swing.JMenuItem();
        btnCadastrarUsuarios = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        MenuItemProvento = new javax.swing.JMenuItem();
        BtnControle = new javax.swing.JMenu();
        LogOperacoesCarteira = new javax.swing.JMenuItem();
        MenuItemEstorno = new javax.swing.JMenuItem();
        btnRelatorios = new javax.swing.JMenu();
        MenuItemHistoricoDeOperacoes = new javax.swing.JMenuItem();
        MenuItemEventosProcessados = new javax.swing.JMenuItem();
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
        jLabel1.setText("HISTÓRICO DE OPERAÇÕES");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(Logado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuarioLogado)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logado)
                    .addComponent(txtUsuarioLogado))
                .addGap(34, 34, 34))
        );

        TabelaOperacoesLogs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID da Operacação", "ID do Usuário", "ID do Ativo", "Usuário", "Ativo", "Corretora", "Tipo de Operação", "Valor da Operação ", "Quantidade", "Valor Unitário", "Valor Total", "Data da Operação", "Observações", "Saldo Após"
            }
        ));
        jScrollPane1.setViewportView(TabelaOperacoesLogs);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Filtrar por Usuário:");

        ComboBoxUsuarios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jMenuBar1.setBackground(new java.awt.Color(153, 153, 153));

        BtnCadastro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/matheus/images/novo.png"))); // NOI18N
        BtnCadastro.setText("Cadastros");

        btnCadastrarAtivos.setText("Ativos");
        btnCadastrarAtivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCadastrarAtivosMouseClicked(evt);
            }
        });
        btnCadastrarAtivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarAtivosActionPerformed(evt);
            }
        });
        BtnCadastro.add(btnCadastrarAtivos);

        btnCadastrarUsuarios.setText("Usuários");
        btnCadastrarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarUsuariosActionPerformed(evt);
            }
        });
        BtnCadastro.add(btnCadastrarUsuarios);

        jMenuItem1.setText("Corretoras");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        BtnCadastro.add(jMenuItem1);

        MenuItemProvento.setText("Proventos");
        MenuItemProvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemProventoActionPerformed(evt);
            }
        });
        BtnCadastro.add(MenuItemProvento);

        jMenuBar1.add(BtnCadastro);

        BtnControle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/matheus/images/configuracoes.png"))); // NOI18N
        BtnControle.setText("Controle");

        LogOperacoesCarteira.setText("Logs de movimentações");
        LogOperacoesCarteira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOperacoesCarteiraActionPerformed(evt);
            }
        });
        BtnControle.add(LogOperacoesCarteira);

        MenuItemEstorno.setText("Estorno");
        MenuItemEstorno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEstornoActionPerformed(evt);
            }
        });
        BtnControle.add(MenuItemEstorno);

        jMenuBar1.add(BtnControle);

        btnRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/matheus/images/calendario.png"))); // NOI18N
        btnRelatorios.setText("Relatórios");

        MenuItemHistoricoDeOperacoes.setText("Historico de Operacoes");
        MenuItemHistoricoDeOperacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemHistoricoDeOperacoesActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemHistoricoDeOperacoes);

        MenuItemEventosProcessados.setText("Eventos Processados");
        MenuItemEventosProcessados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEventosProcessadosActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemEventosProcessados);

        jMenuBar1.add(btnRelatorios);

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
                .addGap(108, 108, 108)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(ComboBoxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ComboBoxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("Historico de Operacoes | SIBOV");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // Abrir jalenas já maximizadas:
        this.setExtendedState(this.MAXIMIZED_BOTH);
         carregarTabelaOperacoes(); // carrega os dados na tabela
    }//GEN-LAST:event_formWindowActivated


    private void btnCadastrarAtivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarAtivosActionPerformed

        // Fechar a tela atual e abrir a tela VisaoAdmCadastroAtivo:
        VisaoAdmCadastroAtivo tela = new VisaoAdmCadastroAtivo();
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCadastrarAtivosActionPerformed

    
    

    private void btnSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairMouseClicked

        // Fechar a Janela atual
        this.dispose();

        // Abre a tela de login novamente
        new TelaDeLogin().setVisible(true);

    }//GEN-LAST:event_btnSairMouseClicked

    private void btnCadastrarAtivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadastrarAtivosMouseClicked
        // Cadastrar:

    }//GEN-LAST:event_btnCadastrarAtivosMouseClicked


    private void btnCadastrarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarUsuariosActionPerformed

        // Ação do botão Cadastrar Usuários: 
        new VisaoAdmCadastroUsers().setVisible(true);
        this.dispose(); // Fecha a janela atual

    }//GEN-LAST:event_btnCadastrarUsuariosActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        
 // Ação do botão Cadastrar Usuários: 
        new VisaoAdmCadastroDeCorretoras().setVisible(true);
        this.dispose(); // Fecha a janela atual


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void MenuItemProventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventoActionPerformed
        

        // Ação do botão Cadastrar Proventos: 
        new VisaoAdmProventos().setVisible(true);
        this.dispose(); // Fecha a janela atual


    }//GEN-LAST:event_MenuItemProventoActionPerformed

    private void LogOperacoesCarteiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOperacoesCarteiraActionPerformed
          new VisaoAdmLogsDeOperacoes().setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_LogOperacoesCarteiraActionPerformed

    
    
    private void MenuItemEstornoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEstornoActionPerformed
        new VisaoAdmEstorno().setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_MenuItemEstornoActionPerformed

    private void MenuItemHistoricoDeOperacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemHistoricoDeOperacoesActionPerformed
         new VisaoAdmLogsDeOperacoes().setVisible(true);
           this.dispose(); 
    }//GEN-LAST:event_MenuItemHistoricoDeOperacoesActionPerformed

    private void MenuItemEventosProcessadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEventosProcessadosActionPerformed
        new VisaoAdministradorEventoProcessado().setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_MenuItemEventosProcessadosActionPerformed

    
    
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
        java.awt.EventQueue.invokeLater(() -> new VisaoAdmLogsDeOperacoes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu BtnCadastro;
    private javax.swing.JMenu BtnControle;
    private javax.swing.JComboBox<String> ComboBoxUsuarios;
    private javax.swing.JMenuItem LogOperacoesCarteira;
    private javax.swing.JLabel Logado;
    private javax.swing.JMenuItem MenuItemEstorno;
    private javax.swing.JMenuItem MenuItemEventosProcessados;
    private javax.swing.JMenuItem MenuItemHistoricoDeOperacoes;
    private javax.swing.JMenuItem MenuItemProvento;
    private javax.swing.JTable TabelaOperacoesLogs;
    private javax.swing.JMenuItem btnCadastrarAtivos;
    private javax.swing.JMenuItem btnCadastrarUsuarios;
    private javax.swing.JMenu btnRelatorios;
    private javax.swing.JMenu btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel txtUsuarioLogado;
    // End of variables declaration//GEN-END:variables

}
