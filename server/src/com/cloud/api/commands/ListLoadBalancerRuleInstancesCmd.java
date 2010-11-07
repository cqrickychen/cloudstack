/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.ApiResponseHelper;
import com.cloud.api.BaseListCmd;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.ServerApiException;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.UserVmResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientAddressCapacityException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.PermissionDeniedException;
import com.cloud.vm.UserVmVO;

@Implementation(method="listLoadBalancerInstances", description="List all virtual machine instances that are assigned to a load balancer rule.")
public class ListLoadBalancerRuleInstancesCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger (ListLoadBalancerRuleInstancesCmd.class.getName());

    private static final String s_name = "listloadbalancerruleinstancesresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.APPLIED, type=CommandType.BOOLEAN, description="true if listing all virtual machines currently applied to the load balancer rule; default is true")
    private Boolean applied;

    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, required=true, description="the ID of the load balancer rule")
    private Long id;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Boolean isApplied() {
        return applied;
    }

    public Long getId() {
        return id;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getName() {
        return s_name;
    }

    @Override @SuppressWarnings("unchecked")
    public ListResponse<UserVmResponse> getResponse() {
        List<UserVmVO> instances = (List<UserVmVO>)getResponseObject();

        ListResponse<UserVmResponse> response = new ListResponse<UserVmResponse>();
        List<UserVmResponse> vmResponses = new ArrayList<UserVmResponse>();
        for (UserVmVO instance : instances) {
            UserVmResponse userVmResponse = ApiResponseHelper.createUserVmResponse(instance);
            userVmResponse.setObjectName("loadbalancerruleinstance");
            vmResponses.add(userVmResponse);
        }

        response.setResponses(vmResponses);
        response.setResponseName(getName());
        return response;
    }
    
    @Override
    public Object execute() throws ServerApiException, InvalidParameterValueException, PermissionDeniedException, InsufficientAddressCapacityException, InsufficientCapacityException, ConcurrentOperationException{
        List<UserVmVO> result = _mgr.listLoadBalancerInstances(this);
        return result;
    }
}
