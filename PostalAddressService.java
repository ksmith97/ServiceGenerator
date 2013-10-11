/*
 * Copyright © 2008, Benecard, All Rights Reserved
 * Unless otherwise indicated, all source code is the copyright of Benecard
 * and is protected by applicable US and international copyright laws,
 * all rights reserved. No part of this material may be used for any purpose
 * without prior written permission. This material and all source codes may
 * not be reproduced in any form without permission in writing from Benecard.
 * Use for any purpose without written permission from Benecard is expressly
 * prohibited by law, and may result in civil and criminal penalties.
 * All rights reserved. No part of this file may be reproduced in any form
 * or by any electronic or mechanical means, including the use of information
 * storage and retrieval systems, without permission in writing from
 * the copyright owner.
 */

package com.benecard.service.ejb.postaladdress;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import com.benecard.commons.ILogUtil;
import com.benecard.commons.LogUtil;
import com.benecard.commons.exceptions.BenecardException;
import com.benecard.dal.PostalAddressDAL;
import com.benecard.pbm.model.City;
import com.benecard.pbm.model.County;
import com.benecard.pbm.model.PostalAddress;
import com.benecard.pbm.model.PostalCode;
import com.benecard.service.ejb.BaseService;

/**
 * Enter the description for the class.
 * 
 * 
 * @author edata_rc
 * @date Dec 16, 2008
 * 
 */

@Stateless
public class PostalAddressService extends BaseService implements
                                                     IPostalAddressServiceLocal,
                                                     IPostalAddressServiceRemote
{
    private transient static ILogUtil logger = LogUtil.getInstance( PostalAddressService.class );

    @Resource( name = "PBMDS", type = javax.sql.DataSource.class )
    private DataSource dataSource;

    @Resource
    private SessionContext sessionContext;

    public final Set<City> getCities( final String cityName ) throws BenecardException
    {
        Connection connection = null;
        try
        {
            connection = getConnection( dataSource );
            String userName = sessionContext.getCallerPrincipal().getName();
            return new PostalAddressDAL( connection, userName ).getCities( connection, cityName );
        }
        finally
        {
            closeConnection( connection );
        }
    }

    public final Set<County> getCounties( final PostalAddress postalAddress ) throws BenecardException
    {
        Connection connection = null;
        try
        {
            connection = getConnection( dataSource );
            String userName = sessionContext.getCallerPrincipal().getName();
            return new PostalAddressDAL( connection, userName ).getCounties( connection, postalAddress );
        }
        finally
        {
            closeConnection( connection );
        }
    }

    public final List<PostalAddress> getCityCountyStateCountryByZipCode( final String zipCode )
                                                                                               throws BenecardException
    {
        Connection connection = null;
        try
        {
            connection = getConnection( dataSource );
            String userName = sessionContext.getCallerPrincipal().getName();
            return new PostalAddressDAL( connection, userName ).getCityCountyStateCountryByZipCode( zipCode );
        }
        finally
        {
            closeConnection( connection );
        }
    }

    public final Set<PostalCode> getPostalCodes( final PostalAddress postalAddress ) throws BenecardException
    {
        Connection connection = null;
        try
        {
            connection = getConnection( dataSource );
            String userName = sessionContext.getCallerPrincipal().getName();
            return new PostalAddressDAL( connection, userName ).getPostalCodes( connection, postalAddress );
        }
        finally
        {
            closeConnection( connection );
        }
    }

    public final Map<String, Boolean> validateAddressFields( final PostalAddress postalAddress )
                                                                                                throws BenecardException
    {
        Connection connection = null;
        try
        {
            connection = getConnection( dataSource );
            String userName = sessionContext.getCallerPrincipal().getName();
            return new PostalAddressDAL( connection, userName ).validateAddressFields( connection, postalAddress );
        }
        finally
        {
            closeConnection( connection );
        }
    }

    public Object[] getStateAndCities( final String zipcode ) throws BenecardException
    {
        Connection connection = null;
        try
        {
            connection = getConnection( dataSource );
            String userName = sessionContext.getCallerPrincipal().getName();
            return new PostalAddressDAL( connection, userName ).getStateAndCities( connection, zipcode );
        }
        finally
        {
            closeConnection( connection );
        }
    }
    
    public Object[] test1( int[] a, List<String, List<Map<String, String>>> b) { return new Object[0]; }
}
