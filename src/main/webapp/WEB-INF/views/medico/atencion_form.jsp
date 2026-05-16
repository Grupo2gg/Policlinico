<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Atención Médica</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .container { max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 20px; border-radius: 5px; border: 1px solid #ddd; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], textarea { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .btn { padding: 10px 15px; background-color: #28a745; color: white; border: none; cursor: pointer; border-radius: 3px; font-size: 16px; }
        .btn:hover { background-color: #218838; }
        .btn-cancel { background-color: #dc3545; color: white; text-decoration: none; padding: 10px 15px; border-radius: 3px; display: inline-block; margin-left: 10px; }
        .btn-cancel:hover { background-color: #c82333; }
        .info-cita { background: #e9ecef; padding: 10px; border-radius: 4px; margin-bottom: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Registrar Atención Médica</h2>
        
        <div class="info-cita">
            <p><strong>Paciente:</strong> ${cita.nombrePaciente}</p>
            <p><strong>Motivo de Consulta:</strong> ${cita.motivo}</p>
            <p><strong>Fecha y Hora:</strong> ${cita.fecha} - ${cita.hora}</p>
        </div>
        
        <form action="/medico/atenciones/guardar" method="post">
            <input type="hidden" name="citaId" value="${atencion.citaId}">
            
            <div class="form-group">
                <label for="diagnostico">Diagnóstico:</label>
                <input type="text" id="diagnostico" name="diagnostico" required placeholder="Ej. Faringitis aguda">
            </div>
            
            <div class="form-group">
                <label for="tratamiento">Tratamiento o Receta:</label>
                <textarea id="tratamiento" name="tratamiento" rows="4" required placeholder="Ej. Paracetamol 500mg cada 8 horas por 3 días"></textarea>
            </div>
            
            <div class="form-group">
                <label for="observaciones">Observaciones (Opcional):</label>
                <textarea id="observaciones" name="observaciones" rows="3" placeholder="Notas adicionales sobre el paciente"></textarea>
            </div>
            
            <button type="submit" class="btn">Guardar Atención</button>
            <a href="/medico/atenciones" class="btn-cancel">Cancelar</a>
        </form>
    </div>
</body>
</html>
