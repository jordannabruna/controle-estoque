package br.edu.ifg.estoque;

import br.edu.ifg.estoque.controller.*;
import br.edu.ifg.estoque.model.*;

import java.time.LocalDate;

public class Main {

    static CategoriaController  categoriaCtrl  = new CategoriaController();
    static ProdutoController    produtoCtrl    = new ProdutoController();
    static ClienteController    clienteCtrl    = new ClienteController();
    static FornecedorController fornecedorCtrl = new FornecedorController();
    static VendaController      vendaCtrl      = new VendaController();
    static CompraController     compraCtrl     = new CompraController();

    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE CONTROLE DE ESTOQUE ===\n");

        // 1. CATEGORIAS
        System.out.println("--- Cadastrando categorias ---");
        Categoria catEletronicos = new Categoria("Eletrônicos");
        Categoria catAlimentos   = new Categoria("Alimentos");
        categoriaCtrl.salvar(catEletronicos);
        categoriaCtrl.salvar(catAlimentos);

        System.out.println("\n--- Listando categorias ---");
        categoriaCtrl.listarTodos().forEach(System.out::println);

        // 2. PRODUTOS
        
        System.out.println("\n--- Cadastrando produtos ---");
        Produto notebook = new Produto("Notebook Dell", catEletronicos);
        Produto arroz    = new Produto("Arroz 5kg", catAlimentos);
        produtoCtrl.salvar(notebook);
        produtoCtrl.salvar(arroz);

        System.out.println("\n--- Listando produtos ---");
        produtoCtrl.listarTodos().forEach(System.out::println);

        // 3. CLIENTES
        
        System.out.println("\n--- Cadastrando clientes ---");
        Cliente joao  = new Cliente("João Silva",  "111.111.111-11", "1234567", "Rua A, 10", "64999990001");
        Cliente maria = new Cliente("Maria Souza", "222.222.222-22", "7654321", "Rua B, 20", "64999990002");
        clienteCtrl.salvar(joao);
        clienteCtrl.salvar(maria);

        System.out.println("\n--- Listando clientes ---");
        clienteCtrl.listarTodos().forEach(System.out::println);

        // 4. FORNECEDORES
        
        System.out.println("\n--- Cadastrando fornecedores ---");
        Fornecedor techDistrib = new Fornecedor("Tech Distribuidora", "Tech Distribuidora Ltda", "11.111.111/0001-11");
        Fornecedor alimentosSA = new Fornecedor("Alimentos SA",       "Alimentos SA Comércio",  "22.222.222/0001-22");
        fornecedorCtrl.salvar(techDistrib);
        fornecedorCtrl.salvar(alimentosSA);

        System.out.println("\n--- Listando fornecedores ---");
        fornecedorCtrl.listarTodos().forEach(System.out::println);

        // 5. COMPRA
        
        System.out.println("\n--- Realizando compra 1 (notebook e arroz) ---");
        Compra compra1 = new Compra(LocalDate.now(), techDistrib);
        compra1.adicionarItem(new ItemCompra(notebook, 10, 2500.00));
        compra1.adicionarItem(new ItemCompra(arroz,    50,   18.00));
        compraCtrl.salvar(compra1);

        System.out.println("\n--- Realizando compra 2 do notebook (para testar média RNF007) ---");
        Compra compra2 = new Compra(LocalDate.now(), techDistrib);
        compra2.adicionarItem(new ItemCompra(notebook, 5, 2300.00));
        compraCtrl.salvar(compra2);

        System.out.println("\n--- Estoque dos produtos após compras ---");
        produtoCtrl.listarTodos().forEach(System.out::println);

        // 6. VENDA
        
        System.out.println("\n--- Realizando venda 1 para João ---");
        Venda venda1 = new Venda(LocalDate.now(), joao);
        venda1.adicionarItem(new ItemVenda(notebook, 1, 3200.00));
        venda1.adicionarItem(new ItemVenda(arroz,    2,   25.00));
        vendaCtrl.salvar(venda1);

        System.out.println("\n--- Realizando venda 2 para João ---");
        Venda venda2 = new Venda(LocalDate.now(), joao);
        venda2.adicionarItem(new ItemVenda(arroz, 3, 25.00));
        vendaCtrl.salvar(venda2);

        System.out.println("\n--- Realizando venda 3 para João ---");
        Venda venda3 = new Venda(LocalDate.now(), joao);
        venda3.adicionarItem(new ItemVenda(arroz, 2, 25.00));
        vendaCtrl.salvar(venda3);

        System.out.println("\n--- Tentativa de venda 4 para João (deve ser BLOQUEADA) ---");
        Venda venda4 = new Venda(LocalDate.now(), joao);
        venda4.adicionarItem(new ItemVenda(arroz, 1, 25.00));
        vendaCtrl.salvar(venda4);

        System.out.println("\n--- Tentativa de venda com estoque zerado (deve ser BLOQUEADA - RNF003) ---");
        // Forçamos produto sem estoque criando um produto novo sem compra
        Produto semEstoque = new Produto("Produto Sem Estoque", catEletronicos);
        produtoCtrl.salvar(semEstoque);
        Venda vendaBloqueada = new Venda(LocalDate.now(), maria);
        vendaBloqueada.adicionarItem(new ItemVenda(semEstoque, 1, 100.00));
        vendaCtrl.salvar(vendaBloqueada);

        System.out.println("\n--- Estoque dos produtos após vendas ---");
        produtoCtrl.listarTodos().forEach(System.out::println);

        // 7. CONSULTAS E LISTAGENS
        
        System.out.println("\n--- Listando todas as vendas ---");
        vendaCtrl.listarTodos().forEach(System.out::println);

        System.out.println("\n--- Listando todas as compras ---");
        compraCtrl.listarTodos().forEach(System.out::println);

        System.out.println("\n--- Pesquisando venda id=1 (com itens) ---");
        Venda vendaConsultada = vendaCtrl.pesquisar(1);
        if (vendaConsultada != null) {
            System.out.println(vendaConsultada);
            vendaConsultada.getItens().forEach(i -> System.out.println("  " + i));
        }

        System.out.println("\n--- Pesquisando compra id=1 (com itens) ---");
        Compra compraConsultada = compraCtrl.pesquisar(1);
        if (compraConsultada != null) {
            System.out.println(compraConsultada);
            compraConsultada.getItens().forEach(i -> System.out.println("  " + i));
        }

        // 8. ALTERAÇÃO E EXCLUSÃO
        
        System.out.println("\n--- Alterando categoria ---");
        catAlimentos.setNome("Alimentos e Bebidas");
        categoriaCtrl.alterar(catAlimentos);
        System.out.println(categoriaCtrl.pesquisar(catAlimentos.getId()));

        System.out.println("\n--- Excluindo fornecedor de teste ---");
        Fornecedor fornecedorTemp = new Fornecedor("Temp", "Temp Ltda", "99.999.999/0001-99");
        fornecedorCtrl.salvar(fornecedorTemp);
        fornecedorCtrl.excluir(fornecedorTemp.getId());

        System.out.println("\n=== TESTES CONCLUÍDOS ===");
    }
}
