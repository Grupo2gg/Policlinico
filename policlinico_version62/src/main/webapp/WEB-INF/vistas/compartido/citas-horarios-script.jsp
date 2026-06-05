<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script>
    const horariosDisponibles = ${empty horariosJson ? '[]' : horariosJson};
    const initialFecha = "${param.initialFecha}";
    const initialHorarioId = "${param.initialHorarioId}";

    const medicoSelect = document.getElementById('medicoSelect');
    const fechaSelect = document.getElementById('fechaSelect');
    const horarioSelect = document.getElementById('horarioSelect');
    const infoDisponibilidad = document.getElementById('infoDisponibilidad');

    function mismoMedico(h, medicoId) {
        return String(h.medicoId) === String(medicoId);
    }

    function filtrarFechas() {
        const medicoId = medicoSelect.value;
        fechaSelect.innerHTML = '';

        if (!medicoId) {
            fechaSelect.appendChild(opcion('', '-- Seleccione un médico primero --'));
            infoDisponibilidad.innerText = 'Elige un médico primero para ver sus fechas y días de disponibilidad.';
            filtrarHorarios();
            return;
        }

        const horariosDelMedico = horariosDisponibles.filter(h => mismoMedico(h, medicoId));
        const fechasUnicas = [...new Set(
            horariosDelMedico.map(h => h.fecha).filter(f => f && String(f).trim() !== '')
        )].sort();

        if (fechasUnicas.length === 0) {
            fechaSelect.appendChild(opcion('', 'No hay fechas disponibles'));
            infoDisponibilidad.innerText = 'Este médico no tiene fechas con horarios DISPONIBLES. Configure disponibilidad y horarios en administración.';
            filtrarHorarios();
            return;
        }

        fechaSelect.appendChild(opcion('', '-- Seleccione una fecha --'));
        fechasUnicas.forEach(fecha => {
            const hMatch = horariosDelMedico.find(h => h.fecha === fecha);
            const dia = hMatch && hMatch.diaSemana ? hMatch.diaSemana : '';
            const etiqueta = dia ? (fecha + ' (' + dia + ')') : fecha;
            const opt = opcion(fecha, etiqueta);
            if (fecha === initialFecha) {
                opt.selected = true;
            }
            fechaSelect.appendChild(opt);
        });

        const dias = [...new Set(horariosDelMedico.map(h => h.diaSemana).filter(d => d))].join(', ');
        infoDisponibilidad.innerText = 'Días de atención: ' + (dias || '—') + '. Fechas: ' + fechasUnicas.join(', ');
        filtrarHorarios();
    }

    function filtrarHorarios() {
        const medicoId = medicoSelect.value;
        const fecha = fechaSelect.value;
        horarioSelect.innerHTML = '';

        if (!medicoId || !fecha) {
            horarioSelect.appendChild(opcion('', '-- Seleccione una fecha primero --'));
            return;
        }

        const horariosFiltrados = horariosDisponibles.filter(h =>
            mismoMedico(h, medicoId) && h.fecha === fecha
        );

        if (horariosFiltrados.length === 0) {
            horarioSelect.appendChild(opcion('', 'No hay horarios disponibles'));
            return;
        }

        horarioSelect.appendChild(opcion('', '-- Seleccione un horario --'));
        horariosFiltrados.forEach(h => {
            const hi = h.horaInicio || '';
            const hf = h.horaFin || '';
            if (!hi && !hf) {
                return;
            }
            const opt = opcion(String(h.id), hi + ' - ' + hf);
            if (String(h.id) === String(initialHorarioId)) {
                opt.selected = true;
            }
            horarioSelect.appendChild(opt);
        });
    }

    function opcion(valor, texto) {
        const opt = document.createElement('option');
        opt.value = valor;
        opt.innerText = texto;
        return opt;
    }

    medicoSelect.addEventListener('change', filtrarFechas);
    fechaSelect.addEventListener('change', filtrarHorarios);

    document.querySelector('form.formulario').addEventListener('submit', function (e) {
        if (!horarioSelect.value) {
            e.preventDefault();
            alert('Debe seleccionar un horario disponible antes de guardar la cita.');
        }
    });

    window.addEventListener('DOMContentLoaded', filtrarFechas);
</script>
