package com.bixuebihui.util.upfile;

import com.bixuebihui.util.other.CMyException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadFile {

    static Logger mLog = Logger.getLogger(UploadFile.class.getName());

    Map<String, String> whiteList;
    long maxSize = 10 * 1024 * 1024L; // 10M

    public UploadFile(Map<String, String> extentionFilter, long maxSize) {
        this.whiteList = extentionFilter;
        this.maxSize = maxSize;
    }

    public UploadFile() {
        whiteList = new Hashtable<String, String>();
        String[] extArray = {".gif", ".jpg", ".png", ".tif", ".doc", ".zip",
                ".xls", ".txt", ".docx", ".dwg", ".xlsx", ".dxf", ".ppt",
                ".rm", ".wma", ".mp3", ".rar", ".pdf"};
        for (int i = 0; i < extArray.length; i++) {
            whiteList.put(extArray[i].toLowerCase(), extArray[i]);
        }
        // filter.put(key, value);
    }

    public static String extractFileExt(String _sFilePathName) {
        String sep = System.getProperty("file.separator");
        if (_sFilePathName.lastIndexOf(sep) > 0) {
            _sFilePathName = _sFilePathName.substring(_sFilePathName
                    .lastIndexOf(sep));
        }

        sep = "/";

        if (_sFilePathName.lastIndexOf(sep) > 0) {
            _sFilePathName = _sFilePathName.substring(_sFilePathName
                    .lastIndexOf(sep));
        }
        int nPos = _sFilePathName.lastIndexOf('.');
        return nPos < 0 ? "" : _sFilePathName.substring(nPos + 1);
    }

    public static String getFileStorePath(String relativePath)
            throws IOException {

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy_MM"); // 格式化日期
        java.util.Date currentTime = new java.util.Date();// 得到当前系统时间
        String strDate = sdf.format(currentTime);
        // System.out.println(strDate);
        String strAbsPath = relativePath;// strAbsPath是C:\tomcat5\webapps\zichangl
        String uploadPath = strAbsPath;

        String sep = System.getProperty("file.separator");
        String tempPath = strAbsPath + sep + "file_upload_store" + sep
                + strDate + sep;// 最后结果是C:\tomcat5\webapps\zichangl\zichangl_hetong_file_upload

        File f0 = new File(uploadPath);
        if (!f0.exists() || !f0.isDirectory()) {
            if (f0.mkdirs() == false) {
                f0 = null;
                throw new IOException("can' t make dir: " + uploadPath);
            } else {
                mLog.log(Level.INFO, "Dir created:" + uploadPath);
            }
        } else {
            mLog.log(Level.FINER, "Dir allready exists:" + uploadPath);
        }

        f0 = null;
        f0 = new File(tempPath);

        if (!f0.exists() || !f0.isDirectory()) {
            if (f0.mkdirs() == false) {
                f0 = null;
                throw new IOException("can' t make dir: " + tempPath);
            } else {
                mLog.log(Level.INFO, "Dir created:" + tempPath);
            }
        } else {
            mLog.log(Level.FINER, "Dir allready exists:" + tempPath);
        }
        return tempPath;
    }

    public boolean isInWhiteList(String fileExtention) {
        return whiteList.containsKey(fileExtention.toLowerCase());
    }

    public String getNewFileName(String oldName, int seq) {
        String flname = "";
        if (oldName != null && !"".equals(oldName)) {

            // 获得文件后缀

            String fileType = "." + extractFileExt(oldName).toLowerCase();

            // upload.setRepositoryPath(tempPath);
            String ss = (new Date()).getTime() + "";

            if (seq < 10) {
                flname = ss + "_00" + seq + fileType;
            }
            if (10 <= seq && seq < 100) {
                flname = ss + "_0" + seq + fileType;
            }
            if (seq >= 100) {
                flname = ss + "_" + seq + fileType;
            }

            if (isInWhiteList(fileType)) {

                // 不改变扩展名
            } else {
                flname += ".file";
            }
        } else {
            flname = seq + ".file";
        }
        return flname;

    }

    /**
     * 不包括路径的文件名
     *
     * @param fileName 文件名URL
     * @return 不包括路径的文件名
     */
    public static String getBaseFileName(String fileName) {
        // First, ask the JDK for the base file name.

        // Now check for a Windows file name parsed incorrectly.
        int colonIndex = fileName.indexOf(":");
        if (colonIndex == -1) {
            // Check for a Windows SMB file path.
            colonIndex = fileName.indexOf("\\\\");
        }
        if (colonIndex == -1) {
            // Check for a URL file path.
            colonIndex = fileName.indexOf("//");
        }
        int backslashIndex = fileName.lastIndexOf("\\");
        if (backslashIndex == -1) {
            String sep = "/";// System.getProperty("file.separator");
            backslashIndex = fileName.lastIndexOf(sep);
        }
        if (colonIndex > -1 && backslashIndex > -1) {
            // Consider this filename to be a full Windows path, and parse it
            // accordingly to retrieve just the base file name.
            fileName = fileName.substring(backslashIndex + 1);
        }
        return fileName;
    }

    @SuppressWarnings("unchecked")
    public String[] submit(javax.servlet.http.HttpServletRequest request,
                           String storePath) throws Exception {
        List fileNameList = new ArrayList();

        int m = 0;
        String flname = "";

        // 取当前系统时间
        // SimpleDateFormat formatter = new
        // java.text.SimpleDateFormat("yyyy-MM-dd"); //格式化日期�

        boolean isMultipart = ServletFileUpload
                .isMultipartContent(new ServletRequestContext(request));

        if (!isMultipart) {
            throw new CMyException(
                    "Please use multipart form! Something like  enctype=\"multipart/form-data\"");
        } else {
            // 临时缓冲文件目录,此处系统默认
            File tempfile = new File(System.getProperty("java.io.tmpdir"));
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            // 设置最多只允许在内存中存储的数据,单位:字节
            diskFileItemFactory.setSizeThreshold(4096);
            // 设置缓冲区目录,一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
            diskFileItemFactory.setRepository(tempfile);
            ServletFileUpload upload = new ServletFileUpload(
                    diskFileItemFactory);
            upload.setSizeMax(maxSize); // 设置文件大小;

            List /* FileItem */fileItems = upload.parseRequest(request);

            // System.out.println("fileItems.size()=" + fileItems.size() +
            // "<br>");

            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                // 以下是一些变量定义
                FileItem fi = (FileItem) i.next();

                if (!fi.isFormField()) // 这里开始外理文件
                {
                    m = saveFileItem(fileNameList, m++, storePath, fi);
                } else {
                    saveFieldItem(request, fi);

                }
            }
            if (fileNameList.size() == 0) {
                //throw new CMyException("No file saved!");
                mLog.info("No file saved!");
            }
            return (String[]) fileNameList.toArray(new String[0]);
        }

    }

    public void saveFieldItem(javax.servlet.http.HttpServletRequest request,
                              FileItem fi) throws UnsupportedEncodingException {
        /*
		 * 因MULTIPART基于流的机制，必须在遍历时保留其他参数，
		 * 不保留的话将丢丢，也不是说本代码段在一个REQUEST只能调用一次
		 */
        Map<String, Vector<String>> fieldsMap;
        fieldsMap = (Map<String, Vector<String>>) request.getAttribute(ServletFileUpload.MULTIPART_FORM_DATA);
        if (fieldsMap == null) {
            fieldsMap = new HashMap<String, Vector<String>>();
            request.setAttribute(ServletFileUpload.MULTIPART_FORM_DATA, fieldsMap);
        }

        Vector<String> vec = new Vector<String>();
        String fieldName = fi.getFieldName(); // 这里取得字段名称
        // 如果已经存在此字段信息（多选列表或多个同名的隐藏域等）
        if (fieldsMap.get(fieldName) != null) {
            vec = (Vector<String>) fieldsMap.get(fieldName);
            //mLog.info("name:" + fieldName + " value:"
            //		+ fi.getString(request.getCharacterEncoding()));
            // 很重要!需要进行编码哦！！！
            vec.add(fi.getString(request.getCharacterEncoding()));
        }
        // 第一次取到这个名的字段
        else {
            //mLog.info("name:" + fieldName + " value: "
            //		+ fi.getString(request.getCharacterEncoding()));
            vec.add(fi.getString(request.getCharacterEncoding()));
            fieldsMap.put(fieldName, vec);
        }
    }

    public int saveFileItem(List<String> fileNames, int m, String storePath, FileItem fi)
            throws Exception {
        String tempPath = getFileStorePath(storePath);// application.getRealPath(""));
        String flname;
        flname = getNewFileName(fi.getName(), m);
        String original_name = getBaseFileName(fi.getName());
        fileNames.add(original_name + "|" + tempPath + flname);
        mLog.finest("DUMP:" + original_name);

        if (fi.getSize() > 0) {
            fi.write(new File(tempPath + flname)); // 写文件到服务器.
        } else {
            //	mLog.finest("File size=0, No file saved! original file name is:"
            //					+ original_name);
        }
        return m;
    }
}
