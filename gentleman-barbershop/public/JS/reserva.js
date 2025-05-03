document.addEventListener('DOMContentLoaded', () => {
    const fechaInput = document.getElementById('fecha');
    if (fechaInput) {
      const today = new Date().toISOString().slice(0, 10); // Formato YYYY-MM-DD
      fechaInput.setAttribute('min', today);
    }
  
    window.submitCita = async function(tipo) {
      console.log('Enviando cita para tipo:', tipo);
      const form = document.getElementById('cita-form');
      if (!form) return;
  
      const formData = new FormData(form);
      const data = {};
  
      // Validación básica
      const servicioId = formData.get('servicio_id');
      const peluqueroId = formData.get('peluquero_id');
      const fecha = formData.get('fecha');
      const hora = formData.get('hora');
      const telefono = formData.get('telefono');
  
      if (!servicioId || !peluqueroId || !fecha || !hora) {
        alert('Por favor, selecciona un servicio, un peluquero, una fecha y una hora.');
        return;
      }
  
      if (tipo === 'registrado') {
        if (!telefono) {
          alert('Por favor, ingresa un número de teléfono.');
          return;
        }
        data.servicio_id = servicioId;
        data.peluquero_id = peluqueroId;
        data.fecha = fecha;
        data.hora = hora;
        data.telefono = telefono;
      } else {
        const nombre = formData.get('nombre');
        const apellidos = formData.get('apellidos');
        const fechaNac = formData.get('fecha_nac');
        if (!nombre || !apellidos || !fechaNac || !telefono) {
          alert('Por favor, completa todos los campos para un nuevo cliente.');
          return;
        }
        data.servicio_id = servicioId;
        data.peluquero_id = peluqueroId;
        data.fecha = fecha;
        data.hora = hora;
        data.nombre = nombre;
        data.apellidos = apellidos;
        data.fecha_nac = fechaNac;
        data.telefono = telefono;
      }
  
      console.log('Datos enviados:', data);
  
      const response = await fetch(`/reserva/${tipo}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(data).toString(),
      });
  
      console.log('Respuesta:', response);
  
      if (response.redirected) {
        window.location.href = response.url;
      } else {
        const text = await response.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(text, 'text/html');
        const errorElement = doc.querySelector('p[style="color: red;"]');
        const errorMessage = errorElement ? errorElement.textContent : 'Error desconocido. Intenta de nuevo.';
        alert(errorMessage);
      }
    };
  });