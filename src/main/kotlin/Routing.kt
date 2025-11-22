package com

import com.fasterxml.jackson.databind.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy
import java.io.*
import org.apache.poi.xwpf.usermodel.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        docRoutes()
    }
}

fun Route.docRoutes(){
    get("/generar-doc"){

        val file = generarMarcaDeAgua(
            marcaDeAguaText = "CONFIDENCIAL",
            footerText = "Generado para comer chivo"
        )
        call.respondFile(file)

    }
}

fun generarMarcaDeAgua(marcaDeAguaText: String, footerText: String): File {

    val doc = XWPFDocument()

    //Marca de Agua

    val header = doc.createHeaderFooterPolicy().createHeader(XWPFHeaderFooterPolicy.DEFAULT)
    val p = header.createParagraph()
    p.alignment = ParagraphAlignment.CENTER

    val r = p.createRun()
    r.setText(marcaDeAguaText)
    r.color = "C0C0C0"
    r.fontSize = 42
    r.isBold = true


    //pie de página

    val footer = doc.createHeaderFooterPolicy().createFooter(XWPFHeaderFooterPolicy.DEFAULT)
    val f = footer.createParagraph()
    f.alignment = ParagraphAlignment.CENTER


    val piePag = f.createRun()
    piePag.setText(footerText)
    piePag.color = "C0C0C0"
    piePag.fontSize = 12
    piePag.isBold = true


    //contenido principal

    val body = doc.createParagraph()
    body.createRun().setText("Documento creado como ejemplo. Que viva el chivo guisao")

    //Guardar tempralmente

    val out = File.createTempFile("generated -", ".docx")
    FileOutputStream(out).use {  doc.write(it) }

    return out
}
