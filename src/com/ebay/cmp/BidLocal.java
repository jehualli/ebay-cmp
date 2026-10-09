package com.ebay.cmp;

import javax.ejb.EJBLocalObject;

public interface BidLocal extends EJBLocalObject {
    Long getId();
    String getBidder();
    void setBidder(String b);
    Long getPriceCents();
    void setPriceCents(Long c);
    Long getPlacedAt();
    void setPlacedAt(Long t);

    /** The other end of the relationship. */
    ItemLocal getItem();
    void setItem(ItemLocal item);
}
