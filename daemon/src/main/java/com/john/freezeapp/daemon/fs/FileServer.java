package com.john.freezeapp.daemon.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fi.iki.elonen.NanoHTTPD;

public class FileServer extends NanoHTTPD {
    private final File rootDir;
    private final boolean allowDirectoryListing;
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        // 扩展 MIME 类型支持
        MIME_TYPES.put("html", "text/html; charset=utf-8");
        MIME_TYPES.put("txt", "text/plain; charset=utf-8");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("zip", "application/zip");
        MIME_TYPES.put("apk", "application/vnd.android.package-archive");
    }

    public FileServer(int port, File rootDir, boolean allowDirectoryListing) {
        super(port);
        this.rootDir = rootDir.getAbsoluteFile();
        this.allowDirectoryListing = allowDirectoryListing;

        // 验证根目录
        if (!this.rootDir.isDirectory()) {
            throw new IllegalArgumentException("Root path must be a directory");
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            // 获取并清理请求路径
            String uri = sanitizeUri(session.getUri());
            boolean isDownload = session.getParms().containsKey("download");

            File requestedFile = new File(rootDir, uri);

            // 安全校验：防止路径遍历攻击
            if (!requestedFile.getCanonicalPath().startsWith(rootDir.getCanonicalPath())) {
                return errorResponse(Response.Status.FORBIDDEN, "Access denied");
            }

            if (requestedFile.isDirectory()) {
                if (isDownload) {
                    return handleDirectoryDownload(requestedFile);
                }
                return handleDirectory(requestedFile, uri);
            } else {
                if (isDownload) {
                    return handleFileDownload(requestedFile);
                }
                return handleFile(requestedFile);
            }
        } catch (Exception e) {
            return errorResponse(Response.Status.INTERNAL_ERROR, e.getMessage());
        }
    }

    private Response handleFile(File file) throws FileNotFoundException {
        if (!file.exists()) {
            return errorResponse(Response.Status.NOT_FOUND, "File not found");
        }

        String mimeType = getMimeType(file.getName());
        FileInputStream fis = new FileInputStream(file);

        Response response = newFixedLengthResponse(
                Response.Status.OK,
                mimeType,
                fis,
                file.length()
        );

        // 设置下载头（可选）
        response.addHeader("Content-Disposition", "inline; filename=\"" + encodeFileName(file.getName()) + "\"");
        return response;
    }

    private Response handleDirectory(File dir, String uri) {
        // 检查目录是否存在 index.html
        File indexFile = new File(dir, "index.html");
        if (indexFile.exists()) {
            try {
                return handleFile(indexFile);
            } catch (FileNotFoundException e) {
                // 正常情况下不会发生
            }
        }

        if (!allowDirectoryListing) {
            return errorResponse(Response.Status.FORBIDDEN, "Directory listing disabled");
        }

        // 生成目录列表 HTML
        String html = generateDirectoryListing(dir, uri);
        return newFixedLengthResponse(
                Response.Status.OK,
                "text/html; charset=utf-8",
                html
        );
    }

    private String generateDirectoryListing(File dir, String currentUri) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>")
                .append("<meta charset='utf-8'>")
                .append("<title>Index of ").append(currentUri).append("</title>")
                .append("<style>")
                .append("body { font-family: sans-serif; margin: 20px; }")
                .append(".file-list { list-style: none; padding: 0; }")
                .append(".file-item { display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #eee; }")
                .append(".file-item:hover { background-color: #f5f5f5; }")
                .append(".file-icon { margin-right: 10px; }")
                .append(".file-name { flex-grow: 1; }")
                .append(".file-size { color: #666; margin: 0 20px; }")
                .append(".download-btn { padding: 5px 10px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 3px; }")
                .append(".download-btn:hover { background-color: #45a049; }")
                .append("</style>")
                .append("</head><body>")
                .append("<h1>Index of ").append(currentUri).append("</h1><hr>")
                .append("<ul class='file-list'>");

        // 添加上级目录链接（非根目录时）
        if (!currentUri.equals("/")) {
            html.append("<li class='file-item'>")
                .append("<span class='file-icon'>📁</span>")
                .append("<a href='").append(getParentUri(currentUri)).append("' class='file-name'>..</a>")
                .append("</li>");
        }

        // 遍历目录内容
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                String encodedName = encodeUriComponent(name);
                String link = currentUri.endsWith("/") ? currentUri + encodedName : currentUri + "/" + encodedName;
                String downloadLink = link + "?download=true";
                String icon = file.isDirectory() ? "📁" : "📄";

                html.append("<li class='file-item'>")
                    .append("<span class='file-icon'>").append(icon).append("</span>")
                    .append("<a href='").append(link).append("' class='file-name'>")
                    .append(escapeHtml(name))
                    .append(file.isDirectory() ? "/" : "")
                    .append("</a>")
                    .append("<span class='file-size'>")
                    .append(file.isDirectory() ? "Directory" : formatFileSize(file.length()))
                    .append("</span>")
                    .append("<a href='").append(downloadLink).append("' class='download-btn'>Download</a>")
                    .append("</li>");
            }
        }

        html.append("</ul><hr></body></html>");
        return html.toString();
    }

    private Response handleFileDownload(File file) throws FileNotFoundException {
        if (!file.exists()) {
            return errorResponse(Response.Status.NOT_FOUND, "File not found");
        }

        String mimeType = getMimeType(file.getName());
        FileInputStream fis = new FileInputStream(file);

        Response response = newFixedLengthResponse(
                Response.Status.OK,
                mimeType,
                fis,
                file.length()
        );

        // 设置下载头
        response.addHeader("Content-Disposition", "attachment; filename=\"" + encodeFileName(file.getName()) + "\"");
        return response;
    }

    private Response handleDirectoryDownload(File dir) {
        File tempFile = null;
        try {
            // 在应用缓存目录创建临时文件
            File cacheDir = new File(rootDir, "cache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            tempFile = new File(cacheDir, "download_" + System.currentTimeMillis() + ".zip");

            // 创建ZIP文件
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile));
            addToZip(dir, dir, zos);
            zos.close();

            // 直接使用文件流作为响应
            FileInputStream fis = new FileInputStream(tempFile);
            Response response = newFixedLengthResponse(
                    Response.Status.OK,
                    "application/zip",
                    fis,
                    tempFile.length()
            );

            // 设置下载头
            response.addHeader("Content-Disposition", "attachment; filename=\"" + encodeFileName(dir.getName() + ".zip") + "\"");
            return response;
        } catch (IOException e) {
            return errorResponse(Response.Status.INTERNAL_ERROR, "Failed to create zip file: " + e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private void addToZip(File rootDir, File currentFile, ZipOutputStream zos) throws IOException {
        if (currentFile.isDirectory()) {
            // 为目录创建ZIP条目
            String entryName = "";
            if (!currentFile.equals(rootDir)) {
                entryName = currentFile.getPath().substring(rootDir.getPath().length() + 1).replace('\\', '/');
                entryName += "/";
            }
            if (!entryName.isEmpty()) {
                ZipEntry entry = new ZipEntry(entryName);
                entry.setTime(currentFile.lastModified());
                zos.putNextEntry(entry);
                zos.closeEntry();
            }

            // 递归处理目录内容
            File[] files = currentFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    addToZip(rootDir, file, zos);
                }
            }
        } else {
            // 为文件创建ZIP条目
            String entryName = currentFile.getPath().substring(rootDir.getPath().length() + 1).replace('\\', '/');
            ZipEntry entry = new ZipEntry(entryName);
            entry.setTime(currentFile.lastModified());
            zos.putNextEntry(entry);

            // 写入文件内容
            FileInputStream fis = new FileInputStream(currentFile);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            fis.close();
            zos.closeEntry();
        }
    }

    // 辅助方法 -----------------------------------------------------------------

    private String sanitizeUri(String uri) throws UnsupportedEncodingException {
        // URL 解码并规范化路径
        uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
        uri = uri.replace('/', File.separatorChar);

        // 移除 query 参数
        int paramsStart = uri.indexOf('?');
        if (paramsStart >= 0) {
            uri = uri.substring(0, paramsStart);
        }

        // 防止空路径
        if (uri.isEmpty()) uri = "/";

        return uri;
    }

    private String getMimeType(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) return "application/octet-stream";

        String ext = filename.substring(dotIndex + 1).toLowerCase();
        return MIME_TYPES.getOrDefault(ext, "application/octet-stream");
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private String encodeFileName(String name) {
        try {
            return new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            return name;
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String unit = "KMGTPE".charAt(exp-1) + "iB";
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), unit);
    }

    private String getParentUri(String uri) {
        int lastSlash = uri.lastIndexOf('/');
        if (lastSlash <= 0) return "/";
        return uri.substring(0, lastSlash);
    }

    private Response errorResponse(Response.Status status, String message) {
        String html = "<html><body><h1>" + status + "</h1><p>" + message + "</p></body></html>";
        return newFixedLengthResponse(status, "text/html", html);
    }

    public static String encodeUriComponent(String s) {
        // 实现 URL 编码逻辑
        try {
            return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8.name())
                    .replace("+", "%20")
                    .replace("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
}
