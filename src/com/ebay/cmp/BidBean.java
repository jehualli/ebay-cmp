package com.ebay.cmp;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

/** The many side of the Item-Bids relationship. */
public abstract class BidBean implements EntityBean {

    private EntityContext ctx;

    public abstract Long getId();
    public abstract void setId(Long id);

    public abstract String getBidder();
    public abstract void setBidder(String b);

    public abstract Long getPriceCents();
    public abstract void setPriceCents(Long c);

    public abstract Long getPlacedAt();
    public abstract void setPlacedAt(Long t);

    public abstract ItemLocal getItem();
    public abstract void setItem(ItemLocal item);

    public Long ejbCreate(Long id, String bidder, Long priceCents, Long placedAt)
            throws CreateException {
        setId(id);
        setBidder(bidder);
        setPriceCents(priceCents);
        setPlacedAt(placedAt);
        return null;
    }

    public void ejbPostCreate(Long id, String bidder, Long priceCents, Long placedAt) { }

    public void setEntityContext(EntityContext c) { this.ctx = c; }
    public void unsetEntityContext() { this.ctx = null; }
    public void ejbActivate() { }
    public void ejbPassivate() { }
    public void ejbLoad() { }
    public void ejbStore() { }
    public void ejbRemove() { }
}
