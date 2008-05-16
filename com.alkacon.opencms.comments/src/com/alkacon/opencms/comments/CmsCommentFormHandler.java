/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.comments/src/com/alkacon/opencms/comments/CmsCommentFormHandler.java,v $
 * Date   : $Date: 2008/05/16 11:40:26 $
 * Version: $Revision: 1.2 $
 *
 * This file is part of the Alkacon OpenCms Add-On Module Package
 *
 * Copyright (c) 2007 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * The Alkacon OpenCms Add-On Module Package is free software: 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Alkacon OpenCms Add-On Module Package is distributed 
 * in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Alkacon OpenCms Add-On Module Package.  
 * If not, see http://www.gnu.org/licenses/.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com.
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org.
 */

package com.alkacon.opencms.comments;

import com.alkacon.opencms.formgenerator.CmsFormHandler;
import com.alkacon.opencms.formgenerator.I_CmsField;

import org.opencms.i18n.CmsMessages;
import org.opencms.main.OpenCms;
import org.opencms.module.CmsModule;
import org.opencms.util.CmsHtmlStripper;
import org.opencms.util.CmsMacroResolver;
import org.opencms.util.CmsStringUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * The form handler controls the html or mail output of a configured comment form.<p>
 * 
 * Provides methods to determine the action that takes place and methods to create different
 * output formats of a submitted form.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.2 $
 * 
 * @since 7.0.5
 */
public class CmsCommentFormHandler extends CmsFormHandler {

    /** The comment field name constant. */
    public static final String FIELD_COMMENT = "comment";

    /** The ip address field name constant. */
    public static final String FIELD_IPADDRESS = "ipaddress";

    /** The locale field name constant. */
    public static final String FIELD_LOCALE = "locale";

    /** The user field name constant. */
    public static final String FIELD_USERNAME = "username";

    /** The module name. */
    private static final String MODULE = "com.alkacon.opencms.comments";

    /**
     * Constructor, creates the necessary form configuration objects.<p>
     * 
     * @param context the JSP page context object
     * @param req the JSP request 
     * @param res the JSP response 
     * 
     * @throws Exception if creating the form configuration objects fails
     */
    public CmsCommentFormHandler(PageContext context, HttpServletRequest req, HttpServletResponse res)
    throws Exception {

        super(context, req, res);
    }

    /**
     * Constructor, creates the necessary form configuration objects using a given configuration file URI.<p>
     * 
     * @param context the JSP page context object
     * @param req the JSP request 
     * @param res the JSP response 
     * @param formConfigUri URI of the form configuration file, if not provided, current URI is used for configuration
     * 
     * @throws Exception if creating the form configuration objects fails
     */
    public CmsCommentFormHandler(
        PageContext context,
        HttpServletRequest req,
        HttpServletResponse res,
        String formConfigUri)
    throws Exception {

        super(context, req, res, formConfigUri);
    }

    /**
     * Returns the form configuration.<p>
     * 
     * @return the form configuration
     */
    public CmsCommentForm getCommentFormConfiguration() {

        return (CmsCommentForm)super.getFormConfiguration();
    }

    /**
     * Initializes the form handler and creates the necessary configuration objects.<p>
     * 
     * @param req the JSP request 
     * @param formConfigUri URI of the form configuration file, if not provided, current URI is used for configuration
     * 
     * @throws Exception if creating the form configuration objects fails
     */
    public void init(HttpServletRequest req, String formConfigUri) throws Exception {

        m_parameterMap = new HashMap();
        m_parameterMap.putAll(getRequest().getParameterMap());

        m_macroResolver = CmsMacroResolver.newInstance();
        m_macroResolver.setKeepEmptyMacros(true);
        m_macroResolver.addMacro(MACRO_URL, OpenCms.getSiteManager().getCurrentSite(getCmsObject()).getServerPrefix(
            getCmsObject(),
            getRequestContext().getUri())
            + link(getRequestContext().getUri()));
        m_macroResolver.addMacro(MACRO_LOCALE, getRequestContext().getLocale().toString());

        String formAction = getParameter(PARAM_FORMACTION);
        m_isValidatedCorrect = null;
        setInitial(CmsStringUtil.isEmpty(formAction));
        // get the localized messages
        CmsModule module = OpenCms.getModuleManager().getModule(MODULE);
        String para = module.getParameter("message", "/com/alkacon/opencms/comments/workplace");

        setMessages(new CmsMessages(para, getRequestContext().getLocale()));
        // get the form configuration
        setFormConfiguration(new CmsCommentForm(this, getMessages(), isInitial(), formConfigUri, formAction));
    }

    /**
     * @see com.alkacon.opencms.formgenerator.CmsFormHandler#sendDatabase()
     */
    protected boolean sendDatabase() throws Exception {

        I_CmsField field = getFormConfiguration().getFieldByDbLabel(FIELD_COMMENT);
        if (field != null) {
            String value = new CmsHtmlStripper(false).stripHtml(field.getValue());
            value = CmsStringUtil.substitute(value, "\n\n", "<p>");
            value = CmsStringUtil.substitute(value, "\n", "<br>");
            field.setValue(value);
        }
        return super.sendDatabase();
    }
}
