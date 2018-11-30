package com.xczg.blockchain.yibaodapp.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang3.StringUtils;

//import com.develop.util.LogUtil;
//import com.develop.util.StringUtil;

public class MultipartUploadParser
{
	private Map<String, String> params = new HashMap<String, String>();

	private List<FileItem> files = new ArrayList<FileItem>();

	// fileItemsFactory is a field (even though it's scoped to the constructor) to prevent the
	// org.apache.commons.fileupload.servlet.FileCleanerCleanup thread from attempting to delete the
	// temp file before while it is still being used.
	//
	// FileCleanerCleanup uses a java.lang.ref.ReferenceQueue to delete the temp file when the FileItemsFactory marker object is GCed
	private DiskFileItemFactory fileItemsFactory;

	@SuppressWarnings("unchecked")
	public MultipartUploadParser(HttpServletRequest request, File repository, ServletContext context) throws Exception
	{
		if (!repository.exists() && !repository.mkdirs())
		{
			throw new IOException("Unable to mkdirs to " + repository.getAbsolutePath());
		}

		fileItemsFactory = setupFileItemFactory(repository, context);

        ServletFileUpload upload = new ServletFileUpload(fileItemsFactory);
        List<FileItem> formFileItems = upload.parseRequest(request);

		parseFormFields(formFileItems);

		if (files.isEmpty())
		{
			//LogUtil.warn("No files were found when processing the requst. Debugging info follows.");

			throw new FileUploadException("No files were found when processing the requst.");
		}
	}

	private DiskFileItemFactory setupFileItemFactory(File repository, ServletContext context)
	{
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
		factory.setRepository(repository);

		FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(context);
		factory.setFileCleaningTracker(pTracker);

		return factory;
	}

	private void parseFormFields(List<FileItem> items)
	{
		for (FileItem item : items)
		{
			if (item.isFormField())
			{
//				String key = item.getFieldName();
//				String value = item.getString();
			    //	if (!StringUtil.isEmpty(key))
				//{
				//	params.put(key.toLowerCase(), StringUtils.defaultString(value));
				//}
			}
			else
			{
				files.add(item);
			}
		}
	}

	public Map<String, String> getParams()
	{
		return params;
	}

	public List<FileItem> getFiles()
	{
		if (files.isEmpty())
		{
			throw new RuntimeException("No FileItems exist.");
		}

		return files;
	}

	public FileItem getFirstFile()
	{
		if (files.isEmpty())
		{
			throw new RuntimeException("No FileItems exist.");
		}

		return files.iterator().next();
	}
}
