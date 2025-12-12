// -----------------------------------------------------------------------------
//  
//
// -----------------------------------------------------------------------------
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.matheus.sibov.view.investidor;

import java.math.BigDecimal;
import javax.swing.JOptionPane;
import org.matheus.perfis.Usuario;
import org.matheus.sibov.dao.CarteiraDAO;
import org.matheus.sibov.dao.CarteiraOperacoesDAO;
import org.matheus.sibov.model.Carteira;
import org.matheus.sibov.service.ContaService;
import org.matheus.sibov.service.UsuarioService;

import org.matheus.sibov.view.TelaDeLogin;

/**
 *
 * @author mathe
 */
public class VisaoInvestidorDepositos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisaoInvestidorDepositos.class.getName());
    private Usuario usuarioLogado;
    private UsuarioService usuarioService;

    /**
     * Creates new form VisaoInvestidor
     */
    public VisaoInvestidorDepositos() {
        initComponents();

        this.usuarioService = new UsuarioService();
        carregarDadosUsuario();
    }

    // Novo construtor que recebe o usuário logado
    public VisaoInvestidorDepositos(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService();
        initComponents();
        carregarDadosUsuario();
    }

    // ----------------------------------------
    //  METODO PARA REALIZAR DEPOSITOS 
    // ----------------------------------------
  private void realizarDeposito() {
    try {
        String idContaTexto = NumeroDaContaIDConta.getText().trim();
        String valorTexto = ValorDeposito.getText().trim();

        if (idContaTexto.isEmpty() || valorTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos antes de depositar.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idConta = Integer.parseInt(idContaTexto);
        BigDecimal valor = new BigDecimal(valorTexto);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "O valor do depósito deve ser maior que zero.",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean sucesso = usuarioService.depositar(idConta, valor.doubleValue());

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Depósito realizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            // Atualizar saldo exibido na tela
            atualizarSaldo();

            // Obter saldo atualizado do usuário logado (valor numérico)
            BigDecimal saldoApos = BigDecimal.valueOf(
                usuarioService.obterSaldoInvestidor(usuarioLogado.getId())
            );

            // Registrar operação no histórico
            CarteiraOperacoesDAO operacoesDAO = new CarteiraOperacoesDAO();
            operacoesDAO.registrarOperacao(
                usuarioLogado.getId(),
                null, // sem ativo
                "DEPÓSITO",
                null,
                null,
                valor,       // valor da operação
                saldoApos,   // saldo após (igual ao que aparece na tela)
                1,           // id da corretora (default)
                "Depósito realizado por " + usuarioLogado.getNome()
            );

            ValorDeposito.setText("");
            NumeroDaContaIDConta.setText("");

        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar depósito. Verifique se o ID existe.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Digite apenas números válidos.",
                "Erro de formato",
                JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
    }
}



    // ----------------------------------------
    //  METODO PARA REALIZAR SAQUES:
    // ----------------------------------------
   private void realizarSaque() {
    try {
        String valorTexto = txtSaque.getText().trim();

        if (valorTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe o valor do saque.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal valor = new BigDecimal(valorTexto);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "O valor do saque deve ser maior que zero.",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idConta = usuarioLogado.getId();
        ContaService contaService = new ContaService();
        boolean sucesso = contaService.sacar(idConta, idConta, valor.doubleValue());

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Saque realizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            // Atualizar saldo exibido na tela
            atualizarSaldo();

            // Obter saldo atualizado do usuário logado (valor numérico)
            BigDecimal saldoApos = BigDecimal.valueOf(
                usuarioService.obterSaldoInvestidor(usuarioLogado.getId())
            );

            // Registrar operação no histórico
            CarteiraOperacoesDAO operacoesDAO = new CarteiraOperacoesDAO();
            operacoesDAO.registrarOperacao(
                usuarioLogado.getId(),
                null, // sem ativo
                "SAQUE",
                null,
                null,
                valor,       // valor da operação
                saldoApos,   // saldo após (igual ao que aparece na tela)
                1,
                "Saque realizado por " + usuarioLogado.getNome()
            );

            txtSaque.setText("");

        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar saque. Verifique seu saldo.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Digite apenas números válidos.",
                "Erro de formato",
                JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
    }
}


    // ----------------------------------------
    //  METODO PARA CARREGAR OS DADOS DO USUARIO
    // ----------------------------------------
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

    // ----------------------------------------
    //  METODO PARA ATUALIZAR O SALDO APÓS CADA OPERAÇÃO
    // ----------------------------------------
    public void atualizarSaldo() {
        if (usuarioLogado != null) {
            txtSaldo.setText(usuarioService.obterSaldoFormatado(usuarioLogado.getId()));
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

        jMenu1 = new javax.swing.JMenu();
        PainelVerdeSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        PainelVerdeInferior = new javax.swing.JPanel();
        Logado = new javax.swing.JLabel();
        VerUsuarioLogado = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNumeroConta = new javax.swing.JLabel();
        PainelDepositoSaque = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        BtnDepositar = new javax.swing.JButton();
        NumeroDaContaIDConta = new javax.swing.JTextField();
        ValorDeposito = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtSaque = new javax.swing.JTextField();
        BtnSacar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnCarteira = new javax.swing.JMenu();
        MenuItemCompraEVenda = new javax.swing.JMenuItem();
        MenuItemDepositosSaques = new javax.swing.JMenuItem();
        BtnProventos = new javax.swing.JMenu();
        MenuItemProventosAgendados = new javax.swing.JMenuItem();
        MenuItemProventosPagos = new javax.swing.JMenuItem();
        btnRelatorios = new javax.swing.JMenu();
        MenuItemExtrato = new javax.swing.JMenuItem();
        MenuCorregatem = new javax.swing.JMenu();
        MenuItemCorretagem = new javax.swing.JMenuItem();
        btnSair = new javax.swing.JMenu();

        jMenu1.setText("jMenu1");

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
        jLabel1.setText("DEPOSITOS E SAQUES");

        javax.swing.GroupLayout PainelVerdeSuperiorLayout = new javax.swing.GroupLayout(PainelVerdeSuperior);
        PainelVerdeSuperior.setLayout(PainelVerdeSuperiorLayout);
        PainelVerdeSuperiorLayout.setHorizontalGroup(
            PainelVerdeSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel5.setText("Conta:");

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
                .addGap(157, 157, 157)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
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
                    .addComponent(jLabel5)
                    .addComponent(txtNumeroConta))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Informe o numero da conta:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Informe o valor que deseja depositar:");

        BtnDepositar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        BtnDepositar.setText("Depositar");
        BtnDepositar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDepositarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BtnDepositar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(NumeroDaContaIDConta)
                    .addComponent(ValorDeposito))
                .addContainerGap(705, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(NumeroDaContaIDConta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(ValorDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(BtnDepositar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );

        PainelDepositoSaque.addTab("Depositar", jPanel1);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel6.setText("Informe o valor que deseja sacar:");

        txtSaque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaqueActionPerformed(evt);
            }
        });

        BtnSacar.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        BtnSacar.setText("Sacar");
        BtnSacar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSacarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnSacar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSaque, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(696, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jLabel6)
                .addGap(40, 40, 40)
                .addComponent(txtSaque, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(BtnSacar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        PainelDepositoSaque.addTab("Sacar", jPanel2);

        btnCarteira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carteira.png"))); // NOI18N
        btnCarteira.setText("Carteira");

        MenuItemCompraEVenda.setText("Compra - Venda de Ativo");
        MenuItemCompraEVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemCompraEVendaActionPerformed(evt);
            }
        });
        btnCarteira.add(MenuItemCompraEVenda);

        MenuItemDepositosSaques.setText("Depositar - Sacar");
        MenuItemDepositosSaques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemDepositosSaquesActionPerformed(evt);
            }
        });
        btnCarteira.add(MenuItemDepositosSaques);

        jMenuBar1.add(btnCarteira);

        BtnProventos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pagamentos.png"))); // NOI18N
        BtnProventos.setText("Proventos");

        MenuItemProventosAgendados.setText("Proventos Agendados");
        MenuItemProventosAgendados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemProventosAgendadosActionPerformed(evt);
            }
        });
        BtnProventos.add(MenuItemProventosAgendados);

        MenuItemProventosPagos.setText("Proventos Pagos");
        MenuItemProventosPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemProventosPagosActionPerformed(evt);
            }
        });
        BtnProventos.add(MenuItemProventosPagos);

        jMenuBar1.add(BtnProventos);

        btnRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/impressora.png"))); // NOI18N
        btnRelatorios.setText("Relatórios");

        MenuItemExtrato.setText("Extrato de Caixa");
        MenuItemExtrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemExtratoActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemExtrato);

        jMenuBar1.add(btnRelatorios);

        MenuCorregatem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/vendas.png"))); // NOI18N
        MenuCorregatem.setText("Corretagem");
        MenuCorregatem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuCorregatemActionPerformed(evt);
            }
        });

        MenuItemCorretagem.setText("Simulador");
        MenuItemCorretagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemCorretagemActionPerformed(evt);
            }
        });
        MenuCorregatem.add(MenuItemCorretagem);

        jMenuBar1.add(MenuCorregatem);

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
            .addComponent(PainelDepositoSaque)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PainelVerdeSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PainelDepositoSaque, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PainelVerdeInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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

    private void MenuItemDepositosSaquesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemDepositosSaquesActionPerformed
        // Chama a tela de depositos e saques
        new VisaoInvestidorDepositos(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela principal
    }//GEN-LAST:event_MenuItemDepositosSaquesActionPerformed


    private void BtnDepositarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDepositarActionPerformed
        // Chama o metodo para depositar

        realizarDeposito();

    }//GEN-LAST:event_BtnDepositarActionPerformed

    private void txtSaqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaqueActionPerformed


    private void BtnSacarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSacarActionPerformed

        realizarSaque();

    }//GEN-LAST:event_BtnSacarActionPerformed


    private void MenuItemCompraEVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemCompraEVendaActionPerformed

        // Vai para a tela de compra de Ativos :
        new VisaoInvestidorCompraAtivo(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela atual

    }//GEN-LAST:event_MenuItemCompraEVendaActionPerformed

    private void MenuItemExtratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemExtratoActionPerformed
        new VisaoInvestidorExtrato(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemExtratoActionPerformed

    private void MenuItemProventosAgendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosAgendadosActionPerformed
       new VisaoInvestidorProventosAgendados(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosAgendadosActionPerformed

    private void MenuItemProventosPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosPagosActionPerformed
        new VisaoInvestidorProventosAgendados(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosPagosActionPerformed

    private void MenuCorregatemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuCorregatemActionPerformed
         new VisaoInvestidorCorretagem(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuCorregatemActionPerformed

    private void MenuItemCorretagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemCorretagemActionPerformed
         new VisaoInvestidorCorretagem(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemCorretagemActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VisaoInvestidorDepositos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDepositar;
    private javax.swing.JMenu BtnProventos;
    private javax.swing.JButton BtnSacar;
    private javax.swing.JLabel Logado;
    private javax.swing.JMenu MenuCorregatem;
    private javax.swing.JMenuItem MenuItemCompraEVenda;
    private javax.swing.JMenuItem MenuItemCorretagem;
    private javax.swing.JMenuItem MenuItemDepositosSaques;
    private javax.swing.JMenuItem MenuItemExtrato;
    private javax.swing.JMenuItem MenuItemProventosAgendados;
    private javax.swing.JMenuItem MenuItemProventosPagos;
    private javax.swing.JTextField NumeroDaContaIDConta;
    private javax.swing.JTabbedPane PainelDepositoSaque;
    private javax.swing.JPanel PainelVerdeInferior;
    private javax.swing.JPanel PainelVerdeSuperior;
    private javax.swing.JTextField ValorDeposito;
    private javax.swing.JLabel VerUsuarioLogado;
    private javax.swing.JMenu btnCarteira;
    private javax.swing.JMenu btnRelatorios;
    private javax.swing.JMenu btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel txtNumeroConta;
    private javax.swing.JLabel txtSaldo;
    private javax.swing.JTextField txtSaque;
    // End of variables declaration//GEN-END:variables
}
