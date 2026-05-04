package br.edu.ifg.estoque.controller;

import br.edu.ifg.estoque.dao.CompraDAO;
import br.edu.ifg.estoque.dao.ProdutoDAO;
import br.edu.ifg.estoque.model.Compra;
import br.edu.ifg.estoque.model.ItemCompra;
import br.edu.ifg.estoque.model.Produto;

import java.util.List;

public class CompraController {

    private final CompraDAO   compraDAO  = new CompraDAO();
    private final ProdutoDAO  produtoDAO = new ProdutoDAO();

    public boolean salvar(Compra compra) {

        // valida itens antes de persistir
        for (ItemCompra item : compra.getItens()) {//para cada item de compra na lista de itens da compra, verifique:
            if (item.getQuantidade() <= 0) {// se a quantidade do item for menor ou igual a zero
                System.err.println("Quantidade inválida para o produto id="// informe que a quantidade é inválida para o produto de id correspondente
                        + item.getProduto().getId());
                return false;//compra nao é salva
            }
            if (item.getValorUnit() <= 0) {// se o valor unitário do item for menor ou igual a zero
                System.err.println("Valor unitário inválido para o produto id="// informe que o valor unitário é inválido para o produto
                        + item.getProduto().getId());
                return false;//compra nao é salva
            }
        }

        compra.calcularTotal();

        // Persiste a compra no banco
        boolean salvo = compraDAO.salvar(compra);
        if (!salvo) return false;

        // atualiza estoque, ultima compra e preço médio
        for (ItemCompra item : compra.getItens()) {//para cada item de compra na lista de itens da compra, faça:
            Produto prod = produtoDAO.pesquisar(item.getProduto().getId());//busque através do id o produto correspondente ao item de compra no banco
            if (prod == null) {//caso o produto não seja encontrado no banco de dados
                System.err.println("Produto id=" + item.getProduto().getId() + " não encontrado.");// o produto correspondente não foi encontrado
                continue;// pula para o próximo item de compra
            }

            // acrescenta quantidade ao estoque
            prod.setQtdeEstoque(prod.getQtdeEstoque() + item.getQuantidade());//atualiza quantidade em estoque do produto somando a quantidade do item de compra
            System.out.println("Estoque atualizado para '"// informa que o estoque foi atualizado
                    + prod.getNome() + "': " + prod.getQtdeEstoque());// mostra o nome do produto e a nova quantidade em estoque

            // atualiza valor da última compra com o valor unitário desta compra
            prod.setValorUltimaCompra(item.getValorUnit());//atualiza o valor da última compra do produto com o valor unitário do item de compra
            System.out.println("Valor última compra atualizado: " + prod.getValorUltimaCompra());// informa novo valor da última compra atualizado 

            // recalcula a média de preços de compra consultando o banco
            double media = compraDAO.calcularPrecoMedio(prod.getId());//obtem a média de preços de compra do produto consultando o banco
            prod.setPrecoMedio(media);//atualiza o preço médio do produto com a média calculada
            System.out.println("Preço médio atualizado: " + prod.getPrecoMedio());// informa novo preço médio atualizado

            produtoDAO.atualizarEstoqueEValores(prod);//atualiza o estoque, valor da última compra e preço médio do produto no banco
        }

        return true;//compra salva com sucesso
    }

    public boolean alterar(Compra compra) {
        return compraDAO.alterar(compra);
    }

    public boolean excluir(int id) {
        return compraDAO.excluir(id);
    }

    public Compra pesquisar(int id) {
        return compraDAO.pesquisar(id);
    }

    public List<Compra> listarTodos() {
        return compraDAO.listarTodos();
    }
}
