package com.ebay.cmp;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import java.util.Collection;

/**
 * CMP 2.0 entity bean.
 *
 * The class is abstract and so is every accessor: the container generates a
 * concrete subclass at deployment time that implements them against a table it
 * also generates. Nothing in this file mentions SQL, a column, a connection or
 * a transaction.
 *
 * What it costs is visible too: an abstract class per table, a pair of
 * interfaces per table, an XML block per table, and no way to run a query that
 * the descriptor did not anticipate.
 */
public abstract class ItemBean implements EntityBean {

    private EntityContext ctx;

    // ---- CMP fields: declared here, implemented by the container ----------

    public abstract Long getId();
    public abstract void setId(Long id);

    public abstract String getTitle();
    public abstract void setTitle(String t);

    public abstract String getSeller();
    public abstract void setSeller(String s);

    public abstract Integer getCategoryId();
    public abstract void setCategoryId(Integer c);

    public abstract Long getCurrentCents();
    public abstract void setCurrentCents(Long c);

    public abstract Integer getBidCount();
    public abstract void setBidCount(Integer n);

    // ---- CMR field: the relationship, also container-managed --------------

    public abstract Collection getBids();
    public abstract void setBids(Collection bids);

    // ---- lifecycle --------------------------------------------------------

    public Long ejbCreate(Long id, String title, String seller,
                          Integer categoryId, Long currentCents)
            throws CreateException {
        setId(id);
        setTitle(title);
        setSeller(seller);
        setCategoryId(categoryId);
        setCurrentCents(currentCents);
        setBidCount(new Integer(0));
        return null;              // CMP: the container returns the key
    }

    public void ejbPostCreate(Long id, String title, String seller,
                              Integer categoryId, Long currentCents) { }

    public void setEntityContext(EntityContext c) { this.ctx = c; }
    public void unsetEntityContext() { this.ctx = null; }
    public void ejbActivate() { }
    public void ejbPassivate() { }
    public void ejbLoad() { }      // container has just read the row
    public void ejbStore() { }     // container is about to write it back
    public void ejbRemove() { }
}
