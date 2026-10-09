package com.ebay.web;

import com.ebay.cmp.CatalogLocal;
import com.ebay.cmp.CatalogLocalHome;
import com.ebay.cmp.ItemVO;
import com.ebay.core.AuctionHouse;
import com.ebay.model.Item;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The same catalogue, held two ways: in a HashMap the application manages, and
 * in a database the container manages through CMP entity beans.
 */
public class PersistenceDemoController extends BaseController {

    protected void handle(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            InitialContext ic = new InitialContext();
            CatalogLocalHome home =
                (CatalogLocalHome) ic.lookup("java:comp/env/ejb/Catalog");
            CatalogLocal catalog = home.create();

            int seeded = catalog.seed(30);
            req.setAttribute("seeded", new Integer(seeded));
            req.setAttribute("rowCount", new Integer(catalog.count()));

            // --- the same question, asked of each tier ---------------------
            Integer cat = new Integer(11);

            long t0 = System.nanoTime();
            List fromMemory = new ArrayList();
            AuctionHouse house = AuctionHouse.current();
            if (house != null) {
                List all = house.allItems();
                for (int i = 0; i < all.size(); i++) {
                    Item it = (Item) all.get(i);
                    if (it.getCategoryId() == cat.intValue()) fromMemory.add(it.getTitle());
                }
            }
            long memoryNanos = System.nanoTime() - t0;

            long t1 = System.nanoTime();
            List fromCmp = catalog.findByCategoryVO(cat);
            long cmpNanos = System.nanoTime() - t1;

            req.setAttribute("memoryHits", new Integer(fromMemory.size()));
            req.setAttribute("cmpHits", new Integer(fromCmp.size()));
            req.setAttribute("memoryMicros", new Long(memoryNanos / 1000));
            req.setAttribute("cmpMicros", new Long(cmpNanos / 1000));
            req.setAttribute("cmpRows", fromCmp);

            // --- other EJB-QL finders --------------------------------------
            req.setAttribute("dearer", catalog.findDearerThanVO(new Long(10000)));
            req.setAttribute("titleLike", catalog.findByTitleLikeVO("%Beanie%"));

            // --- dirty tracking --------------------------------------------
            List any = catalog.findAllVO();
            if (!any.isEmpty()) {
                ItemVO first = (ItemVO) any.get(0);
                req.setAttribute("dirty", catalog.raisePrice(first.getId(), new Long(100)));
            }

            // --- the n+1 walk ----------------------------------------------
            int[] walk = catalog.traverseRelationship();
            req.setAttribute("walkItems", new Integer(walk[0]));
            req.setAttribute("walkBids", new Integer(walk[1]));
            req.setAttribute("walkTrips", new Integer(walk[2]));

            req.setAttribute("generated", catalog.generatedClasses());

        } catch (Exception e) {
            req.setAttribute("cmpError", e.toString());
        }
        forward(req, res, "persistencia.jsp");
    }
}
