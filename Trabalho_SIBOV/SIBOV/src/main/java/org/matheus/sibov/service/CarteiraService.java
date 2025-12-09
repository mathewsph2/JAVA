/* ---------------------------------------------------------------------------- */
// CONTÊM REGRAS DE NEGÓCIO SOBRE A CARTEIRA DE ATIVOS 
//
/* ---------------------------------------------------------------------------- */


package org.matheus.sibov.service;

import org.matheus.sibov.dao.AtivoDAO;
import org.matheus.sibov.dao.CarteiraAtivosDAO;
import org.matheus.sibov.model.Ativo;
import org.matheus.sibov.model.CarteiraAtivo;

import java.math.BigDecimal;

public class CarteiraService {

    private CarteiraAtivosDAO carteiraDAO = new CarteiraAtivosDAO();
    private AtivoDAO ativoDAO = new AtivoDAO();

    /**
     * Versão simplificada — usada pelo botão.
     * Busca automaticamente o preço da cota do ativo.
     */
    public boolean comprarAtivo(int idUsuario, int idAtivo, int quantidade) {
        try {
            Ativo ativo = ativoDAO.buscarPorId(idAtivo);

            if (ativo == null) {
                System.err.println("Ativo não encontrado: " + idAtivo);
                return false;
            }

            BigDecimal precoCota = ativo.getValorCota();
            if (precoCota == null || precoCota.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Preço de cota inválido!");
                return false;
            }

            // chama a versão completa
            comprarAtivo(idUsuario, idAtivo, quantidade, precoCota);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Versão completa — usada internamente.
     * Recebe preço da cota diretamente.
     */
    public void comprarAtivo(int idUsuario, int idAtivo, int quantidade, BigDecimal precoCota) {

        BigDecimal valorCompra = precoCota.multiply(new BigDecimal(quantidade));

        CarteiraAtivo atual = carteiraDAO.buscar(idUsuario, idAtivo);

        if (atual == null) {
            // Primeira compra do ativo
            CarteiraAtivo novo = new CarteiraAtivo(
                    idUsuario,
                    idAtivo,
                    quantidade,
                    precoCota,
                    valorCompra,
                    precoCota
            );
            carteiraDAO.inserir(novo);

        } else {
            // Compra adicional
            int novaQtd = atual.getQuantidadeTotal() + quantidade;
            BigDecimal novoValInvestido = atual.getValorInvestido().add(valorCompra);
            BigDecimal novoPrecoMedio = novoValInvestido.divide(
                    new BigDecimal(novaQtd), 2, BigDecimal.ROUND_HALF_UP
            );

            atual.setQuantidadeTotal(novaQtd);
            atual.setValorInvestido(novoValInvestido);
            atual.setPrecoMedio(novoPrecoMedio);
            atual.setUltimoValorCota(precoCota);

            carteiraDAO.atualizar(atual);
        }
    }
}
