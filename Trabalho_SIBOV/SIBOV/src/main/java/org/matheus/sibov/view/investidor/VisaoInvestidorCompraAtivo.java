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
import java.util.List;
import javax.swing.JOptionPane;
import org.matheus.perfis.Usuario;
import org.matheus.sibov.dao.AtivoDAO;
import org.matheus.sibov.dao.CarteiraAtivosDAO;
import org.matheus.sibov.dao.CarteiraDAO;
import org.matheus.sibov.dao.CarteiraPosicaoDAO;
import org.matheus.sibov.model.Ativo;
import org.matheus.sibov.model.Carteira;
import org.matheus.sibov.model.CarteiraAtivo;
import org.matheus.sibov.service.CarteiraService;
import org.matheus.sibov.service.CarteiraServiceSaldo;
import org.matheus.sibov.service.UsuarioService;
import org.matheus.sibov.dao.CarteiraOperacoesDAO;

import org.matheus.sibov.view.TelaDeLogin;

/**
 *
 * @author mathe
 */
public class VisaoInvestidorCompraAtivo extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VisaoInvestidorCompraAtivo.class.getName());
    private Usuario usuarioLogado;
    private UsuarioService usuarioService;
    private AtivoDAO ativoDAO = new AtivoDAO();
    private CarteiraOperacoesDAO operacoesDAO = new CarteiraOperacoesDAO();

    /**
     * Creates new form VisaoInvestidor
     */
    public VisaoInvestidorCompraAtivo() {
        initComponents();

        this.usuarioService = new UsuarioService();
        carregarDadosUsuario();
    }

    // Novo construtor que recebe o usuário logado
    public VisaoInvestidorCompraAtivo(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService();
        initComponents();
        carregarDadosUsuario();
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

    public void atualizarSaldo() {
        if (usuarioLogado != null) {
            txtSaldo.setText(usuarioService.obterSaldoFormatado(usuarioLogado.getId()));
        }
    }

    // ----------------------------------------
    //  METODO PARA CARREGAR OS ATIVOS DISPONÍVEIS
    // ----------------------------------------
    private void carregarTabelaAtivos() {

        List<Ativo> lista = ativoDAO.listarTodos();

        String[] colunas = {"Ticker", "Tipo", "Segmento", "Valor da Cota"};
        Object[][] dados = new Object[lista.size()][4];

        for (int i = 0; i < lista.size(); i++) {
            Ativo a = lista.get(i);
            dados[i][0] = a.getTicker();
            dados[i][1] = a.getTipo().name();
            dados[i][2] = a.getSegmento();
            dados[i][3] = "R$ " + a.getValorCota();
        }

        tabelaAtivos.setModel(new javax.swing.table.DefaultTableModel(dados, colunas));
    }

    // ----------------------------------------
    //  MÉTODO PARA CARREGAR POSIÇÃO DA CARTEIRA
    // ----------------------------------------
    private void carregarTabelaPosicaoCarteira() {
        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuário não identificado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        CarteiraPosicaoDAO carteiraPosicaoDAO = new CarteiraPosicaoDAO();
        List<Object[]> lista = carteiraPosicaoDAO.buscarPosicaoCarteira(usuarioLogado.getId());

        String[] colunas = {
            "Ticker", "Tipo", "Quantidade Total", "Preço Médio", "Valor Investido", "Valor de Mercado"
        };

        Object[][] dados = new Object[lista.size()][colunas.length];

        for (int i = 0; i < lista.size(); i++) {
            Object[] linha = lista.get(i);
            dados[i][0] = linha[0]; // ticker
            dados[i][1] = linha[1]; // tipo
            dados[i][2] = linha[2]; // quantidade_total
            dados[i][3] = linha[3]; // preco_medio
            dados[i][4] = linha[4]; // valor_investido
            dados[i][5] = linha[5]; // valor_mercado
        }

        TabelaDePosicaoAtivos.setModel(new javax.swing.table.DefaultTableModel(dados, colunas));
    }

    // ------------------------------------------------------------------------------
//  MÉTODO PARA COMPRAR ATIVO
// ------------------------------------------------------------------------------
    private void comprarAtivo() {
        int linha = tabelaAtivos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um ativo na tabela para comprar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ticker = tabelaAtivos.getValueAt(linha, 0).toString();
        Ativo ativoSelecionado = ativoDAO.buscarPorTicker(ticker);
        if (ativoSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar informações desse ativo.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String inputQtd = JOptionPane.showInputDialog(this,
                "Informe a quantidade que deseja comprar:");
        if (inputQtd == null) {
            return;
        }

        int qtd;
        try {
            qtd = Integer.parseInt(inputQtd);
            if (qtd <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade inválida.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal precoUnit = ativoSelecionado.getValorCota();
        BigDecimal totalCompra = precoUnit.multiply(new BigDecimal(qtd));

        CarteiraServiceSaldo carteiraServiceSaldo = new CarteiraServiceSaldo();
        boolean sucesso = carteiraServiceSaldo.comprarAtivo(
                usuarioLogado.getId(),
                ativoSelecionado.getId(),
                qtd,
                precoUnit
        );

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Compra realizada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            atualizarSaldo();

            BigDecimal saldoApos = BigDecimal.valueOf(
                    usuarioService.obterSaldoInvestidor(usuarioLogado.getId())
            );

            operacoesDAO.registrarOperacao(
                    usuarioLogado.getId(),
                    ativoSelecionado.getId(),
                    "COMPRA",
                    qtd,
                    precoUnit,
                    totalCompra,
                    saldoApos,
                    1,
                    "Compra realizada por " + usuarioLogado.getNome()
            );

            carregarTabelaPosicaoCarteira();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao efetuar a compra. Verifique seu saldo.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    
    
// ------------------------------------------------------------------------------
//  MÉTODO PARA VENDER ATIVO
// ------------------------------------------------------------------------------
    private void venderAtivo() {
        int linha = TabelaDePosicaoAtivos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um ativo na tabela para vender.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ticker = TabelaDePosicaoAtivos.getValueAt(linha, 0).toString();
        Ativo ativoSelecionado = ativoDAO.buscarPorTicker(ticker);
        if (ativoSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar informações desse ativo.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String inputQtd = JOptionPane.showInputDialog(this,
                "Informe a quantidade que deseja vender:");
        if (inputQtd == null) {
            return;
        }

        int qtd;
        try {
            qtd = Integer.parseInt(inputQtd);
            if (qtd <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade inválida.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        CarteiraAtivosDAO carteiraAtivosDAO = new CarteiraAtivosDAO();
        CarteiraAtivo posicao = carteiraAtivosDAO.buscar(usuarioLogado.getId(), ativoSelecionado.getId());
        if (posicao == null || posicao.getQuantidadeTotal() < qtd) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade insuficiente para venda.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal precoUnit = ativoSelecionado.getValorCota();
        BigDecimal totalVenda = precoUnit.multiply(new BigDecimal(qtd));

        int novaQtd = posicao.getQuantidadeTotal() - qtd;
        if (novaQtd > 0) {
            posicao.setQuantidadeTotal(novaQtd);
            posicao.setValorInvestido(precoUnit.multiply(new BigDecimal(novaQtd)));
            posicao.setUltimoValorCota(precoUnit);
            carteiraAtivosDAO.atualizar(posicao);
        } else {
            carteiraAtivosDAO.deletar(usuarioLogado.getId(), ativoSelecionado.getId());
        }

        CarteiraDAO carteiraDAO = new CarteiraDAO();
        Carteira carteira = carteiraDAO.buscarPorInvestidor(usuarioLogado.getId());
        BigDecimal saldoAtual = carteira != null ? carteira.getSaldo() : BigDecimal.ZERO;
        BigDecimal novoSaldo = saldoAtual.add(totalVenda);
        carteiraDAO.atualizarSaldo(usuarioLogado.getId(), novoSaldo);

        JOptionPane.showMessageDialog(this,
                "Venda realizada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        atualizarSaldo();

        BigDecimal saldoApos = BigDecimal.valueOf(
                usuarioService.obterSaldoInvestidor(usuarioLogado.getId())
        );

        operacoesDAO.registrarOperacao(
                usuarioLogado.getId(),
                ativoSelecionado.getId(),
                "VENDA",
                qtd,
                precoUnit,
                totalVenda,
                saldoApos,
                1,
                "Venda realizada por " + usuarioLogado.getNome()
        );

        carregarTabelaPosicaoCarteira();

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
        PainelAtivos = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelaDePosicaoAtivos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaAtivos = new javax.swing.JTable();
        btnComprarAtivo = new javax.swing.JButton();
        BtnVendaDeAtivo = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnCarteira = new javax.swing.JMenu();
        BtnComprarAtivo = new javax.swing.JMenuItem();
        BtnDepositar = new javax.swing.JMenuItem();
        btnProventos = new javax.swing.JMenu();
        MenuItemProventosPagos = new javax.swing.JMenuItem();
        MenuItemProventosAgendados = new javax.swing.JMenuItem();
        btnRelatorios = new javax.swing.JMenu();
        MenuItemExtrato = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        MenuItemCorretagem = new javax.swing.JMenuItem();
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
        jLabel1.setText("ATIVOS");

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

        TabelaDePosicaoAtivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ticket", "Tipo", "Quantidade Total", "Preço Médio", "Valor investido", "Valor de Mercado"
            }
        ));
        jScrollPane2.setViewportView(TabelaDePosicaoAtivos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1085, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        PainelAtivos.addTab("Listar meus Ativos", jPanel2);

        tabelaAtivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Ticket", "Tipo", "Segmento", "Valor da Cota"
            }
        ));
        jScrollPane1.setViewportView(tabelaAtivos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1085, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        PainelAtivos.addTab("Comprar Ativo", jPanel1);

        btnComprarAtivo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnComprarAtivo.setText("Comprar Ativo");
        btnComprarAtivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprarAtivoActionPerformed(evt);
            }
        });

        BtnVendaDeAtivo.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        BtnVendaDeAtivo.setText("Vender Ativo");
        BtnVendaDeAtivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnVendaDeAtivoActionPerformed(evt);
            }
        });

        btnCarteira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carteira.png"))); // NOI18N
        btnCarteira.setText("Carteira");

        BtnComprarAtivo.setText("Compra - Venda de Ativo");
        btnCarteira.add(BtnComprarAtivo);

        BtnDepositar.setText("Depositar - Sacar");
        BtnDepositar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDepositarActionPerformed(evt);
            }
        });
        btnCarteira.add(BtnDepositar);

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

        MenuItemExtrato.setText("Extrato de caixa");
        MenuItemExtrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemExtratoActionPerformed(evt);
            }
        });
        btnRelatorios.add(MenuItemExtrato);

        jMenuBar1.add(btnRelatorios);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/images/vendas.png"))); // NOI18N
        jMenu1.setText("Corretagem");

        MenuItemCorretagem.setText("Simulador");
        MenuItemCorretagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemCorretagemActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemCorretagem);

        jMenuBar1.add(jMenu1);

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
            .addComponent(PainelAtivos, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnVendaDeAtivo)
                .addGap(48, 48, 48)
                .addComponent(btnComprarAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BtnVendaDeAtivo, btnComprarAtivo});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PainelVerdeSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PainelAtivos, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnComprarAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnVendaDeAtivo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PainelVerdeInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BtnVendaDeAtivo, btnComprarAtivo});

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        // Para abrir a janela já maximizada, adicionado esse trecho de código:
        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.setVisible(true);
        carregarDadosUsuario();

        carregarTabelaAtivos();
        carregarTabelaPosicaoCarteira();

    }//GEN-LAST:event_formWindowActivated

    private void btnSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairMouseClicked

        // Fechar a Janela atual
        this.dispose();

        // Abre a tela de login novamente
        new TelaDeLogin().setVisible(true);

    }//GEN-LAST:event_btnSairMouseClicked

    private void BtnDepositarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDepositarActionPerformed

        // Chama a tela de depositos e nessa tela tem uma aba SAQUES saques
        new VisaoInvestidorDepositos(usuarioLogado).setVisible(true);
        this.dispose(); // Fecha a tela atual 

    }//GEN-LAST:event_BtnDepositarActionPerformed


    private void btnComprarAtivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprarAtivoActionPerformed

        comprarAtivo();

    }//GEN-LAST:event_btnComprarAtivoActionPerformed


    private void BtnVendaDeAtivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnVendaDeAtivoActionPerformed

        venderAtivo();

    }//GEN-LAST:event_BtnVendaDeAtivoActionPerformed

    private void MenuItemProventosAgendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosAgendadosActionPerformed
         new VisaoInvestidorProventosAgendados(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosAgendadosActionPerformed

    private void MenuItemExtratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemExtratoActionPerformed
        new VisaoInvestidorExtrato(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemExtratoActionPerformed

    private void MenuItemProventosPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemProventosPagosActionPerformed
        new VisaoInvestidorProventosPagos(usuarioLogado).setVisible(true); // Passando o Usuário Logado
    this.dispose(); // fecha a tela atual
    }//GEN-LAST:event_MenuItemProventosPagosActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VisaoInvestidorCompraAtivo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BtnComprarAtivo;
    private javax.swing.JMenuItem BtnDepositar;
    private javax.swing.JButton BtnVendaDeAtivo;
    private javax.swing.JLabel Logado;
    private javax.swing.JMenuItem MenuItemCorretagem;
    private javax.swing.JMenuItem MenuItemExtrato;
    private javax.swing.JMenuItem MenuItemProventosAgendados;
    private javax.swing.JMenuItem MenuItemProventosPagos;
    private javax.swing.JTabbedPane PainelAtivos;
    private javax.swing.JPanel PainelVerdeInferior;
    private javax.swing.JPanel PainelVerdeSuperior;
    private javax.swing.JTable TabelaDePosicaoAtivos;
    private javax.swing.JLabel VerUsuarioLogado;
    private javax.swing.JMenu btnCarteira;
    private javax.swing.JButton btnComprarAtivo;
    private javax.swing.JMenu btnProventos;
    private javax.swing.JMenu btnRelatorios;
    private javax.swing.JMenu btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabelaAtivos;
    private javax.swing.JLabel txtNumeroConta;
    private javax.swing.JLabel txtSaldo;
    // End of variables declaration//GEN-END:variables
}
