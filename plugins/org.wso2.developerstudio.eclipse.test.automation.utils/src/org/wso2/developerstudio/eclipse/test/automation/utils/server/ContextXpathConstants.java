/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.test.automation.utils.server;

public class ContextXpathConstants {
    // constants for product group element
    public static final String PRODUCT_GROUP_NAME = "//productGroup[@name='%s']";
    public static final String PRODUCT_GROUP_PARENT = "//platform/productGroup";
    public static final String PRODUCT_GROUP_ALL_STANDALONE_INSTANCE = "//platform/productGroup/instance[@type='standalone']";

    public static final String PRODUCT_GROUP_STANDALONE_INSTANCE = "//productGroup[@default='true']/instance[@type='standalone']";
    public static final String PRODUCT_GROUP_CLUSTERING_ENABLED = "//productGroup[@name='%s']/@clusteringEnabled";
    public static final String PRODUCT_GROUP_DEFAULT_NAME = "//productGroup[@default='true']/@name";
    public static final String PRODUCT_GROUP_INSTANCE_NAME = "//productGroup[@name='%s']/instance[@name='%s']";
    public static final String PRODUCT_GROUP_INSTANCE_TYPE = "//productGroup[@name='%s']/instance[@type='%s']";
    public static final String PRODUCT_GROUP_INSTANCE_PORT = "//productGroup[@name='%s']/instance[@name='%s']/ports/port";
    public static final String PRODUCT_GROUP_INSTANCE_HOST = "//productGroup[@name='%s']/instance[@name='%s']/hosts/host";
    public static final String PRODUCT_GROUP_INSTANCE_PROPERTY = "//productGroup[@name='%s']/instance[@name='%s']/properties/property";
    public static final String PRODUCT_GROUP_DEFAULT_STANDALONE_INSTANCE = ""
            + "//productGroup[@default='true']/instance[@type='standalone']";
    public static final String PRODUCT_GROUP_INSTANCE = "//productGroup[@name='%s']/instance[ @name='%s']";
    public static final String PRODUCT_GROUP_INSTANCE_PORTS = "//productGroup[@name='%s']/instance[@name='%s']/ports/port";
    public static final String PRODUCT_GROUP_INSTANCE_HOSTS = "//productGroup[@name='%s']/instance[@name='%s']/hosts/host";
    public static final String PRODUCT_GROUP_INSTANCE_PROPERTIES = "//productGroup[@name='%s']/instance[@name='%s']/properties/property";
    // constants for user management element
    public static final String USER_MANAGEMENT_SUPER_TENANT_KEY = "//userManagement/superTenant/tenant/@key";
    public static final String SUPER_TENANT_ADMIN_USERNAME = "//userManagement/superTenant/tenant[@key='%s']/admin/user[@key='%s']/userName";
    public static final String SUPER_TENANT_ADMIN_PASSWORD = "//userManagement/superTenant/tenant[@key='%s']/admin/user[@key='%s']/password";
    public static final String USER_MANAGEMENT_SUPER_TENANT_ADMIN = "//superTenant/tenant/admin";
    public static final String USER_MANAGEMENT_SUPER_TENANT_USER_KEY = "//superTenant/tenant/%s/user/@key";
    public static final String USER_MANAGEMENT_SUPER_TENANT_DOMAIN = "//superTenant/tenant/@domain";
    public static final String USER_MANAGEMENT_TENANT_ADMIN_USERNAME = "//%s/tenant[@domain='%s']/admin/user[@key='%s']/userName";
    public static final String USER_MANAGEMENT_TENANT_ADMIN_PASSWORD = "//%s/tenant[@domain='%s']/admin/user[@key='%s']/password";
    public static final String USER_MANAGEMENT_SUPER_TENANT_ADMIN_USER = "//superTenant/tenant/admin/user";
    public static final String USER_MANAGEMENT_SUPER_TENANT_USERS = "//superTenant/tenant/users";
    public static final String USER_MANAGEMENT_TENANT_DOMAIN = "//%s/tenant[@key='%s']/@domain";
    public static final String TENANT_DOMAIN = "//tenants/tenant/@domain";
    public static final String USER_MANAGEMENT_TENANT_ADMIN = "//tenants/tenant[@domain='%s']/admin";
    public static final String USER_MANAGEMENT_TENANT_USERS = "//tenants/tenant[@domain='%s']/users";
    public static final String USER_MANAGEMENT_TENANT_USER = "//%s/tenant[@domain='%s']/users/user[@key='%s']";
    public static final String USER_MANAGEMENT_TENANT_USER_KEY = "//tenants/tenant[@domain='%s']/%s/user/@key";
    public static final String SUPER_TENANT_DOMAIN = "//superTenant/tenant[@key='superTenant']/@domain";
    public static final String USER_MANAGEMENT_TENANT_USER_NAME = "//%s/tenant[@domain='%s']/users/user[@key='%s']/userName";
    public static final String USER_MANAGEMENT_TENANT_USER_PASSWORD = "//%s/tenant[@domain='%s']/users/user[@key='%s']/password";
    public static final String PRODUCT_GROUP_WEBCONTEXT = "webContext";
    public static final String PRODUCT_GROUP_PORT_HTTPS = "https";
    public static final String PRODUCT_GROUP_PORT_HTTP = "http";
    public static final String PRODUCT_GROUP_PORT_NHTTPS = "nhttps";
    public static final String PRODUCT_GROUP_PORT_NHTTP = "nhttp";
    public static final String TENANTS = "tenants";
    public static final String SUPER_TENANT = "superTenant";
    public static final String SUPER_ADMIN = "superAdmin";
    public static final String ADMIN = "admin";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String INSTANCE = "instance";
    public static final String KEY = "key";
    public static final String NON_BLOCKING_ENABLED = "nonBlockingTransportEnabled";
    public static final String WORKER = "worker";
    public static final String DEFAULT = "default";
    public static final String MANAGER = "manager";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String USERS = "users";
    public static final String CLUSTERING_ENABLED = "clusteringEnabled";
    public static final String SUPER_TENANT_ADMIN = "SUPER_TENANT_ADMIN";
    public static final String SUPER_TENANT_USER = "SUPER_TENANT_USER";
    public static final String TENANT_ADMIN = "TENANT_ADMIN";
    public static final String TENANT_USER = "TENANT_USER";
    public static final String EXECUTION_ENVIRONMENT = "//executionEnvironment/text()";

    public static final String ROLES = "roles";
    public static final String USER_MANAGEMENT_TENANT_USERS_ROLES = "//%s/tenant[@domain='%s']/users/user[@key='%s']/roles/role";

    public static final String USER_NODE = "//%s/tenant[@domain='%s']/users/user";
    public static final String TENANTS_NODE = "//tenants";
    public static final String DOMAIN = "domain";

}
