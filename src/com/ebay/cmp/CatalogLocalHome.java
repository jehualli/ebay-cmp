package com.ebay.cmp;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface CatalogLocalHome extends EJBLocalHome {
    CatalogLocal create() throws CreateException;
}
