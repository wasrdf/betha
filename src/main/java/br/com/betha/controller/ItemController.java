/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.controller;

import br.com.betha.dao.ItemDAO;
import br.com.betha.model.Item;
import br.com.betha.util.Mensagem;
import br.com.br.betha.ClausulaSQL.ClausulaWhere;
import br.com.br.betha.ClausulaSQL.GeneroCondicaoWhere;
import br.com.br.betha.ClausulaSQL.OperacaoCondicaoWhere;
import br.com.br.betha.ClausulaSQL.TipoCondicaoWhere;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Was
 */
@ManagedBean
@ViewScoped
public class ItemController {
    private Item item;
    private List<Item> items;
    private String search = "";
    private int screem = 0;   
    
    public ItemController() {
        this.item = new Item();
        this.items = new ArrayList<>();
    }

    public void newItem(int pScreem) {
        item = new Item();
        screem = pScreem;             
    }
    
    public void selectItem(Item pItem) {
        item = pItem;
        screem = 1;
    }
    
    public void save(int pScreem) { 
        ItemDAO itemDAO = new ItemDAO();       
        if(item.getIdItem() == 0) {
            item = itemDAO.insertItem(item);
            if(item.getIdItem() != 0) {
                Mensagem.saveMessage("Produto cadastrado com sucesso.", "Sucesso");
                item = new Item();   
                screem = pScreem;
            }
            else {
                Mensagem.saveMessage("Ocorreu um erro inesperado ao tentar cadastrar este produto.", "");
                return;
            }            
        } else { 
            item = itemDAO.updateItem(item);
            Mensagem.saveMessage("Produto alterado com sucesso!", "Sucesso");
        }
        listItem();
    }
    
    public void changeScreem(int pScreem) {
        screem = pScreem;
    }
    
    public void deleteItem() {
        ItemDAO itemDAO = new ItemDAO();
        if(itemDAO.deletarItem(item)) {
            Mensagem.saveMessage("Produto deletado com sucesso!", "");
            screem = 0;
            listItem();
            item = new Item();
        } else {
            Mensagem.saveMessage("Não é possivél deletar este produto pois existem pedidos vinculados a ele.", "");
        }
    }
    
    public void listItem() {
        ItemDAO itemDAO = new ItemDAO();
        ClausulaWhere condition = new ClausulaWhere();
        condition.AdicionarCondicao(OperacaoCondicaoWhere.vazio, "TB_item.name", GeneroCondicaoWhere.contem, search, TipoCondicaoWhere.Texto);
        items = itemDAO.listItems(condition);        
    }
    
    
    //get set
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getScreem() {
        return screem;
    }

    public void setScreem(int screem) {
        this.screem = screem;
    }
    
}
