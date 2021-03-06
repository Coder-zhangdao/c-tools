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
    }

    public static String extractFileExt(String filePathName) {
        String sep = System.getProperty("file.separator");
        if (filePathName.lastIndexOf(sep) > 0) {
            filePathName = filePathName.substring(filePathName
                    .lastIndexOf(sep));
        }

        sep = "/";

        if (filePathName.lastIndexOf(sep) > 0) {
            filePathName = filePathName.substring(filePathName
                    .lastIndexOf(sep));
        }
        int nPos = filePathName.lastIndexOf('.');
        return nPos < 0 ? "" : filePathName.substring(nPos + 1);
    }

    public static String getFileStorePath(String relativePath)
            throws IOException {

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy_MM");
        java.util.Date currentTime = new java.util.Date();
        String strDate = sdf.format(currentTime);
        String strAbsPath = relativePath;
        String uploadPath = strAbsPath;

        String sep = System.getProperty("file.separator");
        String tempPath = strAbsPath + sep + "file_upload_store" + sep
                + strDate + sep;

        File f0 = new File(uploadPath);
        if (!f0.exists() || !f0.isDirectory()) {
            if (!f0.mkdirs()) {
                throw new IOException("can' t make dir: " + uploadPath);
            } else {
                mLog.log(Level.INFO, "Dir created:" + uploadPath);
            }
        } else {
            mLog.log(Level.FINER, "Dir allready exists:" + uploadPath);
        }

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

            // ??????????????????

            String fileType = "." + extractFileExt(oldName).toLowerCase();

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

                // ??????????????????
            } else {
                flname += ".file";
            }
        } else {
            flname = seq + ".file";
        }
        return flname;

    }

    /**
     * ???????????????????????????
     *
     * @param fileName ?????????URL
     * @return ???????????????????????????
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
            String sep = "/";
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

        boolean isMultipart = ServletFileUpload
                .isMultipartContent(new ServletRequestContext(request));

        if (!isMultipart) {
            throw new CMyException(
                    "Please use multipart form! Something like  enctype=\"multipart/form-data\"");
        } else {
            // ????????????????????????,??????????????????
            File tempfile = new File(System.getProperty("java.io.tmpdir"));
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            // ????????????????????????????????????????????????,??????:??????
            diskFileItemFactory.setSizeThreshold(4096);
            // ?????????????????????,????????????????????????getSizeThreshold()???????????????????????????????????????
            diskFileItemFactory.setRepository(tempfile);
            ServletFileUpload upload = new ServletFileUpload(
                    diskFileItemFactory);
            // ??????????????????;
            upload.setSizeMax(maxSize);

            List /* FileItem */fileItems = upload.parseRequest(request);


            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                // ???????????????????????????
                FileItem fi = (FileItem) i.next();

                // ????????????????????????
                if (!fi.isFormField())
                {
                    m = saveFileItem(fileNameList, m++, storePath, fi);
                } else {
                    saveFieldItem(request, fi);

                }
            }
            if (fileNameList.size() == 0) {
                mLog.info("No file saved!");
            }
            return (String[]) fileNameList.toArray(new String[0]);
        }

    }

    public void saveFieldItem(javax.servlet.http.HttpServletRequest request,
                              FileItem fi) throws UnsupportedEncodingException {
        /*
		 * ???MULTIPART????????????????????????????????????????????????????????????
		 * ????????????????????????????????????????????????????????????REQUEST??????????????????
		 */
        Map<String, Vector<String>> fieldsMap;
        fieldsMap = (Map<String, Vector<String>>) request.getAttribute(ServletFileUpload.MULTIPART_FORM_DATA);
        if (fieldsMap == null) {
            fieldsMap = new HashMap<String, Vector<String>>();
            request.setAttribute(ServletFileUpload.MULTIPART_FORM_DATA, fieldsMap);
        }

        Vector<String> vec = new Vector<>();
        // ????????????????????????
        String fieldName = fi.getFieldName();
        // ?????????????????????????????????????????????????????????????????????????????????
        if (fieldsMap.get(fieldName) != null) {
            vec = fieldsMap.get(fieldName);

            // ?????????!??????????????????????????????
            vec.add(fi.getString(request.getCharacterEncoding()));
        }
        // ?????????????????????????????????
        else {
            vec.add(fi.getString(request.getCharacterEncoding()));
            fieldsMap.put(fieldName, vec);
        }
    }

    public int saveFileItem(List<String> fileNames, int m, String storePath, FileItem fi)
            throws Exception {
        String tempPath = getFileStorePath(storePath);
        String flname;
        flname = getNewFileName(fi.getName(), m);
        String original_name = getBaseFileName(fi.getName());
        fileNames.add(original_name + "|" + tempPath + flname);
        mLog.finest("DUMP:" + original_name);

        if (fi.getSize() > 0) {
            // ?????????????????????.
            fi.write(new File(tempPath + flname));
        }
        return m;
    }
}
