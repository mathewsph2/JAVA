/* ---------------------------------------------------------------------------- */
// CONTÊM REGRAS DE NEGÓCIO SOBRE A CARTEIRA DE SALDO DO CLIENTE 
//
/* ---------------------------------------------------------------------------- */

package org.matheus.sibov.service;

import org.matheus.sibov.dao.CarteiraAtivosDAO;
import org.matheus.sibov.dao.CarteiraDAO;
import org.matheus.sibov.dao.AtivoDAO;
import org.matheus.sibov.model.Ativo;
import org.matheus.sibov.model.CarteiraAtivo;

import java.math.BigDecimal;

public class CarteiraServiceSaldo {

    private CarteiraDAO carteiraDAO = new CarteiraDAO();
    private CarteiraAtivosDAO carteiraAtivosDAO = new CarteiraAtivosDAO();
    private AtivoDAO ativoDAO = new AtivoDAO();

    /**
     * Método público usado pela UI: tenta comprar o ativo verificando saldo,
     * debitando e registrando a compra. Retorna true se tudo ok.
     */
    public boolean comprarAtivo(int idUsuario, int idAtivo, int quantidade) {
        try {
            Ativo ativo = ativoDAO.buscarPorId(idAtivo);
            if (ativo == null) {
                System.err.println("Ativo não encontrado: " + idAtivo);
                return false;
            }

            BigDecimal precoUnit = ativo.getValorCota();
            if (precoUnit == null || precoUnit.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Preço de cota inválido para o ativo: " + idAtivo);
                return false;
            }

            BigDecimal totalCompra = precoUnit.multiply(new BigDecimal(quantidade));

            // 1) Bloquear compra: debitar saldo (retorna false se insuficiente)
            boolean debitoOk = debitarCompra(idUsuario, totalCompra);
            if (!debitoOk) {
                System.err.println("Saldo insuficiente para o usuário: " + idUsuario);
                return false;
            }

            // 2) Registrar compra na carteira_ativos
            boolean registroOk = registrarCompraNaCarteira(idUsuario, idAtivo, quantidade, precoUnit);
            if (!registroOk) {
                // se falhar ao registrar, devolver o dinheiro
                creditarVenda(idUsuario, totalCompra);
                System.err.println("Falha ao registrar compra — estornando saldo.");
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Versão que já recebe o preço unitário (útil em testes ou chamadas internas).
     */
    public boolean comprarAtivo(int idUsuario, int idAtivo, int quantidade, BigDecimal precoUnit) {
        BigDecimal totalCompra = precoUnit.multiply(new BigDecimal(quantidade));

        boolean debitoOk = debitarCompra(idUsuario, totalCompra);
        if (!debitoOk) return false;

        boolean registroOk = registrarCompraNaCarteira(idUsuario, idAtivo, quantidade, precoUnit);
        if (!registroOk) {
            creditarVenda(idUsuario, totalCompra);
            return false;
        }

        return true;
    }

    /**
     * Debita o saldo da carteira (bloqueia a compra). Retorna true se conseguiu debitar.
     */
    
    public boolean debitarCompra(int idInvestidor, BigDecimal valorTotal) {
        // buscar carteira
        var carteira = carteiraDAO.buscarPorInvestidor(idInvestidor);
        if (carteira == null) {
            System.err.println("Carteira não encontrada para investidor: " + idInvestidor);
            return false;
        }

        if (carteira.getSaldo().compareTo(valorTotal) < 0) {
            return false; // saldo insuficiente
        }

        var novoSaldo = carteira.getSaldo().subtract(valorTotal);
        return carteiraDAO.atualizarSaldo(idInvestidor, novoSaldo);
    }

    /**
     * Credita (adiciona) o valor ao saldo da carteira.
     */
    public boolean creditarVenda(int idInvestidor, BigDecimal valorTotal) {
        var carteira = carteiraDAO.buscarPorInvestidor(idInvestidor);
        if (carteira == null) {
            System.err.println("Carteira não encontrada para investidor: " + idInvestidor);
            return false;
        }

        var novoSaldo = carteira.getSaldo().add(valorTotal);
        return carteiraDAO.atualizarSaldo(idInvestidor, novoSaldo);
    }

    /**
     * Realiza o insert ou update em carteira_ativos usando CarteiraAtivosDAO.
     * Retorna true se operação OK.
     */
    private boolean registrarCompraNaCarteira(int idUsuario, int idAtivo, int quantidade, BigDecimal precoUnit) {
        try {
            // busca registro na carteira_ativos
            CarteiraAtivo atual = carteiraAtivosDAO.buscar(idUsuario, idAtivo);

            BigDecimal valorCompra = precoUnit.multiply(new BigDecimal(quantidade));

            if (atual == null) {
                // inserir novo registro
                CarteiraAtivo novo = new CarteiraAtivo(
                        idUsuario,
                        idAtivo,
                        quantidade,
                        precoUnit,
                        valorCompra,
                        precoUnit
                );
                carteiraAtivosDAO.inserir(novo);
            } else {
                // atualizar registro existente (compra adicional)
                int novaQtd = atual.getQuantidadeTotal() + quantidade;
                BigDecimal novoValInvestido = atual.getValorInvestido().add(valorCompra);
                BigDecimal novoPrecoMedio = novoValInvestido.divide(
                        new BigDecimal(novaQtd), 2, BigDecimal.ROUND_HALF_UP
                );

                atual.setQuantidadeTotal(novaQtd);
                atual.setValorInvestido(novoValInvestido);
                atual.setPrecoMedio(novoPrecoMedio);
                atual.setUltimoValorCota(precoUnit);

                carteiraAtivosDAO.atualizar(atual);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Erro ao registrar compra na carteira: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
