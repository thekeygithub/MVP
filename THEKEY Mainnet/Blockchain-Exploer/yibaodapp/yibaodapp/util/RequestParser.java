package com.xczg.blockchain.yibaodapp.util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;


import org.apache.commons.fileupload.FileItem;
public class RequestParser
{
    private static String FILENAME_PARAM = "qqfile";

    private String filename;
    private FileItem uploadItem;

    private RequestParser()
    {
    }

    //2nd param is null unless a MPFR
    static RequestParser getInstance(HttpServletRequest request, MultipartUploadParser multipartUploadParser) throws Exception
    {
    	System.out.println("aaa");
        RequestParser requestParser = new RequestParser();

        if (multipartUploadParser != null)
        {
            requestParser.uploadItem = multipartUploadParser.getFirstFile();
            requestParser.filename = multipartUploadParser.getFirstFile().getName();
        }
        else
        {
            requestParser.filename = request.getParameter(FILENAME_PARAM);
        }

        //grab other params here...

        return requestParser;
    }

    public String getFilename()
    {
        return filename;
    }

    //only non-null for MPFRs
    public FileItem getUploadItem()
    {
        return uploadItem;
    }
}
