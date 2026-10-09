package com.ebay.cmp;

import com.ebay.core.AuctionHouse;
import com.ebay.model.Bid;
import com.ebay.model.Item;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CatalogBean implements SessionBean {

    private SessionContext ctx;

    public void ejbCreate() { }
    public void setSessionContext(SessionContext c) { this.ctx = c; }
    public void ejbRemove() { }
    public void ejbActivate() { }
    public void ejbPassivate() { }

    private ItemLocalHome itemHome() {
        try {
            return (ItemLocalHome) new InitialContext()
                .lookup("java:comp/env/ejb/ItemEntity");
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    private BidLocalHome bidHome() {
        try {
            return (BidLocalHome) new InitialContext()
                .lookup("java:comp/env/ejb/BidEntity");
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    // ------------------------------------------------------------- seeding

    public int seed(int howMany) {
        try {
            ItemLocalHome ih = itemHome();
            BidLocalHome bh = bidHome();
            if (!ih.findAll().isEmpty()) return 0;

            AuctionHouse house = AuctionHouse.current();
            if (house == null) return 0;

            List items = house.allItems();
            long bidId = 1;
            int made = 0;

            for (int i = 0; i < items.size() && made < howMany; i++) {
                Item it = (Item) items.get(i);
                ItemLocal row = ih.create(new Long(it.getId()), it.getTitle(),
                                          it.getSeller(),
                                          new Integer(it.getCategoryId()),
                                          new Long(it.getCurrentCents()));
                List bids = it.getBids();
                int n = 0;
                for (int b = 0; b < bids.size() && n < 5; b++, n++) {
                    Bid bid = (Bid) bids.get(b);
                    BidLocal br = bh.create(new Long(bidId++), bid.getBidder(),
                                            new Long(bid.getPriceCents()),
                                            new Long(bid.getTime()));
                    // Setting one side of a CMR field updates both ends.
                    br.setItem(row);
                }
                row.setBidCount(new Integer(n));
                made++;
            }
            return made;
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    public int count() {
        try {
            return itemHome().findAll().size();
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    // ------------------------------------------------------------- queries

    public List findAllVO() {
        try { return toVO(itemHome().findAll()); }
        catch (Exception e) { throw new EJBException(e); }
    }

    public List findByCategoryVO(Integer categoryId) {
        try { return toVO(itemHome().findByCategory(categoryId)); }
        catch (Exception e) { throw new EJBException(e); }
    }

    public List findByTitleLikeVO(String pattern) {
        try { return toVO(itemHome().findByTitleLike(pattern)); }
        catch (Exception e) { throw new EJBException(e); }
    }

    public List findDearerThanVO(Long cents) {
        try { return toVO(itemHome().findDearerThan(cents)); }
        catch (Exception e) { throw new EJBException(e); }
    }

    private List toVO(Collection rows) {
        List out = new ArrayList();
        for (Iterator i = rows.iterator(); i.hasNext();) {
            ItemLocal r = (ItemLocal) i.next();
            out.add(new ItemVO(r.getId(), r.getTitle(), r.getSeller(),
                               r.getCategoryId(), r.getCurrentCents(),
                               r.getBidCount()));
        }
        return out;
    }

    // ------------------------------------------------------ dirty tracking

    public String raisePrice(Long itemId, Long byCents) {
        try {
            ItemLocal row = itemHome().findByPrimaryKey(itemId);
            long before = row.getCurrentCents().longValue();
            row.setCurrentCents(new Long(before + byCents.longValue()));
            // No save, no flush, no update statement. The container writes the
            // row back when this transaction commits, on the way out of the
            // facade -- that is what ejbStore() exists to bracket.
            return row.getTitle() + ": " + before + " -> " + row.getCurrentCents();
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    // --------------------------------------------------------- the n+1 walk

    /**
     * One finder, then one relationship traversal per row. Returns
     * {items, bids, roundTrips}. roundTrips is items + 1: the finder itself,
     * plus a visit per item. This shape is the whole reason people left CMP.
     */
    public int[] traverseRelationship() {
        try {
            Collection all = itemHome().findAll();
            int items = 0, bids = 0;
            for (Iterator i = all.iterator(); i.hasNext();) {
                ItemLocal row = (ItemLocal) i.next();
                bids += row.getBids().size();   // a visit to the database
                items++;
            }
            return new int[] { items, bids, items + 1 };
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    public String generatedClasses() {
        try {
            ItemLocalHome ih = itemHome();
            ItemLocal one = null;
            Collection all = ih.findAll();
            if (!all.isEmpty()) one = (ItemLocal) all.iterator().next();
            return "home  = " + ih.getClass().getName()
                 + " | entity = " + (one == null ? "n/a" : one.getClass().getName())
                 + " | bean class you wrote = " + ItemBean.class.getName()
                 + " (abstract: " + java.lang.reflect.Modifier
                       .isAbstract(ItemBean.class.getModifiers()) + ")";
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }
}
