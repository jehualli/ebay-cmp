package com.ebay.cmp;

import java.io.Serializable;

/**
 * Value Object (Core J2EE Patterns, 2001).
 *
 * Entity beans are local and every accessor is a container-intercepted call,
 * so handing one to the web tier means a round trip per getter. The period
 * answer was to copy the state into a flat serializable object at the tier
 * boundary and pass that instead. Later generations call this a DTO and argue
 * about whether it was ever necessary.
 */
public class ItemVO implements Serializable {

    private final Long id;
    private final String title;
    private final String seller;
    private final Integer categoryId;
    private final Long currentCents;
    private final Integer bidCount;

    public ItemVO(Long id, String title, String seller, Integer categoryId,
                  Long currentCents, Integer bidCount) {
        this.id = id;
        this.title = title;
        this.seller = seller;
        this.categoryId = categoryId;
        this.currentCents = currentCents;
        this.bidCount = bidCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSeller() { return seller; }
    public Integer getCategoryId() { return categoryId; }
    public Long getCurrentCents() { return currentCents; }
    public Integer getBidCount() { return bidCount; }
}
