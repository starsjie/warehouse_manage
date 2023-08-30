package com.pn.service;

import com.pn.entity.Purchase;
import com.pn.entity.Result;
import com.pn.page.Page;

/**
 * @author ljj
 */
public interface PurchaseService {
    //添加采购单的业务方法
    public Result savePurchase(Purchase purchase);

    //分页查询采购单的业务方法
    public Page queryPurchasePage(Page page, Purchase purchase);

    //删除采购单的业务方法
    public Result deletePurchase(Integer buyId);

    //修改采购单的业务方法
    public Result updatePurchase(Purchase purchase);
}
