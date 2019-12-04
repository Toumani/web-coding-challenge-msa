package org.shopzz.shopzzapp.util;

import org.shopzz.model.Shop;

import java.io.Serializable;

/**
 * Mother class of all request coming to the API
 */

public class Request implements Serializable {
    protected String hashcode;

    public String getHashcode() { return hashcode; }

    public static final long serialVersionUID = 1L;
}
