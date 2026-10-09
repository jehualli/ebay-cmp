package com.ebay.cmp;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * The home is where EJB 2.0 put queries. Every finder below is implemented by
 * the container from the EJB-QL written in ejb-jar.xml -- there is no SQL and
 * no query code anywhere in this project.
 */
public interface ItemLocalHome extends EJBLocalHome {

    ItemLocal create(Long id, String title, String seller,
                     Integer categoryId, Long currentCents) throws CreateException;

    ItemLocal findByPrimaryKey(Long pk) throws FinderException;

    Collection findAll() throws FinderException;

    Collection findByCategory(Integer categoryId) throws FinderException;

    Collection findDearerThan(Long cents) throws FinderException;

    Collection findByTitleLike(String pattern) throws FinderException;

    Collection findBySeller(String seller) throws FinderException;
}
