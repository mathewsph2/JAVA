-- Alterar o preço médio de uma ação por ID: 

-- Ver preço médio: Aréa do Investidor > Carteira > Aba Listar meus ativos > Coluna "Preço Médio"

UPDATE ativo
SET valor_cota = 90.50
WHERE id = 12;



--  Mudar o status de AGENDADO X PAGO 
SELECT * FROM sibov.proventos;

UPDATE proventos
SET status = 'AGENDADO'
WHERE id = 8;
