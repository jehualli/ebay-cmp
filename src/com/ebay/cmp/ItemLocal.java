package com.ebay.cmp;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/** Local component interface of the listing entity. */
public interface ItemLocal extends EJBLocalObject {

    Long getId();

    String getTitle();
    void setTitle(String t);

    String getSeller();
    void setSeller(String s);

    Integer getCategoryId();
    void setCategoryId(Integer c);

    Long getCurrentCents();
    void setCurrentCents(Long c);

    Integer getBidCount();
    void setBidCount(Integer n);

    /** Container-managed relationship: the bids on this listing. */
    Collection getBids();
    void setBids(Collection bids);
}
