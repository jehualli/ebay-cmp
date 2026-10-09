package com.ebay.cmp;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface BidLocalHome extends EJBLocalHome {
    BidLocal create(Long id, String bidder, Long priceCents, Long placedAt)
            throws CreateException;
    BidLocal findByPrimaryKey(Long pk) throws FinderException;
    Collection findAll() throws FinderException;
    Collection findByBidder(String bidder) throws FinderException;
}
