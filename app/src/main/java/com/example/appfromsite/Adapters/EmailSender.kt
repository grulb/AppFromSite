package com.example.appfromsite.Adapters

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender(private val userMail: String) {
    private val smtpHost = "smtp.gmail.com"
    private val smtpPort = "587"
    private val username = userMail
    private val password = "vdmgsofxkrgabgkl"

    fun sendEmail(recipient: String, subject: String, body: String) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", smtpHost)
            put("mail.smtp.port", smtpPort)
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(username))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                setSubject(subject)
                setText(body)
            }

            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw Exception("Не удалось отправить письмо: ${e.message}")
        }
    }
}