package com.xczg.blockchain.yibaodapp.util;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import ch.qos.logback.core.util.FileUtil;

//import com.develop.Configure;
//import com.develop.util.FileUtil;
//import com.develop.util.LogUtil;
//import com.develop.util.StringUtil;
@WebServlet(name="uploadFile",urlPatterns="/uploadFile")
public class UploadReceiver extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1498801237097404263L;
	private static File UPLOAD_DIR = new File("D://UserImg");
    private static File TEMP_DIR = new File("uploadsTemp");

    private static String CONTENT_TYPE = "text/plain;charset=utf-8";
    private static String CONTENT_LENGTH = "Content-Length";
    private static int RESPONSE_CODE = 200;

    @Override
    public void init() throws ServletException
    {
//    	UPLOAD_DIR = new File( Configure.SYSTEM_UPLOAD_DIR );
        //UPLOAD_DIR.mkdirs();
    	System.out.println("init");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	System.out.println("doPost");
    	
        String contentLengthHeader = req.getHeader(CONTENT_LENGTH);
//        Long expectedFileSize = StringUtil.isEmpty(contentLengthHeader) ? null : Long.parseLong(contentLengthHeader);
        
        RequestParser requestParser;
        
        try
        {
        	req.setCharacterEncoding("utf-8");
            resp.setContentType(CONTENT_TYPE);
            resp.setStatus(RESPONSE_CODE);
            String fileName="";
            String prefix = req.getParameter("prefix");
            if ( prefix == null ) prefix="";
            
            if (ServletFileUpload.isMultipartContent(req))
            {
            	System.out.println("if");
                requestParser = RequestParser.getInstance(req, new MultipartUploadParser(req, TEMP_DIR, getServletContext()));
                fileName=requestParser.getFilename();
                fileName=String.valueOf(System.currentTimeMillis())+"_"+fileName;

                if ( !"".equals(prefix) && !fileName.startsWith(prefix) ) {
                	fileName=prefix+fileName;
                }
                System.out.println("checkFileName="+fileName);
                String checkFileName=fileName.toUpperCase();
                boolean isSafe=checkSafe(checkFileName);
                
                System.out.println("isSafe="+isSafe);
                
                if ( !isSafe ) {
                	writeResponse(resp.getWriter(), "NotPermit",fileName);
                	return ;
                }
                
//                if (FileUtil.isExist(Configure.SYSTEM_UPLOAD_DIR, fileName) ) 
//                {
//                	writeResponse(resp.getWriter(), "Exist",fileName);
//                }
                else {
	                doWriteTempFileForPostRequest(requestParser,fileName);
	                writeResponse(resp.getWriter(), null,"/uploadFiles/"+fileName);
                }
            }
            else
            {
            	System.out.println("else");
            	requestParser = RequestParser.getInstance(req, null);
            	fileName=requestParser.getFilename();
            	System.out.println(fileName);
            	//prefix
                if ( !"".equals(prefix) && !fileName.startsWith(prefix) ) {
                	fileName=prefix+fileName;
                }
                
                String checkFileName=fileName.toUpperCase();
                boolean isSafe=checkSafe(checkFileName);
                if ( !isSafe ) {
                	writeResponse(resp.getWriter(), "NotPermit",fileName);
                	return ;
                }
                
            	 
//            	if(FileUtil.isExist(Configure.SYSTEM_UPLOAD_DIR, fileName) ) 
//            	{
//                	writeResponse(resp.getWriter(), "Exist",fileName);
//                } 
            	else {
            		System.out.println(Long.parseLong(contentLengthHeader));
                    writeToTempFile(req.getInputStream(), new File(UPLOAD_DIR, fileName), Long.parseLong(contentLengthHeader));
                    writeResponse(resp.getWriter(), null,fileName);
                }
            }
        } catch (Exception e)
        {
//        	LogUtil.error(e);
            writeResponse(resp.getWriter(), e.getMessage(),"");
        }
    }

    private boolean checkSafe(String checkFileName){
    	if ( checkFileName.startsWith(".") ) return false;
    	if ( checkFileName.indexOf("..") != -1 ) return false;
    	if ( checkFileName.endsWith(".PNG") 
        		|| checkFileName.endsWith(".JPG") || checkFileName.endsWith(".JPEG") ) {
        	return true;
        }
    	return false;
    }

    private void doWriteTempFileForPostRequest(RequestParser requestParser,String fileName) throws Exception
    {
        writeToTempFile(requestParser.getUploadItem().getInputStream(), new File(UPLOAD_DIR, fileName), null);
    }

    private File writeToTempFile(InputStream in, File out, Long expectedFileSize) throws IOException
    {
        FileOutputStream fos = null;
 
        try
        {
        	
        	System.out.println("out file : "+out.getAbsolutePath());
        	
            fos = new FileOutputStream(out);

            IOUtils.copy(in, fos);

            if (expectedFileSize != null)
            {
                Long bytesWrittenToDisk = out.length();
                if (!expectedFileSize.equals(bytesWrittenToDisk))
                {
                    throw new IOException(String.format("文件大小不匹配:实际大小为 %s ,期望大小为 %s", bytesWrittenToDisk, expectedFileSize));
                }
            }

            return out;
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            IOUtils.closeQuietly(fos);
        }
    }

    private void writeResponse(PrintWriter writer, String failureReason,String fileName)
    {
        if (failureReason == null)
        {
            writer.print("{\"success\": true,\"code\":1,\"message\":\""+fileName+"\"}");
        }
        else
        {
            writer.print("{\"error\": \"" + failureReason + "\",\"code\":2,\"message\":\"\"}");
        }
    }
}
