/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.betha.controller;

import br.com.betha.dao.ItemDAO;
import br.com.betha.dao.OrderDAO;
import br.com.betha.dao.OrderItemDAO;
import br.com.betha.model.Item;
import br.com.betha.model.Order;
import br.com.betha.model.OrderItem;
import br.com.betha.util.Mensagem;
import br.com.br.betha.ClausulaSQL.ClausulaWhere;
import br.com.br.betha.ClausulaSQL.GeneroCondicaoWhere;
import br.com.br.betha.ClausulaSQL.OperacaoCondicaoWhere;
import br.com.br.betha.ClausulaSQL.TipoCondicaoWhere;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Was
 */
@ManagedBean
@ViewScoped
public class OrderController {

    private Item item;
    private List<Item> items;
    private Order order;
    private List<Order> requestList;
    private OrderItem orderItem;
    private List<OrderItem> orderItemList;
    private BigDecimal total;
    private int screem = 0;
    long idOrder;
    private String search = "";

    public OrderController() {
        this.item = new Item();
        this.items = new ArrayList<>();
        this.order = new Order();
        this.requestList = new ArrayList<>();
        this.orderItem = new OrderItem();
        this.orderItemList = new ArrayList<>();
        this.total = new BigDecimal(BigInteger.ZERO);
    }

    public void listItens() {
        ItemDAO itemDAO = new ItemDAO();
        ClausulaWhere condition = new ClausulaWhere();
        condition.AdicionarCondicao(OperacaoCondicaoWhere.vazio, "TB_item.name", GeneroCondicaoWhere.contem, search, TipoCondicaoWhere.Texto);
        items = itemDAO.listItems(condition);
    }

    public void addToCart(Item pItem) {

        for (OrderItem oi : orderItemList) {
            if (pItem.getIdItem() == oi.getIdItem()) {
                ArrayList<OrderItem> list = new ArrayList<OrderItem>(orderItemList);//creating copy of array list
                list.remove(oi);
                total = total.subtract(oi.getTotalCost());
                orderItemList = list;
            }
        }

        orderItem.setIdItem(pItem.getIdItem());
        orderItem.setQuantity(1);
        orderItem.setUnitCost(pItem.getUnitValue());
        orderItem.setItemName(pItem.getName());
        orderItem.setTotalCost(pItem.getUnitValue());
        orderItemList.add(orderItem);
        orderItem = new OrderItem();
        //calcule total
        total = total.add(pItem.getUnitValue());

    }

    public void calculeTotalValue(OrderItem pOrderItem) {
        total = new BigDecimal(BigInteger.ZERO);
        pOrderItem.setTotalCost(pOrderItem.getUnitCost().multiply(BigDecimal.valueOf(pOrderItem.getQuantity())));

        //calcule total
        for (OrderItem oi : orderItemList) {
            total = total.add(oi.getTotalCost());
        }
    }

    

    public void removeItem(OrderItem pOrderItem) {
        total = total.subtract(pOrderItem.getTotalCost());
        orderItemList.remove(pOrderItem);
    }

    public void buy() {
        try {
            OrderDAO orderDAO = new OrderDAO();
            order.setDateOrder(new Date());
            order = orderDAO.insertOrder(order);
            OrderItemDAO orderItemDAO = new OrderItemDAO();
            for (OrderItem oi : orderItemList) {
                oi.setIdOrder(order.getIdOrder());
                orderItemDAO.insertOrder(oi);
            }
            changePage("/order/order_resume.jsf?IdOrder=" + order.getIdOrder());
            Mensagem.saveMessage("Compra realizada com sucesso! veja abaixo detalhes de sua compra.", "Sucesso");
        } catch (Exception e) {
            Mensagem.saveMessage("Ocorreu um erro inesperado ao executar sua compra." + e.getMessage(), "");
        }
    }

    public void changePage(String pPage) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().getExternalContext().redirect(ctx.getExternalContext().getRequestContextPath() + pPage);
        } catch (IOException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get and set
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Order> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Order> requestList) {
        this.requestList = requestList;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getScreem() {
        return screem;
    }

    public void setScreem(int screem) {
        this.screem = screem;
    }

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long idOrder) {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        OrderDAO orderDAO = new OrderDAO();
        ClausulaWhere condition = new ClausulaWhere();
        condition.AdicionarCondicao(OperacaoCondicaoWhere.vazio, "Tb_order.id_order", GeneroCondicaoWhere.igual, String.valueOf(idOrder), TipoCondicaoWhere.Numero);
        order = orderDAO.requestList(condition).get(0);

        ClausulaWhere condition2 = new ClausulaWhere();
        condition2.AdicionarCondicao(OperacaoCondicaoWhere.vazio, "Tb_order_item.id_order", GeneroCondicaoWhere.igual, String.valueOf(idOrder), TipoCondicaoWhere.Numero);
        orderItemList = orderItemDAO.listOrderItem(condition2);
        //calcule total
        for (OrderItem oi : orderItemList) {
            total = total.add(oi.getTotalCost());
        }

        this.idOrder = idOrder;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
