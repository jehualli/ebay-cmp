package com.ebay.cmp;

import javax.ejb.EJBLocalObject;
import java.util.List;

/**
 * Session Facade (Core J2EE Patterns, 2001).
 *
 * The web tier is not allowed to touch entity beans: they are local, every
 * accessor is an intercepted call, and each one would need its own
 * transaction. So a session bean sits in front, runs the whole use case inside
 * one container transaction, and returns flat value objects.
 */
public interface CatalogLocal extends EJBLocalObject {

    int seed(int howMany);
    int count();

    List findAllVO();
    List findByCategoryVO(Integer categoryId);
    List findByTitleLikeVO(String pattern);
    List findDearerThanVO(Long cents);

    /** Mutates a persistent field. There is no save() to call. */
    String raisePrice(Long itemId, Long byCents);

    /** Walks the container-managed relationship; reports the round trips. */
    int[] traverseRelationship();

    String generatedClasses();
}
