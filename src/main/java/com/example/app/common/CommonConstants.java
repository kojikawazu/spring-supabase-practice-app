package com.example.app.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 共通定数群
 * @since 2024/06/21
 * @author koji kawazu
 */
public class CommonConstants {
	public static final String SESSION_CURRENT_USER_PROP = "currentUser";
	
	public static final String NORMAL_ROLE_NAME = "ROLE_USER";
	public static final String ADMIN_ROLE_NAME  = "ROLE_ADMIN";
	
	public static final Map<String, String> ROLE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(NORMAL_ROLE_NAME, "USER");
            put(ADMIN_ROLE_NAME, "ADMIN");
        }
    };
}
