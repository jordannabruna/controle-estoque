package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.ClienteDAO;
import br.edu.ifg.estoque.dao.ProdutoDAO;
import br.edu.ifg.estoque.dao.VendaDAO;
import br.edu.ifg.estoque.model.ItemVenda;
import br.edu.ifg.estoque.model.Produto;
import br.edu.ifg.estoque.model.Venda;

import java.util.List;

public class VendaController {

    private final VendaDAO    vendaDAO   = new VendaDAO();
    private final ProdutoDAO  produtoDAO = new ProdutoDAO();
    private final ClienteDAO  clienteDAO = new ClienteDAO();

    public boolean salvar(Venda venda) {

        // verifica limite de vendas por CPF no mês
        String cpf = venda.getCliente().getCpf();// obtem cpf do cliente
        int mes = venda.getDataVenda().getMonthValue();// obtem mês da venda
        int ano = venda.getDataVenda().getYear();// obtem ano da venda
        int vendasNoPeriodo = clienteDAO.contarVendasNoPeriodo(cpf, mes, ano);// consulta no banco quantas vendas o cliente já fez no mesmo mês e ano

        if (vendasNoPeriodo >= 3) {// se o cliente já tiver feito 3 compras no mês, sistema bloqueia a venda
            System.err.println("RNF004 - Venda bloqueada: cliente CPF " + cpf// informa que o cliente deste cpf já atingiu o limite de vendas mensal
                    + " já possui " + vendasNoPeriodo + " vendas em " + mes + "/" + ano
                    + ". Limite de 3 por mês atingido.");
            return false;// bloqueia venda
        }

        // verifica estoque de cada produto antes de prosseguir
        for (ItemVenda item : venda.getItens()) {// para cada item da venda
            Produto prodAtual = produtoDAO.pesquisar(item.getProduto().getId());// consulta o produto no banco para obter informações de estoque
            if (prodAtual == null) {//se não enontrar no banco
                System.err.println("Produto id=" + item.getProduto().getId() + " não encontrado.");//emnsagem de erro
                return false;// bloqueia a venda
            }
            if (prodAtual.getQtdeEstoque() < 1) {// se o estoque for menor que 1
                System.err.println("RNF003 - Venda bloqueada: produto '"//não é possível vender um produto sem estoque
                        + prodAtual.getNome() + "' sem estoque disponível.");// exibe nome do produto indisponível
                return false;// bloqueia a venda
            }
            if (prodAtual.getQtdeEstoque() < item.getQuantidade()) {// caso o estoque seja menor que a quantidade solicitada
                System.err.println("RNF003 - Venda bloqueada: estoque insuficiente para '"// não é possível vender mais do que o disponível
                        + prodAtual.getNome() + "'. Disponível: " + prodAtual.getQtdeEstoque()//mostra quantos itens estão disponíveis
                        + ", solicitado: " + item.getQuantidade());//mostra quantos itens foram solicitados
                return false;// bloqueia a venda
            }
        }

        // Calcula o total da venda
        venda.calcularTotal();//chama método da classe Venda para calcular o valor total com base nos itens, quantidades e valores unitarios

        boolean salvo = vendaDAO.salvar(venda);//chama metodo da classe VendaDAO para salvar a venda no banco
        if (!salvo) return false;// se nao conseguiu salvar a venda, retorna false

        // atualiza estoque e valor_ultima_venda para cada produto
        for (ItemVenda item : venda.getItens()) {// para cada item da venda
            Produto prod = produtoDAO.pesquisar(item.getProduto().getId());// consulta o produto no banco para obter informacao de estoque e valores

            // subtrai quantidade do estoque
            prod.setQtdeEstoque(prod.getQtdeEstoque() - item.getQuantidade());// atualiza a quantidade em estoque subtraindo a quantidade vendida

            // atualiza valor da última venda com o valor unitário da venda
            prod.setValorUltimaVenda(item.getValorUnit());

            produtoDAO.atualizarEstoqueEValores(prod);// atualiza o produto no banco com novas informacoes de estoque e valor da última venda
            System.out.println("RNF001 - Estoque atualizado para '"//informa que o estoque do produto foi atualizado
                    + prod.getNome() + "': " + prod.getQtdeEstoque());// mostra o nome do produto e a nova quantidade em estoque atualizada
            System.out.println("RNF005 - Valor última venda atualizado: " + prod.getValorUltimaVenda());//
        }

        return true;// se atendeu todas as regras e conseguiu salvar a venda, retorna true
    }

    public boolean alterar(Venda venda) {//metodo para alterar uma venda já existente no banco que recebe um objeto Venda por parametro
        return vendaDAO.alterar(venda);
    }

    public boolean excluir(int id) {//metodo que exclui uma venda do banco, recebe o id da venda a ser excluída
        return vendaDAO.excluir(id);
    }

    public Venda pesquisar(int id) {//metodo para pesquisar uma venda no banco, recebe o id da venda a ser pesquisada
    // retorna o objeto em questão
        return vendaDAO.pesquisar(id);
    }

    public List<Venda> listarTodos() {//metodo para listar todas as vendas no banco
        return vendaDAO.listarTodos();// retorna uma lista de objetos Venda
    }
}
