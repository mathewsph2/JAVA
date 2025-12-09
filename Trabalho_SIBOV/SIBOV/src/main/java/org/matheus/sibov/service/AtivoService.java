/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.matheus.sibov.service;

import java.util.List;
import org.matheus.sibov.dao.AtivoDAO;
import org.matheus.sibov.model.Ativo;

public class AtivoService {

    
    private AtivoDAO ativoDAO;

    
    
    public AtivoService() {
        this.ativoDAO = new AtivoDAO();
    }

    
    
    /*---  Validação dos dados do ativo  ---------------------------------------*/
    private void validarAtivo(Ativo ativo) throws Exception {

        if (ativo.getTicker() == null || ativo.getTicker().isEmpty()) {
            throw new Exception("O ticker não pode estar vazio.");
        }
        if (ativo.getTipo() == null) {
            throw new Exception("O tipo do ativo é obrigatório.");
        }
        if (ativo.getSegmento() == null || ativo.getSegmento().isEmpty()) {
            throw new Exception("O segmento é obrigatório.");
        }

        // Observações podem ser nulas, então não é obrigatório
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    
    
     /*--- Salvar Ativo --------------------------------------------------------*/
    public void salvar(Ativo ativo) throws Exception {
        validarAtivo(ativo);
        ativoDAO.salvar(ativo);
    }
    /*--------------------------------------------------------------------------*/
    
    

    
    
    /*--- Atualizar ativo pelo ID ----------------------------------------------*/
    public void atualizar(Ativo ativo) throws Exception {
        validarAtivo(ativo);

        if (ativo.getId() <= 0) {
            throw new Exception("O ID do ativo é inválido para atualização.");
        }

        ativoDAO.atualizar(ativo);
    }
    /*--------------------------------------------------------------------------*/
    
    

    
    
    /*--- Listar todos os ativos -----------------------------------------------*/
    public List<Ativo> listarTodos() {
        return ativoDAO.listarTodos();
    }
    /*--------------------------------------------------------------------------*/
    
    

    
    
    /*--- Deletar ativo por ID--------------------------------------------------*/
    public void deletar(int id) throws Exception {

        if (id <= 0) {
            throw new Exception("ID inválido para exclusão.");
        }

        ativoDAO.deletar(id);
    }
    /*--------------------------------------------------------------------------*/
    
    
    
    /*--- Deletar ativo por Ticket ---------------------------------------------*/
    public void excluirAtivo(String ticker) {
        ativoDAO.deletarPorTicker(ticker);
    }
    /*--------------------------------------------------------------------------*/

}
