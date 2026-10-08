package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Instrumento;
import cr.ac.una.sicobuws.model.Notificacion;
import cr.ac.una.sicobuws.model.NotificacionDto;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.annotation.Resource;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Envío de correos HTML (JavaMail de Payara) y bitácora de notificaciones (tabla NOTIFICACION). */
@Stateless
@LocalBean
public class NotificacionService {

    private static final Logger LOG = Logger.getLogger(NotificacionService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    @Resource(lookup = "mail/Sicobu")
    private Session mailSession;

    /** Lista la bitácora de notificaciones de un instrumento. */
    public Respuesta getNotificaciones(Long idInstrumento) {
        try {
            TypedQuery<Notificacion> qry = em.createNamedQuery("Notificacion.findByInstrumento", Notificacion.class);
            qry.setParameter("idInstrumento", idInstrumento);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Notificaciones", aDtos(qry.getResultList()));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar las notificaciones.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar las notificaciones.", "getNotificaciones " + ex.getMessage());
        }
    }

    /** Lista la bitácora por tipo de notificación (ACTIVACION, CREACION, ESTADO, PAGO, PRUEBA...). */
    public Respuesta getNotificacionesPorTipo(String tipo) {
        try {
            TypedQuery<Notificacion> qry = em.createNamedQuery("Notificacion.findByTipo", Notificacion.class);
            qry.setParameter("tipo", tipo);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Notificaciones", aDtos(qry.getResultList()));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar las notificaciones.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar las notificaciones.", "getNotificacionesPorTipo " + ex.getMessage());
        }
    }

    /** Envía un correo de prueba para verificar el recurso mail/Sicobu. */
    public Respuesta enviarPrueba(String correo) {
        String cuerpo = plantilla("Correo de prueba", "<p>Si usted recibe este mensaje, el envío de correos de SicobuUNA funciona correctamente.</p>");
        if (enviarCorreo(null, correo, "SicobuUNA - Correo de prueba", cuerpo, "PRUEBA", null)) {
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        }
        return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "No se pudo enviar el correo. Revise el recurso mail/Sicobu y el log del servidor.", "enviarPrueba falló el envío");
    }

    /**
     * Envía un correo HTML y registra el envío en la bitácora. NUNCA lanza excepción: si falla,
     * lo deja en el log y devuelve false, para que no se caiga el guardado que lo llamó.
     * idInstrumento y adjunto son opcionales (null).
     */
    public boolean enviarCorreo(Long idInstrumento, String destinatario, String asunto, String cuerpoHtml, String tipo, File adjunto) {
        try {
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, false));
            msg.setSubject(asunto, "UTF-8");

            MimeMultipart partes = new MimeMultipart();
            MimeBodyPart html = new MimeBodyPart();
            html.setContent(cuerpoHtml, "text/html; charset=UTF-8");
            partes.addBodyPart(html);
            if (adjunto != null && adjunto.exists()) {
                MimeBodyPart archivo = new MimeBodyPart();
                archivo.attachFile(adjunto);
                partes.addBodyPart(archivo);
            }
            msg.setContent(partes);
            Transport.send(msg);
        } catch (MessagingException | java.io.IOException | RuntimeException ex) {
            LOG.log(Level.SEVERE, "No se pudo enviar el correo a " + destinatario, ex);
            return false;
        }
        registrar(idInstrumento, destinatario, asunto, tipo);
        return true;
    }

    /** Estructura HTML común de todos los correos del sistema. */
    public String plantilla(String titulo, String contenidoHtml) {
        return "<html><body style=\"margin:0;padding:0;background:#f2f4f7;font-family:Arial,sans-serif;\">"
                + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tr><td align=\"center\" style=\"padding:24px;\">"
                + "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#ffffff;border-radius:8px;overflow:hidden;\">"
                + "<tr><td style=\"background:#8c2f39;color:#ffffff;padding:20px 28px;font-size:22px;font-weight:bold;\">SicobuUNA</td></tr>"
                + "<tr><td style=\"padding:28px;color:#333333;font-size:15px;line-height:1.5;\">"
                + "<h2 style=\"margin-top:0;color:#8c2f39;\">" + titulo + "</h2>" + contenidoHtml + "</td></tr>"
                + "<tr><td style=\"background:#f2f4f7;color:#888888;padding:14px 28px;font-size:12px;\">"
                + "Este es un mensaje automático, por favor no responda a este correo.</td></tr>"
                + "</table></td></tr></table></body></html>";
    }

    // ---------- Internos ----------

    private void registrar(Long idInstrumento, String destinatario, String asunto, String tipo) {
        try {
            Notificacion n = new Notificacion();
            if (idInstrumento != null) {
                n.setInstrumento(em.find(Instrumento.class, idInstrumento));
            }
            n.setDestinatario(destinatario);
            n.setAsunto(asunto);
            n.setFechaEnvio(LocalDateTime.now());
            n.setTipo(tipo);
            em.persist(n);
            em.flush();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "El correo se envió pero no se pudo registrar en la bitácora.", ex);
        }
    }

    private List<NotificacionDto> aDtos(List<Notificacion> lista) {
        List<NotificacionDto> dtos = new ArrayList<>();
        for (Notificacion n : lista) {
            dtos.add(new NotificacionDto(n));
        }
        return dtos;
    }
}