import android.content.Context
import android.widget.Toast
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun enviarCorreo(context: Context, to: String, subject: String, message: String) {
    // Configurar las propiedades de la sesión de correo
    val props = Properties().apply {
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.auth", "true")
    }

    // Configurar el autenticador para la sesión de correo
    val session = Session.getDefaultInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("nestorleivamora@gmail.com", "Queso2024@")
        }
    })

    try {
        // Crear un objeto MimeMessage
        val mimeMessage = MimeMessage(session)
        // Establecer los campos del mensaje
        mimeMessage.setFrom(InternetAddress("nestorleivamora@gmail.com"))
        mimeMessage.setRecipient(Message.RecipientType.TO, InternetAddress(to))
        mimeMessage.subject = subject
        mimeMessage.setText(message)

        // Enviar el mensaje
        Transport.send(mimeMessage)

        // Mostrar mensaje de confirmación al usuario
        val confirmacion = "Se ha enviado un correo electrónico a $to con la información de registro"
        Toast.makeText(context, confirmacion, Toast.LENGTH_SHORT).show()

    } catch (e: MessagingException) {
        throw RuntimeException("Error al enviar el correo electrónico", e)
    }
}
