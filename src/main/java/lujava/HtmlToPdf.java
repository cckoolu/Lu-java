package lujava;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;


public class HtmlToPdf {
    public static void htmlToPdf(InputStream inputStream) throws Exception {
        pdfCreate(inputStream);
    }

    public static void htmlToPdf(String htmlText) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        pdfCreate(inputStream);
    }

    public static void pdfCreate(InputStream inputStream) throws Exception {
        File pdfDest = new File("target/output.pdf");
        OutputStream os = new FileOutputStream(pdfDest);

        Document document = new Document(PageSize.A4);
        // 为该Document创建一个Writer实例
        PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
        pdfWriter.setViewerPreferences(PdfWriter.HideToolbar);
        document.open();
        XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
        worker.parseXHtml(pdfWriter, document, inputStream, StandardCharsets.UTF_8, new AsianFontProvider());

        System.out.println("PDF生成完毕");
        document.close();
    }
}

class AsianFontProvider extends XMLWorkerFontProvider {
    @Override
    public Font getFont(final String fontName, String encoding, float size, final int style) {
        try {
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return new Font(bfChinese, size, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getFont(fontName, encoding, size, style);
    }
}