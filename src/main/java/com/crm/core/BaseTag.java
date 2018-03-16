package com.crm.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by wlrllr on 2018/3/15.
 */
@Component
public class BaseTag extends TagSupport {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BaseTag.class);

    public BaseTag() {
    }

    public int doStartTag() throws JspException {
        try {
            HttpServletRequest e = (HttpServletRequest)this.pageContext.getRequest();
            String path = e.getContextPath();
            String basePath = e.getScheme() + "://" + e.getServerName() + ":" + e.getServerPort() + path + "/";
            StringBuilder base = new StringBuilder("<base href=\"");
            base.append(basePath);
            base.append("\">");
            JspWriter out = this.pageContext.getOut();
            out.println(base);
        } catch (Exception var6) {
            logger.error("doStartTag error", var6);
        }

        return 1;
    }
}
