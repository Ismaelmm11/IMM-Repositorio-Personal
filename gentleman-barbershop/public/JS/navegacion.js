document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('nav a');
    const contentDiv = document.getElementById('componente');
  
    links.forEach(link => {
      link.addEventListener('click', async (e) => {
        e.preventDefault(); // Evita recargar la página
  
        const partialName = link.getAttribute('data-componente') || link.getAttribute('href').replace('/', '');
        const url = `/componente/${partialName}`;
  
        try {
          const response = await fetch(url);
          const html = await response.text();
  
          console.log(html);
  
          // Actualizar el contenido del div
          contentDiv.innerHTML = html;
  
          // Actualizar la URL en la barra de direcciones
          window.history.pushState({}, '', `/${partialName}`);
  
          // Actualizar el título de la página
          document.title = link.textContent + ' - Gentleman Barbershop';
  
          window.scrollTo(0, 0);
        } catch (error) {
          console.error('Error al cargar el contenido:', error);
        }
      });
    });
  
    // Manejar navegación con botones de atrás/adelante
    window.addEventListener('popstate', async () => {
      const path = window.location.pathname;
      const partialName = path === '/' || path === '' ? 'index' : path.replace('/', '');
      const url = `/componente/${partialName}`;
  
      try {
        const response = await fetch(url);
        const html = await response.text();
        contentDiv.innerHTML = html;
        document.title = partialName.charAt(0).toUpperCase() + partialName.slice(1) + ' - Gentleman Barbershop';
      } catch (error) {
        console.error('Error al manejar popstate:', error);
      }
    });
  });